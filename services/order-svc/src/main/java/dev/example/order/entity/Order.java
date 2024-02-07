package dev.example.order.entity;

import dev.example.common.model.Status;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name ="order_table" )
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private Long productId;
    private  Double price;
    private Status status;
    @PrePersist
    void persist(){
        this.id=0L;
    }
}
