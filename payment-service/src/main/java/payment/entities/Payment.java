package payment.entities;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"payment\"")
@Data
public class Payment {

    @Id
    private long id;

    @Column
    private String orderId;

    @Column
    private String cardNumber;

    @Column(name = "create_date")
    private Date createDate;

}
