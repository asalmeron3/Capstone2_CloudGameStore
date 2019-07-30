package com.capstone2.gamestoreretailservice.feignClients;

import com.capstone2.gamestoreretailservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product-service")
public interface ProductClient {

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    Product getProductById(@PathVariable int id);
}
