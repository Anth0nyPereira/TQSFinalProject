package tqs.proudpapers.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/6 17:05
 */
@Controller
public class ProductController {

    @Autowired
    ProductService service;

    @ApiOperation("Search products by the given keyword")
    @GetMapping("/search/{key}")
    public String search(@PathVariable(name = "key") String key, Model model){
        var s = "products";
        try{
            var product = service.searchById(Integer.parseInt(key));
            if (product == null)
                model.addAttribute(s, new ArrayList<>());
            else
                model.addAttribute(s, Collections.singletonList(product));

        }catch (Exception e){
            List<Product> products = service.searchByKeyWord(key);
            model.addAttribute(s, products);
        }

        return "search";
    }

    @ApiOperation("Get the products by the given id")
    @GetMapping("/product/{id}")
    public String search(@PathVariable(name = "id") Integer key, Model model){
        var product = service.searchById(key);

        model.addAttribute("product", product);

        return "productDetail";
    }
}
