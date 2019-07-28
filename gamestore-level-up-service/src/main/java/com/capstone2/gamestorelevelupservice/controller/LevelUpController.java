package com.capstone2.gamestorelevelupservice.controller;

import com.capstone2.gamestorelevelupservice.dao.LevelUpDaoJdbcTemplateImpl;
import com.capstone2.gamestorelevelupservice.exception.NotFoundException;
import com.capstone2.gamestorelevelupservice.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/levelUp")
public class LevelUpController {

    @Autowired
    LevelUpDaoJdbcTemplateImpl levelUpDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelUp addLevelUp(@RequestBody @Valid LevelUp levelUp) {return levelUpDao.createLevelUp(levelUp);}

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LevelUp> getAllLevelUps() {return levelUpDao.getAllLevesUps();}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LevelUp getLevelUpById(@PathVariable int id) {
        return levelUpDao.getLevelUpById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateLevelUp(@RequestBody @Valid LevelUp levelUp) {
        if(levelUp.getLevelUpId() == 0) {
            throw new NotFoundException("An 'levelUpId' is required to update a levelUp");
        }
        return levelUpDao.updateLevelUp(levelUp);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteLevelUp(@PathVariable int id) {return levelUpDao.deleteLevelUp(id);}


}
