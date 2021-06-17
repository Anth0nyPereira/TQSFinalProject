package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author wy
 * @date 2021/6/13 11:50
 */
@Entity
@Table(name="cart")
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="client")
    private Integer client;

    public Integer getId() {
        return this.id;
    }
}
