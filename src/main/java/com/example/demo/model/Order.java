package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@Table(name = "Orders")
public class Order {
    private static final long serialVersionUID = 1L;
    @GenericGenerator(
            name = "wikiSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "ORDER_TABLE_SEQUENCE"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "wikiSequenceGenerator")
    @Column(name = "orderId")
    private Long orderId;

    private String name;
    private String phone;
    private String address;
    private String payment;
    @JsonManagedReference
    @JoinTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @OneToMany
    private List<Product> products = new ArrayList<>();
    private BigDecimal price = BigDecimal.ZERO;
    private Date created = new Date();

    public Order() {}
}
