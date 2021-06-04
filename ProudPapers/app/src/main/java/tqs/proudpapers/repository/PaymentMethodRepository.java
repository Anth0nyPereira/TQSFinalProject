package tqs.proudpapers.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import tqs.proudpapers.entity.PaymentMethod;

/**
 * @author wy
 * @date 2021/6/4 12:43
 */
public interface PaymentMethodRepository extends PagingAndSortingRepository<PaymentMethod, Integer> {
    PaymentMethod getById(Integer id);
}
