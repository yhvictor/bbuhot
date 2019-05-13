package com.bbuhot.server.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "bbuhot_game")
@Entity
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

  @Column(name = "bet_option_limit")
  private int betOptionLimit = 1;

  @Column(name = "bet_amount_lowest")
  private int betAmountLowest = 0;

  @Column(name = "bet_amount_highest")
  private int betAmountHighest = 0;

  @Column(name = "end_time_ms")
  private Timestamp endTimeMs;

  @Column(name = "winning_bet_option")
  private int winningBetOption = -2;

  // Use FetchType.EAGER to bypass a bug: https://hibernate.atlassian.net/browse/HHH-4808
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id", referencedColumnName = "id")
  @OrderBy("id")
  private List<BettingOptionEntity> bettingOptionEntities = new ArrayList<>();

    public GameEntity() {
    }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public int getBetOptionLimit() {
    return betOptionLimit;
  }

  public void setBetOptionLimit(int betOptionLimit) {
    this.betOptionLimit = betOptionLimit;
  }

  public int getBetAmountLowest() {
    return betAmountLowest;
  }

  public void setBetAmountLowest(int betAmountLowest) {
    this.betAmountLowest = betAmountLowest;
  }

  public int getBetAmountHighest() {
    return betAmountHighest;
  }

  public void setBetAmountHighest(int betAmountHighest) {
    this.betAmountHighest = betAmountHighest;
  }

  public Timestamp getEndTimeMs() {
    return endTimeMs;
  }

  public void setEndTimeMs(Timestamp endTimeMs) {
    this.endTimeMs = endTimeMs;
  }

  public List<BettingOptionEntity> getBettingOptionEntities() {
    return bettingOptionEntities;
  }

  public int getWinningBetOption() {
    return winningBetOption;
  }

  public void setWinningBetOption(int winningBetOption) {
    this.winningBetOption = winningBetOption;
  }

  @Table(name = "bbuhot_betting_options")
  @Entity
  public static class BettingOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private int id;

    @Column(nullable = false)
    private String name = "";

    @Column(nullable = false)
    private int odds = 1000000;

      public BettingOptionEntity() {
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
