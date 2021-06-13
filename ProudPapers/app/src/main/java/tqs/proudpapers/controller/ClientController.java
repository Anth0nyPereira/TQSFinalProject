package tqs.proudpapers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.service.CartService;
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

    @Autowired
    CartService cartService;

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

        CartDTO cart = cartService.getCartByClientID(client.getId());
        client.setCartDTO(cart);
        session.setAttribute("client", client);
        return "index";
    }


    @GetMapping("/account/{id}/{page}")
    public String accountInfo(@PathVariable("id") Integer id,
                              @PathVariable("page") String page,
                              Model model){

        String[] pages = {"myinfo", "address", "contact", "payment", "deliveries"};
        ClientDTO client = clientService.getClientById(id);
        CartDTO cart= cartService.getCartByClientID(id);
        client.setCartDTO(cart);

        model.addAttribute("cartDto", cart);
        model.addAttribute("client", client);

        for (String s : pages) {
            model.addAttribute(s, s.equals(page));
        }

        return "account";
    }

    @ResponseBody
    @PostMapping("/account/{clientId}/add_to_cart/{productId}")
    public ResponseEntity<ProductOfCart> addProductToCart(@PathVariable("clientId") Integer clientId,
                                           @PathVariable("productId") Integer productId,
                                           @RequestParam(value = "quantity", defaultValue = "1") Integer quantity){
        ProductOfCart saved = cartService.save(clientId, productId, quantity);
        if (saved != null)
            return new ResponseEntity<>(saved, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}

