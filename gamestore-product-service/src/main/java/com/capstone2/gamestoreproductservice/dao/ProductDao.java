package com.capstone2.gamestoreproductservice.dao;

import com.capstone2.gamestoreproductservice.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts();

    Product postAProduct(Product product);
    Product getProductById(int id);

    String updateProduct(Product product);
    String deleteProduct(int id);
}
