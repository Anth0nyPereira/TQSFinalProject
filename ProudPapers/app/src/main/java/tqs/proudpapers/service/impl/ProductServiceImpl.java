package tqs.proudpapers.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.proudpapers.entity.Product;
import tqs.proudpapers.repository.ProductRepository;
import tqs.proudpapers.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

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
        return repository.getProductByNameContains(name)
                .stream()
                .filter(p -> p.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public Product searchById(Integer id) {
        return repository.getProductById(id);
    }

    @Override
    public Product save(Product p) {
        return repository.save(p);
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }
}
