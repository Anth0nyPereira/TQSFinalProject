package com.example.riderapp.Workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.riderapp.Classes.Encomenda;
import com.example.riderapp.Connections.API_Connection;
import com.example.riderapp.Connections.API_Service;
import com.example.riderapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckDeliveriesWorker extends Worker {

    static Set<Integer> oldDeliveries= new HashSet<>();
    static Set<Integer> newDeliveries;
    static API_Connection api_connection;
    static boolean first_time = true;

    public CheckDeliveriesWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        api_connection = API_Service.getClient();

    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        newDeliveries = new HashSet<>();
        oldDeliveries = new HashSet<>(oldDeliveries);
        Call<List<Encomenda>> call = api_connection.api_get_deliveries();
        call.enqueue(new Callback<List<Encomenda>>() {
            @Override
            public void onResponse(Call<List<Encomenda>> call, Response<List<Encomenda>> response) {
                List<Encomenda> allDeliveries = response.body();

                for(Encomenda delivery: allDeliveries) newDeliveries.add(delivery.getId());

                if (first_time){
                    first_time = false;
                }
                else{
                    Set<Integer> diff = new HashSet<>(newDeliveries);
                    diff.removeAll(oldDeliveries);
                    if (diff.size() > 0){

                        Log.w("Worker", "Notification launched");
                        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(CheckDeliveriesWorker.super.getApplicationContext())
                                .setSmallIcon(R.drawable.easydelivers)
                                .setContentTitle("Check your app! Rider")
                                .setContentText(String.format("You have %d new deliveries available!",diff.size()))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManager mNotificationManager =
                                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            String channelId = "Your_channel_id";
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_HIGH);
                            mNotificationManager.createNotificationChannel(channel);
                            notificationbuilder.setChannelId(channelId);
                        }

                        mNotificationManager.notify(1,notificationbuilder.build());
                    }
                }
                oldDeliveries = new HashSet<>(newDeliveries);
                Log.w("Worker", "Deliveries Success");
            }

            @Override
            public void onFailure(Call<List<Encomenda>> call, Throwable t) {
                call.cancel();
                Log.w("Worker", "Error "+t.toString());
            }
        });


        return Result.success();
    }
}
