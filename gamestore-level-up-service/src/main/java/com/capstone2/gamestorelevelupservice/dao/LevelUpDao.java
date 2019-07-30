package com.capstone2.gamestorelevelupservice.dao;

import com.capstone2.gamestorelevelupservice.model.LevelUp;

import java.util.List;

public interface LevelUpDao {

    List<LevelUp> getAllLevesUps();

    LevelUp createLevelUp(LevelUp levelUp);
    LevelUp getLevelUpById(int id);
    LevelUp getLevelUpByCustomerId(int id);


    String updateLevelUp(LevelUp levelUp);
    String deleteLevelUp(int id);
}
