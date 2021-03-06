package tqs.proudpapers.controller;

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

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = {"/", "/index"})
    public String index(Model model){
        model.addAttribute("products", productService.getAll());
        return "index";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "signUp";
    }

    @PostMapping("/signup")
    public String signUp(ClientDTO clientDTO,
                         String cardNumber,
                         String cardExpirationMonth,
                         String cvc,
                         Model model){

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardNumber(cardNumber);
        paymentMethod.setCvc(cvc);
        paymentMethod.setCardExpirationMonth(cardExpirationMonth);

        clientDTO.setPaymentMethod(paymentMethod);
        Client client = clientService.saveClient(clientDTO);

        if(client != null){
            model.addAttribute("email", client.getEmail());
            return "redirect:login";
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

        model.addAttribute("products", productService.getAll());
        return "redirect:/index";
    }


    @GetMapping("/account/{id}/{page}")
    public String accountInfo(@PathVariable("id") Integer id,
                              @PathVariable("page") String page,
                              Model model){

        String[] pages = {"myinfo", "address", "contact", "payment", "cart", "deliveries"};
        ClientDTO client = clientService.getClientById(id);
        CartDTO cart= cartService.getCartByClientID(id);
        client.setCartDTO(cart);
        PaymentMethod p = client.getPaymentMethod();

        model.addAttribute("client", client);

        for (String s : pages) {
            model.addAttribute(s, s.equals(page));
        }

        if ("deliveries".equals(page)){
            model.addAttribute("deliveriesDTO", deliveryService.getDeliveries(id));
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

    @PostMapping("/account/{clientId}/purchase")
    @Transactional
    public ResponseEntity<Integer> purchase(@PathVariable("clientId") Integer clientId,
                                                  ClientDTO clientDTO,
                                                  String cardNumber,
                                                  String cardExpirationMonth,
                                                  String cvc){

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardExpirationMonth(cardExpirationMonth);
        paymentMethod.setCvc(cvc);
        paymentMethod.setCardNumber(cardNumber);

        clientDTO.setPaymentMethod(paymentMethod);
        clientDTO.setId(clientId);
        Integer deliveryId = cartService.buyAllProductsInTheCart(clientDTO);
        sendDeliveryToEasyDelivery(deliveryId, clientDTO);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/update/{id}/state/{state}")
    @Transactional
    public ResponseEntity<Object> updateState(@PathVariable("id") Integer id,
                                              @PathVariable("state") String state){

        deliveryService.changeStateOfDelivery(id, state);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private void sendDeliveryToEasyDelivery(Integer deliveryId, ClientDTO clientDTO){
        Map<String, String> request = Map.of("store", "1",
                                    "client_telephone", clientDTO.getTelephone(),
                                    "start", "UA",
                                    "destination", clientDTO.getZip() + " " + clientDTO.getCity());

        ResponseEntity<Integer> response = restTemplate.postForEntity("localhost:8080/delivery", request, Integer.class);
        Integer id_delivery_store = response.getBody();
        deliveryService.setDeliveryIdInStore(deliveryId, id_delivery_store);
    }
}

