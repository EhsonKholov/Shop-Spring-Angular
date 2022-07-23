package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


@Entity
@Data
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @GenericGenerator(
            name = "wikiSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "PRODUCT_SEQUENCE"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "wikiSequenceGenerator")
    @Column(name = "productId")
    private Long productId;

    private String type;
    private String title;
    private String photo;
    private byte[] imageBytes;
    @Column(columnDefinition = "Text")
    private String info;
    private BigDecimal price = BigDecimal.ZERO;
    private Date created = new Date();
    /*@OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "imageId")
    private List<ImageModel> images;*/
    /*@Column(columnDefinition = "Text")*/

    /*@JsonBackReference
    @ManyToMany
    private List<Order> orders;*/

    public Product() {
    }

}
