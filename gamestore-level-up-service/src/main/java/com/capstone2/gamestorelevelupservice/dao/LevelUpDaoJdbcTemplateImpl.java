package com.capstone2.gamestorelevelupservice.dao;

import com.capstone2.gamestorelevelupservice.model.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LevelUpDaoJdbcTemplateImpl implements LevelUpDao {

    @Autowired
    private JdbcTemplate levelUpJdbc;

    public LevelUpDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.levelUpJdbc = jdbcTemplate;
    }

    public LevelUp mapRowToLevelUp(ResultSet rs, int rowNum) throws SQLException {
        LevelUp levelup = new LevelUp(
                rs.getInt("customer_id"),
                rs.getInt("points"),
                rs.getDate("member_date").toLocalDate()
        );
        levelup.setLevelUpId(rs.getInt("level_up_id"));

        return levelup;
    }

    private final static String CREATE_LEVEL_UP =
            "insert into level_up (customer_id, points, member_date) values (?, ?, ?)";
    private final static String GET_ALL_LEVEL_UPS =
            "select * from level_up";
    private final static String GET_LEVEL_UP_BY_ID =
            "select * from level_up where level_up_id = ?";
    private final static String GET_LEVEL_UP_BY_CUSTOMER_ID =
            "select * from level_up where customer_id = ?";
    private final static String UPDATE_LEVEL_UP =
            "update level_up set customer_id = ?, points = ?, member_date = ? where level_up_id = ?";
    private final static String DELETE_LEVEL_UP =
            "delete from level_up where level_up_id = ?";

    @Override
    public List<LevelUp> getAllLevesUps() {
        return levelUpJdbc.query(GET_ALL_LEVEL_UPS, this::mapRowToLevelUp);
    }

    @Override
    public LevelUp createLevelUp(LevelUp levelUp) {
        levelUpJdbc.update(CREATE_LEVEL_UP, levelUp.getCustomerId(), levelUp.getPoints(), levelUp.getMemberDate());

        int id = levelUpJdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);

        levelUp.setLevelUpId(id);
        return levelUp;
    }

    @Override
    public LevelUp getLevelUpById(int id) {
        try {
            return levelUpJdbc.queryForObject(GET_LEVEL_UP_BY_ID, this::mapRowToLevelUp, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String updateLevelUp(LevelUp levelUp) {

        String successful = "Update successful: "+ levelUp.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        int rowsUpdated = levelUpJdbc.update(
                UPDATE_LEVEL_UP,
                levelUp.getCustomerId(),
                levelUp.getPoints(),
                levelUp.getMemberDate(),
                levelUp.getLevelUpId());

        return rowsUpdated == 1 ? successful : unsuccessful;
    }

    @Override
    public String deleteLevelUp(int id) {
        String successful = "Deletion successful. LevelUp with levelUpId '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No levelUp with levelUpId '" + id + "' was found.";

        int rowsDeleted = levelUpJdbc.update(DELETE_LEVEL_UP, id);

        return rowsDeleted == 1 ? successful : unsuccessful;

    }

    @Override
    public LevelUp getLevelUpByCustomerId(int id) {
        try {
            return levelUpJdbc.queryForObject(GET_LEVEL_UP_BY_CUSTOMER_ID, this::mapRowToLevelUp, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
