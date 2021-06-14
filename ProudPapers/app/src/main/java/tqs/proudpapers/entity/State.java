package tqs.proudpapers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wy
 * @date 2021/6/14 16:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="state")
public class State {
    @Id
    private Integer id;

    @Column(name="description")
    private String description;

    @Column(name="delivery")
    private Integer delivery;

    @Column(name="timestamp")
    private Date timestamp;
}
