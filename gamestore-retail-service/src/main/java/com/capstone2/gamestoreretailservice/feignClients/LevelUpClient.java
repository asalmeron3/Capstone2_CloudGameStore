package com.capstone2.gamestoreretailservice.feignClients;

import com.capstone2.gamestoreretailservice.model.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient(value = "level-up-service")
public interface LevelUpClient {

    @RequestMapping(value = "/levelUp/{id}", method = RequestMethod.GET)
    LevelUp getLevelUpById(@PathVariable int id);

    @RequestMapping(value = "/levelUp/customer/{id}", method = RequestMethod.GET)
    LevelUp getLevelUpByCustomerId(@PathVariable int id);

    @RequestMapping(value = "/levelUp", method = RequestMethod.PUT)
    String updateLevelUp(@RequestBody @Valid LevelUp levelUp);
}
