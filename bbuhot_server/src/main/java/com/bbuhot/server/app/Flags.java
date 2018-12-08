package com.bbuhot.server.app;

import javax.inject.Inject;

public class Flags {

  private final Database database = new Database();
  private final DiscuzConfig discuzConfig = new DiscuzConfig();
  private final int port = 8080;
  private final boolean isDebug = true;

  @Inject
  Flags() {
  }

  public Database getDatabase() {
    return database;
  }

  public DiscuzConfig getDiscuzConfig() {
    return discuzConfig;
  }

  public int getPort() {
    return port;
  }

  public boolean isDebug() {
    return isDebug;
  }

  public static class Database {

    private final String url = "jdbc:mysql://192.168.31.23:3306/ultrax";
    private final String user = "root";
    private final String password = "bbuhot";
    private final String tablePrefix = "pre_";

    public String getUrl() {
      return url;
    }

    public String getUser() {
      return user;
    }

    public String getPassword() {
      return password;
    }

    public String getTablePrefix() {
      return tablePrefix;
    }
  }

  public static class DiscuzConfig {

    private final String authkey = "76fce85ae9cf5ceeee99c014615ee215qfDf49F0YXdxHuEkru";

    public String getAuthkey() {
      return authkey;
    }
  }
}
