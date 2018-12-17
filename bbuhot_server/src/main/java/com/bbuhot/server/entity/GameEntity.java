package com.bbuhot.server.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "bbuhot_game")
@Entity()
public class GameEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, updatable = false)
  private int id;

  @Column(nullable = false)
  private String name = "";

  @Column(nullable = false)
  private String description = "";

  @Column(name = "normal_user_visible")
  private boolean normalUserVisible = false;

  @Column(nullable = false)
  private int status = 0;

  @Column(name = "maximum_bet_options")
  private int maximumBetOptions = 1;

  @Column(name = "bet_money_lowest")
  private int betMoneyLowest = 0;

  @Column(name = "bet_money_highest")
  private int betMoneyLeast = 0;

  @Column(name = "end_time_ms")
  private Timestamp endTimeMs;

  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "game_id", referencedColumnName = "id")
  private List<BetEntity> betEntities;

  public GameEntity() {
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isNormalUserVisible() {
    return normalUserVisible;
  }

  public void setNormalUserVisible(boolean normalUserVisible) {
    this.normalUserVisible = normalUserVisible;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getMaximumBetOptions() {
    return maximumBetOptions;
  }

  public void setMaximumBetOptions(int maximumBetOptions) {
    this.maximumBetOptions = maximumBetOptions;
  }

  public int getBetMoneyLowest() {
    return betMoneyLowest;
  }

  public void setBetMoneyLowest(int betMoneyLowest) {
    this.betMoneyLowest = betMoneyLowest;
  }

  public int getBetMoneyLeast() {
    return betMoneyLeast;
  }

  public void setBetMoneyLeast(int betMoneyLeast) {
    this.betMoneyLeast = betMoneyLeast;
  }

  public Timestamp getEndTimeMs() {
    return endTimeMs;
  }

  public void setEndTimeMs(Timestamp endTimeMs) {
    this.endTimeMs = endTimeMs;
  }

  public List<BetEntity> getBetEntities() {
    if (betEntities == null) {
      betEntities = new ArrayList<>();
    }
    return betEntities;
  }

  public void setBetEntities(List<BetEntity> betEntities) {
    this.betEntities = betEntities;
  }

  @Table(name = "bbuhot_betting_options")
  @Entity()
  public static class BetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private int id;

    private String name = "";

    private int odds = 1000000;

    public BetEntity() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getOdds() {
      return odds;
    }

    public void setOdds(int odds) {
      this.odds = odds;
    }
  }
}
