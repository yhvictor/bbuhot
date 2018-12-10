package com.bbuhot.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@EntityMapping(tableName = "common_member", hasPrefix = true)
@Entity(name = "User")
public class User {

  @Id
  @GeneratedValue
  @Column(unique = true, nullable = false)
  private int uid;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  User() {}

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
