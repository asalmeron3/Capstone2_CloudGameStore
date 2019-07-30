package com.capstone2.gamestoreretailservice.feignClients;

import com.capstone2.gamestoreretailservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "customer-service")
public interface CustomerClient {

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    Customer getCustomerById(@PathVariable int id);

}
