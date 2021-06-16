package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wy
 * @date 2021/6/14 16:44
 */
@Setter
@Entity
@Table(name="state")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="description")
    private String description;

    @Column(name="delivery")
    private Integer delivery;

    @Column(name="timestamp")
    private Date timestamp;
}
