package com.bbuhot.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "{pre}_common_member")
@Entity()
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, updatable = false, nullable = false)
  private int uid;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  UserEntity() {
  }

  public int getUid() {
    return uid;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
