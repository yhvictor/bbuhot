package com.bbuhot.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


// TODO(luciusgone): The default credits used for trading is `extcredits2`
//                   from`pre_common_member_count`. Find a good way to handle
//                   this.
@Table(name = "pre_common_member_count")
@Entity
public class ExtcreditsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, updatable = false, nullable = false)
  private int uid;

  @Column(nullable = false)
  private int extcredits2;

  public ExtcreditsEntity() {}

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public int getExtcredits2() {
    return extcredits2;
  }

  public void setExtcredits2(int extcredits2) {
    this.extcredits2 = extcredits2;
  }
}
