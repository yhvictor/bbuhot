package com.bbuhot.server.config;

import com.bbuhot.errorprone.TestOnly;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.util.JsonFormat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;

public class Flags {

    private Flags() {
    }

  private static Configuration configuration;
  private static Set<Integer> adminGroups;
  private static String authCookieName;
  private static String saltKeyCookieName;

    public static void initialize(String[] args) {
    // TODO(yhvictor): better flag handling.
    final String configurationFile;
    if (args.length == 0) {
      configurationFile = "configuration.json";
    } else {
      configurationFile = args[0];
    }

    Configuration.Builder configurationBuilder = Configuration.newBuilder();
    try {
      JsonFormat.parser()
          .merge(
              Files.newBufferedReader(new File(configurationFile).toPath(), StandardCharsets.UTF_8),
              configurationBuilder);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    initializeWithConfiguration(configurationBuilder.build());
  }

  @TestOnly
  public static void initializeWithConfiguration(Configuration configuration) {
    Flags.configuration = configuration;
    adminGroups = ImmutableSet.copyOf(configuration.getDiscuzConfig().getAdminGroupList());
    authCookieName = configuration.getDiscuzConfig().getCookiePre() + "auth";
    saltKeyCookieName = configuration.getDiscuzConfig().getCookiePre() + "saltkey";
  }

  public static Configuration getInstance() {
    return configuration;
  }

  public static boolean isDebug() {
    return configuration.getIsDebug();
  }

  public static Set<Integer> getAdminGroups() {
    return adminGroups;
  }

  public static String getAuthCookieName() {
    return authCookieName;
  }

  public static String getSaltKeyCookieName() {
    return saltKeyCookieName;
  }
}
