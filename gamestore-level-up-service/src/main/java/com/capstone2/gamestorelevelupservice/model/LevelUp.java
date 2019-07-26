package com.capstone2.gamestorelevelupservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUp {

    private int levelUpId;

    @Positive(message = "'customerId' must be present in this levelUp")
    private int customerId;

    @Positive(message = "'points' must be present in this levelUp")
    private int points;

    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "'memberDate' must be present in this levelUp and have the format yyyy-mm-dd")
    private LocalDate memberDate;

    public LevelUp() {
    }

    public LevelUp(int customerId, int points, LocalDate memberDate) {
        this.customerId = customerId;
        this.points = points;
        this.memberDate = memberDate;
    }

    public int getLevelUpId() {
        return levelUpId;
    }

    public void setLevelUpId(int levelUpId) {
        this.levelUpId = levelUpId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDate getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(LocalDate memberDate) {
        this.memberDate = memberDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelUp levelUp = (LevelUp) o;
        return levelUpId == levelUp.levelUpId &&
                customerId == levelUp.customerId &&
                points == levelUp.points &&
                Objects.equals(memberDate, levelUp.memberDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelUpId, customerId, points, memberDate);
    }

    @Override
    public String toString() {
        return "LevelUp{" +
                "levelUpId=" + levelUpId +
                ", customerId=" + customerId +
                ", points=" + points +
                ", memberDate=" + memberDate +
                '}';
    }
}
