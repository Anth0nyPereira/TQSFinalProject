package tqs.proudpapers.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tqs.proudpapers.entity.*;
import tqs.proudpapers.service.CartService;
import tqs.proudpapers.service.ClientService;
import tqs.proudpapers.service.DeliveryService;
import tqs.proudpapers.service.ProductService;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Map;

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

    @Autowired
    ProductService productService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation("Get the index page.")
    @GetMapping(value = {"/", "/index"})
    public String index(Model model){
        model.addAttribute("products", productService.getAll());
        return "index";
    }

    @ApiOperation("Get the signUp page")
    @GetMapping("/signup")
    public String signUp(){
        return "signUp";
    }

    @ApiOperation("Try to create a new account with given infos")
    @PostMapping("/signup")
    public String signUp(ClientDTO clientDTO,
                         String cardNumber,
                         String cardExpirationMonth,
                         String cvc,
                         Model model){

        var paymentMethod = new PaymentMethod();
        paymentMethod.setCardNumber(cardNumber);
        paymentMethod.setCvc(cvc);
        paymentMethod.setCardExpirationMonth(cardExpirationMonth);

        clientDTO.setPaymentMethod(paymentMethod);
        var client = clientService.saveClient(clientDTO);

        if(client != null){
            model.addAttribute("email", client.getEmail());
            return "redirect:login";
        }

        model.addAttribute("emailDuplicated", true);
        return "signUp";
    }

    @ApiOperation("Get the login page")
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @ApiOperation("Try to login with given email and password")
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

        model.addAttribute("products", productService.getAll());
        return "redirect:/index";
    }

    @ApiOperation("Get indicated info of the user with the given id")
    @GetMapping("/account/{id}/{page}")
    public String accountInfo(@PathVariable("id") Integer id,
                              @PathVariable("page") String page,
                              Model model){

        var pages = new String[]{"myinfo", "address", "contact", "payment", "cart", "deliveries"};
        ClientDTO client = clientService.getClientById(id);
        CartDTO cart= cartService.getCartByClientID(id);
        client.setCartDTO(cart);

        model.addAttribute("client", client);

        for (String s : pages) {
            model.addAttribute(s, s.equals(page));
        }

        if ("deliveries".equals(page)){
            model.addAttribute("deliveriesDTO", deliveryService.getDeliveries(id));
        }

        return "account";
    }

    @ApiOperation("Add a product to the cart of the user with the given id")
    @ResponseBody
    @PostMapping("/account/{clientId}/add_to_cart/{productId}")
    public ResponseEntity<Object> addProductToCart(@PathVariable("clientId") Integer clientId,
                                           @PathVariable("productId") Integer productId,
                                           @RequestParam(value = "quantity", defaultValue = "1") Integer quantity){
        ProductOfCart saved = cartService.save(clientId, productId, quantity);
        if (saved != null)
            return new ResponseEntity<>(saved, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @ApiOperation("Buy all products in the cart of the user with the given id")
    @PostMapping("/account/{clientId}/purchase")
    @Transactional
    public String purchase(@PathVariable("clientId") Integer clientId,
                                                  ClientDTO clientDTO,
                                                  String cardNumber,
                                                  String cardExpirationMonth,
                                                  String cvc){

        var paymentMethod = new PaymentMethod();
        paymentMethod.setCardExpirationMonth(cardExpirationMonth);
        paymentMethod.setCvc(cvc);
        paymentMethod.setCardNumber(cardNumber);

        clientDTO.setPaymentMethod(paymentMethod);
        clientDTO.setId(clientId);
        Integer deliveryId = cartService.buyAllProductsInTheCart(clientDTO);
        sendDeliveryToEasyDelivery(deliveryId, clientDTO);
        return "redirect:/account/" + clientId + "/deliveries";
    }

    @ApiOperation("Update the indicated delivery's state")
    @PostMapping("/update/{id}/state/{state}")
    @Transactional
    public ResponseEntity<Object> updateState(@PathVariable("id") Integer id,
                                              @PathVariable("state") String state){

        deliveryService.changeStateOfDelivery(id, state);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private void sendDeliveryToEasyDelivery(Integer deliveryId, ClientDTO clientDTO){
        Map<String, Object> request = Map.of("store", 1,
                                    "client_telephone", clientDTO.getTelephone(),
                                    "start", "Universidade de Aveiro",
                                    "destination", clientDTO.getZip() + " " + clientDTO.getCity());

        ResponseEntity<Integer> response = restTemplate.postForEntity("http://deti-tqs-06.ua.pt:8080/api/delivery", request, Integer.class);
        Integer idDeliveryStore = response.getBody();
        deliveryService.setDeliveryIdInStore(deliveryId, idDeliveryStore);
    }
}

