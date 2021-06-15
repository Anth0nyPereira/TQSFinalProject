package tqs.proudpapers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author wy
 * @date 2021/6/3 15:50
 */
@Data
@NoArgsConstructor
public class ClientDTO implements Serializable {
    @NotBlank
    private Integer id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String zip;

    @NotBlank
    private String city;

    @NotBlank
    private String telephone;

    private CartDTO cartDTO;

    private PaymentMethod paymentMethod;

}
