package tqs.proudpapers.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ProductRepository;
import tqs.proudpapers.service.ProductService;

import java.util.List;

/**
 * @author wy
 * @date 2021/6/6 17:07
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository repository;

    @Override
    public List<Product> searchByKeyWord(String name) {
        return repository.getProductByNameContains(name);
    }

    @Override
    public Product searchById(Integer id) {
        return repository.getProductById(id);
    }
}
