package tqs.proudpapers.service;

import tqs.proudpapers.entity.Product;

import java.util.List;

/**
 * @author wy
 * @date 2021/6/5 18:44
 */
public interface ProductService {
    List<Product> searchByKeyWord(String name);
    Product searchById(Integer id);
}
