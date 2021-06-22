package tqs.proudpapers.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author wy
 * @date 2021/6/3 15:50
 */
@Data
@NoArgsConstructor
public class ClientDTO implements Serializable {
    @NotBlank
    @ApiModelProperty(value = "Client's id", example = "1")
    private Integer id;

    @NotBlank
    @ApiModelProperty(value = "Client's name", example = "Alex")
    private String name;

    @Email
    @ApiModelProperty(value = "Client's email", example = "alex@ua.pt")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "Client's password")
    private String password;

    @NotBlank
    @ApiModelProperty(value = "Client's zip", example = "2222-222")
    private String zip;

    @NotBlank
    @ApiModelProperty(value = "Client's city", example = "aveiro")
    private String city;

    @NotBlank
    @ApiModelProperty(value = "Client's telephone", example = "915 111 111")
    private String telephone;

    @ApiModelProperty(value = "Client's cart")
    private CartDTO cartDTO;

    @ApiModelProperty(value = "Client's payment method")
    private PaymentMethod paymentMethod;

}
