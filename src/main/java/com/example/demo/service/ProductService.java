package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ProductRepository productRepository;


    public  Product getById(Long id) {
        Product p = productRepository.findById(id).get();
        if (p == null)
            return null;
        p.setImageBytes(imageUploadService.decompressByte(p.getImageBytes()));
        return p;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "productId"))
                .stream()
                .map(p ->
                        {
                            p.setImageBytes(ImageUploadService.decompressByte(p.getImageBytes()));
                            return p;
                        })
                .collect(Collectors.toList());
    }

    public Product getProductFromJson(String obj, MultipartFile file) throws IOException {
        //Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(obj);// response will be the json String
        //Product emp = gson.fromJson(object, Product.class);

        Product p = new Product();
        p.setType(object.get("type").getAsString());
        p.setTitle(object.get("title").getAsString());
        p.setPhoto(file.getOriginalFilename());
        p.setInfo(object.get("info").getAsString());
        p.setPrice(object.get("price").getAsBigDecimal());
        p.setImageBytes(imageUploadService.compressBytes(file.getBytes()));

        return p;
    }

    public void remove(Long productId) {
        productRepository.deleteById(productId);
    }

    public Product update(Product product) {
        product.setImageBytes(imageUploadService.compressBytes(product.getImageBytes()));
        product = productRepository.save(product);
        product.setImageBytes(imageUploadService.decompressByte(product.getImageBytes()));
        return product;
    }
}
