package com.capstone2.gamestorelevelupservice.controller;

import com.capstone2.gamestorelevelupservice.dao.LevelUpDaoJdbcTemplateImpl;
import com.capstone2.gamestorelevelupservice.model.LevelUp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class LevelUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LevelUpDaoJdbcTemplateImpl levelUpDao;

    private ObjectMapper om = new ObjectMapper();
    private LevelUp levelUpExpected;

    @Before
    public void setUp() {
        levelUpExpected = new LevelUp(9, 12,  LocalDate.of(2017, 07, 26));
        levelUpExpected.setLevelUpId(1);
    }

    @Test
    public void testPostLevelUp() throws Exception {

        LevelUp levelUpInfo = new LevelUp(9, 12, LocalDate.of(2017, 07, 26));

        LevelUp levelUpNoCustomerId = new LevelUp();
        levelUpNoCustomerId.setPoints(7);
        levelUpNoCustomerId.setMemberDate(LocalDate.of(2017, 07, 26));

        LevelUp levelUpNoPoints = new LevelUp();
        levelUpNoPoints.setCustomerId(9);
        levelUpNoPoints.setMemberDate(LocalDate.of(2017, 07, 26));

        LevelUp levelUpNoMemberDate = new LevelUp();
        levelUpNoMemberDate.setCustomerId(9);
        levelUpNoMemberDate.setPoints(12);

        Mockito.when(levelUpDao.createLevelUp(levelUpInfo)).thenReturn(levelUpExpected);

        String expectedLevelUpJson = om.writeValueAsString(levelUpExpected);
        String levelUpInfoJson = om.writeValueAsString(levelUpInfo);
        String levelUpNoCustomerIdJson = om.writeValueAsString(levelUpNoCustomerId);
        String levelUpNoPointsJson = om.writeValueAsString(levelUpNoPoints);
        String levelUpNoMemberDateJson = om.writeValueAsString(levelUpNoMemberDate);

        mockMvc.perform(MockMvcRequestBuilders.post("/levelUp")
                .content(levelUpInfoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedLevelUpJson));

        mockMvc.perform(MockMvcRequestBuilders.post("/levelUp")
                .content(levelUpNoCustomerIdJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(MockMvcRequestBuilders.post("/levelUp")
                .content(levelUpNoPointsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(MockMvcRequestBuilders.post("/levelUp")
                .content(levelUpNoMemberDateJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void testGetLevelUp() throws Exception {

        Mockito.when(levelUpDao.getLevelUpById(0)).thenReturn(null);
        Mockito.when(levelUpDao.getLevelUpById(1)).thenReturn(levelUpExpected);

        String expectedLevelUpJson = om.writeValueAsString(levelUpExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/levelUp/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedLevelUpJson));

        mockMvc.perform(MockMvcRequestBuilders.get("/levelUp/0"))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetAllLevelUps() throws Exception {
        LevelUp levelUp1 = new LevelUp(4, 5,LocalDate.of(2017, 07, 26));
        levelUp1.setLevelUpId(2);

        LevelUp levelUp2 = new LevelUp(7, 2,LocalDate.of(2017, 07, 26));
        levelUp2.setLevelUpId(3);

        List<LevelUp> allLevelUps = new ArrayList<>();
        allLevelUps.add(levelUp1);
        allLevelUps.add(levelUp2);

        String allLevelUpsJson = om.writeValueAsString(allLevelUps);

        Mockito.when(levelUpDao.getAllLevesUps()).thenReturn(allLevelUps);

        mockMvc.perform(MockMvcRequestBuilders.get("/levelUp"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allLevelUpsJson));
    }

    @Test
    public void deleteLevelUp() throws Exception {

        String successful = "Deletion successful. LevelUp with levelUpId '" + 1 + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No levelUp with levelUpId '" + 0 + "' was found.";

        Mockito.when(levelUpDao.deleteLevelUp(0)).thenReturn(unsuccessful);
        Mockito.when(levelUpDao.deleteLevelUp(1)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.delete("/levelUp/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.delete("/levelUp/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

    }

    @Test
    public void updateLevelUp() throws Exception {
        LevelUp levelUpId0NotAllowed =
                new LevelUp(5, 9,LocalDate.of(2017, 07, 26));
        levelUpId0NotAllowed.setLevelUpId(0);

        LevelUp levelUpId4NotFound =
                new LevelUp(10, 21,LocalDate.of(2017, 07, 26));
        levelUpId4NotFound.setLevelUpId(4);

        String levelUpId0NotAllowedJson = om.writeValueAsString(levelUpId0NotAllowed);
        String levelUp4NotFoundJson = om.writeValueAsString(levelUpId4NotFound);
        String levelUpExpectedJson = om.writeValueAsString(levelUpExpected);

        String successful = "Update successful: "+ levelUpExpected.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        Mockito.when(levelUpDao.updateLevelUp(levelUpId4NotFound)).thenReturn(unsuccessful);
        Mockito.when(levelUpDao.updateLevelUp(levelUpExpected)).thenReturn(successful);

        mockMvc.perform(MockMvcRequestBuilders.put("/levelUp")
                .content(levelUpExpectedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successful));

        mockMvc.perform(MockMvcRequestBuilders.put("/levelUp")
                .content(levelUp4NotFoundJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(unsuccessful));

        mockMvc.perform(MockMvcRequestBuilders.put("/levelUp")
                .content(levelUpId0NotAllowedJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isUnprocessableEntity());
    }
}
