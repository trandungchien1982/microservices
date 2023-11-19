package orders.entities;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"orders\"")
@Data
public class Orders {

    @Id
    private long id;

    @Column
    private String status;

    @Column
    private String orderName;

    @Column
    private String cardNumber;

    @Column
    private String customerName;

    @Column
    private String description;

    @Column(name = "create_date")
    private Date createDate;

}
