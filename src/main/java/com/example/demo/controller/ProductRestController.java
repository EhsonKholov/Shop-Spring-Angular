package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Product> create(HttpServletRequest request,
                                          MultipartFile file,
                                          @RequestParam(value = "product") String product
                                          //@RequestParam(value = "chunk", required = false) String chunkRequest,
                                          //@RequestParam(value = "chunks", required = false) String totalChunks
    ) throws IOException {

        Product p = productService.getProductFromJson(product, file);

        return new ResponseEntity<Product>(productRepository.save(p), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/{productId}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable("productId") Long productId) {

        Product product = productService.getById(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping(value = "/remove/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeById(@PathVariable("productId") Long productId) {

        Product product = productService.getById(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productService.remove(productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit/{productId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> editById(@PathVariable("productId") Long productId, @RequestBody Product product) {

        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!product.getProductId().equals(productId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Product p = productService.update(product);

        return new ResponseEntity<>(p, HttpStatus.OK);
    }

}
