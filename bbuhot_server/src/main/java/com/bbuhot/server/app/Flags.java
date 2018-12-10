package com.bbuhot.server.app;

public class Flags {

  private static Flags flags = new Flags();

  private final Database database = new Database();
  private final DiscuzConfig discuzConfig = new DiscuzConfig();
  private final int port = 8080;
  private final boolean isDebug = true;

  private Flags() {}

  public static Flags getInstance() {
    return flags;
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
    private final String ucKey = "n42ck829Ebr0FbR2z5P8K973Dcg5j505ed1cSbr3La19ZdD0Wbnfn1W7w6yaj338";

    public String getAuthkey() {
      return authkey;
    }

    public String getUcKey() {
      return ucKey;
    }
  }
}
