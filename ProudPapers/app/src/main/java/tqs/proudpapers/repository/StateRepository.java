package tqs.proudpapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.proudpapers.entity.State;

/**
 * @author wy
 * @date 2021/6/14 16:46
 */
public interface StateRepository extends JpaRepository<State, Integer> {

}
