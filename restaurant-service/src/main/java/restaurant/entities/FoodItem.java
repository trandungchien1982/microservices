package restaurant.entities;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"food_item\"")
@Data
public class FoodItem {

    @Id
    private long id;

    @Column
    private String orderId;

    @Column
    private String foodName;

    @Column(name = "create_date")
    private Date createDate;

}
