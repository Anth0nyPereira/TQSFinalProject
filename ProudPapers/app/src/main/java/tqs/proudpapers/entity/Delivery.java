package tqs.proudpapers.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/3 16:05
 */
@Setter
@Getter
@Entity
@Table(name="delivery")
public class Delivery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="total_price")
    private Double totalPrice = 0.0;

    @Column(name="client")
    private Integer client;

    @Column(name="state")
    private String state = "awaiting_processing";

    @Column(name="id_delivery_store")
    private Integer idDeliveryStore;

    @Transient
    private List<ProductOfDeliveryDTO> productsOfDelivery;

    public void setClient(Integer clientId) {
        this.client = clientId;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setProductsOfDelivery(List<ProductOfDeliveryDTO> products) {
        this.productsOfDelivery = products;
    }

    public List<ProductOfDeliveryDTO> getProductsOfDelivery() {
        return productsOfDelivery;
    }

    public int getIdDeliveryStore() {
        return idDeliveryStore;
    }

    public void setId(int deliveryId) {
        this.id = deliveryId;
    }
}
