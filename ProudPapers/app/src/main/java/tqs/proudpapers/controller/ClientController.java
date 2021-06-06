package tqs.proudpapers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tqs.proudpapers.entity.Client;
import tqs.proudpapers.entity.ClientDTO;
import tqs.proudpapers.entity.PaymentMethod;
import tqs.proudpapers.service.ClientService;

import javax.servlet.http.HttpSession;

/**
 * @author wy
 * @date 2021/6/4 11:21
 */
@Controller
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/signup")
    public String signUp(){
        return "signUp";
    }

    @PostMapping("/signup")
    public String signUp(ClientDTO clientDTO, PaymentMethod paymentMethod, Model model){
        clientDTO.setPaymentMethod(paymentMethod);
        Client client = clientService.saveClient(clientDTO);

        if(client != null){
            model.addAttribute("email", client.getEmail());
            return "login";
        }

        model.addAttribute("emailDuplicated", true);
        return "signUp";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password,
                        Model model,
                        HttpSession session){
        ClientDTO client = clientService.getClientByEmailAndPass(email, password);
        if(client == null){
            model.addAttribute("failed", true);
            return "login";
        }

        model.addAttribute("client", client);
        session.setAttribute("userEmail", email);
        session.setAttribute("userName", client.getName());
        return "index";
    }

}
