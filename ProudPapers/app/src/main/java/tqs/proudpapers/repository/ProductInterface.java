package tqs.proudpapers.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import tqs.proudpapers.entity.Product;

/**
 * @author wy
 * @date 2021/6/3 22:15
 */
public interface ProductInterface extends PagingAndSortingRepository<Product, Integer>{
    Product getProductByName(String name);
    Product getProductById(Integer id);
}
