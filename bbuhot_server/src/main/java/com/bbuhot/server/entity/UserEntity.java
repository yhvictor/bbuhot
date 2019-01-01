package com.bbuhot.server.entity;

import com.bbuhot.errorprone.TestOnly;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// TODO(yh_victor): we should not need to support custom prefix.
@Table(name = "pre_common_member")
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

  @Column(nullable = false)
  private int groupId;

  //TODO(luciusgone): we can't be sure which extcredit we are using.
  //                  by default it is `extcredits2` from `pre_common_member_count`
  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  @JoinColumn(name = "uid", referencedColumnName = "uid")
  private ExtcreditsEntity extcreditsEntity;

  public UserEntity() {}

  public int getUid() {
    return uid;
  }

  @TestOnly
  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getUsername() {
    return username;
  }

  @TestOnly
  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  @TestOnly
  public void setPassword(String password) {
    this.password = password;
  }

  public int getGroupId() {
    return groupId;
  }

  public ExtcreditsEntity getExtcreditsEntity() {
    return extcreditsEntity;
  }

  @TestOnly
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  @Table(name = "pre_common_member_count")
  @Entity
  public static class ExtcreditsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private int uid;

    // Use this credits instead of the credits from the `pre_common_member`
    // table for now.
    @Column(nullable = false)
    private int extcredits2;

    public ExtcreditsEntity() {}

    public int getExtcredits2() {
      return extcredits2;
    }

    public void setExtcredits2(int extcredits2) {
      this.extcredits2 = extcredits2;
    }
  }
}
