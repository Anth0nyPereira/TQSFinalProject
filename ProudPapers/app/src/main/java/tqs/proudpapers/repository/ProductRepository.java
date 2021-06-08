package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.proudpapers.entity.Product;

import java.util.List;

/**
 * @author wy
 * @date 2021/6/3 22:15
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductByNameContains(String name);
    Product getProductById(Integer id);
}
