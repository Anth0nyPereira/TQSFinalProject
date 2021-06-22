package tqs.proudpapers.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

import javax.persistence.*;
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
    @ApiModelProperty(value = "Cart's id",example = "1")
    private Integer id;

    @Column(name="client")
    @ApiModelProperty(value = "Owner's id",example = "1")
    private Integer client;

    public Integer getId() {
        return this.id;
    }

}