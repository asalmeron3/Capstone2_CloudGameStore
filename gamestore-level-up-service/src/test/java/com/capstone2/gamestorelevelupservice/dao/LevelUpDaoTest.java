package com.capstone2.gamestorelevelupservice.dao;

import com.capstone2.gamestorelevelupservice.model.LevelUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LevelUpDaoTest {

    @Autowired
    private LevelUpDao levelUpDao;

    @Before
    public void setup() {
        List<LevelUp> allLevelUp = levelUpDao.getAllLevesUps();
        allLevelUp.stream().forEach(inventory -> levelUpDao.deleteLevelUp(inventory.getLevelUpId()));
    }

    @Test
    public void createGetDeleteLevelUp() {
        LevelUp inventoryExpected =
                new LevelUp(5, 9, LocalDate.of(2017, 07, 26));

        LevelUp inventoryToAdd =
                new LevelUp(5, 9, LocalDate.of(2017, 07, 26));

        LevelUp inventoryAdded = levelUpDao.createLevelUp(inventoryToAdd);

        int id = inventoryAdded.getLevelUpId();
        inventoryExpected.setLevelUpId(id);

        Assert.assertEquals(inventoryExpected, inventoryAdded);

        LevelUp inventoryFound = levelUpDao.getLevelUpById(id);
        Assert.assertEquals(inventoryExpected, inventoryFound);

        String successful = "Deletion successful. LevelUp with levelUpId '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No levelUp with levelUpId '" + 0 + "' was found.";

        String deleteSuccessMsg = levelUpDao.deleteLevelUp(id);
        String deleteNotSuccessfulMsg = levelUpDao.deleteLevelUp(0);

        Assert.assertEquals(successful, deleteSuccessMsg);
        Assert.assertEquals(unsuccessful, deleteNotSuccessfulMsg);
    }

    @Test
    public void updatedLevelUp() {
        LevelUp levelUpToFailUpdate =
                new LevelUp(5, 10, LocalDate.of(2017, 07, 26));
        LevelUp levelUpToUpdate =
                new LevelUp(4, 25, LocalDate.of(2017, 07, 26));

        LevelUp inventoryAdded = levelUpDao.createLevelUp(levelUpToUpdate);

        int id = inventoryAdded.getLevelUpId();

        levelUpToUpdate.setLevelUpId(id);
        levelUpToUpdate.setPoints(100);

        String successful = "Update successful: "+ levelUpToUpdate.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        String updateFailedMsg = levelUpDao.updateLevelUp(levelUpToFailUpdate);
        String updateSucceededMsg = levelUpDao.updateLevelUp(levelUpToUpdate);

        Assert.assertEquals(unsuccessful, updateFailedMsg);
        Assert.assertEquals(successful, updateSucceededMsg);
    }

    @Test
    public void getAllLevelUp() {
        LevelUp levelUp1 =
                new LevelUp(5, 10, LocalDate.of(2017, 07, 26));
        LevelUp levelUp2 =
                new LevelUp(4, 25, LocalDate.of(2017, 07, 26));

        levelUp1 = levelUpDao.createLevelUp(levelUp1);
        levelUp2 = levelUpDao.createLevelUp(levelUp2);

        List<LevelUp> allLevelUp = new ArrayList<>();
        allLevelUp.add(levelUp1);
        allLevelUp.add(levelUp2);

        Assert.assertEquals(2, levelUpDao.getAllLevesUps().size());
    }

    @Test
    public void getLevelUpByCustomerId() {
        LevelUp levelUp1 =
                new LevelUp(5, 10, LocalDate.of(2017, 07, 26));
        LevelUp levelUp2 =
                new LevelUp(4, 25, LocalDate.of(2017, 07, 26));

        LevelUp levelUp1Actual = levelUpDao.createLevelUp(levelUp1);
        LevelUp levelUp2Actual = levelUpDao.createLevelUp(levelUp2);

        int id1 = levelUp1Actual.getLevelUpId();
        int id2 = levelUp2Actual.getLevelUpId();

        levelUp1.setLevelUpId(id1);
        levelUp2.setLevelUpId(id2);

        Assert.assertEquals(levelUp1, levelUp1Actual);
        Assert.assertEquals(levelUp2, levelUp2Actual);


    }
}
