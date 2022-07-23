package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class ImageModel {
    private static final long serialVersionUID = 1L;
    @GenericGenerator(
            name = "wikiSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "IMAGE_SEQUENCE"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "wikiSequenceGenerator")
    @Column(name = "imageId")
    private Long imageId;

    private String name;
    private byte[] imageBytes;
    private Long userId;
    private Long productId;
    private Date createdDate = new Date();
    private boolean deleted = false;
}
