package com.bbuhot.server.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "bbuhot_bets")
@Entity
public class BetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, updatable = false)
  private int id;

  @Column(name = "user_id", nullable = false, updatable = false)
  private int uid = 0;

  @Column(name = "game_id", nullable = false, updatable = false)
  private int gameId = 0;

  @Column(name = "betting_option_id", nullable = false)
  private int bettingOptionId = 0;

  @Column(name = "bet_amount", nullable = false)
  private int betAmount = 0;

  @Column(name = "status", nullable = false)
  private int status = 0;

  @Column(name = "earning", nullable = false)
  private int earning = 0;

  @Column(name = "created_at")
  private Timestamp createdAt;

    public BetEntity() {
    }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public int getBettingOptionId() {
    return bettingOptionId;
  }

  public void setBettingOptionId(int bettingOptionId) {
    this.bettingOptionId = bettingOptionId;
  }

  public int getBetAmount() {
    return betAmount;
  }

  public void setBetAmount(int betAmount) {
    this.betAmount = betAmount;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getEarning() {
    return earning;
  }

  public void setEarning(int earning) {
    this.earning = earning;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }
}
