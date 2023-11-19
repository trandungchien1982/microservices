package delivery.entities;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"delivery_item\"")
@Data
public class DeliveryItem {

    @Id
    private long id;

    @Column
    private String orderId;

    @Column
    private String foodName;

    @Column
    private String driverName;

    @Column(name = "create_date")
    private Date createDate;

}
