package tqs.proudpapers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.service.ProductService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/6 17:05
 */
@Controller
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping("/search/{key}")
    public String search(@PathVariable(name = "key") String key, Model model){
        try{
            Product product = service.searchById(Integer.parseInt(key));

            if (product == null)
                model.addAttribute("products", new ArrayList<>());
            else
                model.addAttribute("products", List.of(product));

        }catch (Exception e){
            List<Product> products = service.searchByKeyWord(key);
            model.addAttribute("products", products);
        }

        return "search";
    }

    @GetMapping("/product/{id}")
    public String search(@PathVariable(name = "id") Integer key, Model model){
        Product product = service.searchById(key);

        model.addAttribute("product", product);

        return "productDetail";
    }
}
