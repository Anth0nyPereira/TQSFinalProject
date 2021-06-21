package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/3 16:37
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Product's id", example = "1")
    private Integer id;

    @Column(name = "name")
    @ApiModelProperty(notes = "Product's name")
    private String name;

    @Column(name = "price")
    @ApiModelProperty(notes = "Product's price", example = "1.0")
    private Double price;

    @Column(name = "quantity")
    @ApiModelProperty(notes = "Stock of the product", example = "1")
    private Integer quantity;

    @Column(name = "description")
    @ApiModelProperty(notes = "Product's description")
    private String description;
}

