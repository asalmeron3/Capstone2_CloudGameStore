package com.capstone2.gamestoreproductservice.controller;

import com.capstone2.gamestoreproductservice.dao.ProductDaoJdbcTemplateImpl;
import com.capstone2.gamestoreproductservice.exception.NotFoundException;
import com.capstone2.gamestoreproductservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductDaoJdbcTemplateImpl productsDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody @Valid Product product) {return productsDao.postAProduct(product);}

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {return productsDao.getAllProducts();}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProductById(@PathVariable int id) {return productsDao.getProductById(id);}

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateProduct(@RequestBody @Valid Product product) {
        if(product.getId() == 0) {
            throw new NotFoundException("An 'id' is required to update a product");
        }
        return productsDao.updateProduct(product);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteProduct(@PathVariable int id) {return productsDao.deleteProduct(id);}
}
