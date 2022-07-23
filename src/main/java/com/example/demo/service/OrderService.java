package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    public OrderService() {
    }


    public Order getOrderFromJson(String obj) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(obj);// response will be the json String
        JsonObject o;
        //Product emp = gson.fromJson(object, Product.class);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Order order = new Order();

        order.setName(object.get("name").getAsString());
        order.setAddress(object.get("address").getAsString());
        order.setPhone(object.get("phone").getAsString());

        JsonElement productsJSON = object.get("products");

        System.out.println(productsJSON);

        Type collectionType = new TypeToken<List<Product>>(){}.getType();
        List<Product> products = gson.fromJson(productsJSON, collectionType);

        System.out.println(products.toString());

        //List<Product> products = (List<Product>) object.get("products");

        if (products.isEmpty())
            throw new IllegalStateException("Error read products");

        products
                .stream()
                .map(p -> {
                    if (p.getProductId() == null)
                        throw new IllegalStateException("Error read products");
                    p = productService.getById(p.getProductId());

                    totalPrice.add(p.getPrice());

                    return p;
                })
                .collect(Collectors.toList());

        order.setPrice(totalPrice);

        return order;
    }

}
