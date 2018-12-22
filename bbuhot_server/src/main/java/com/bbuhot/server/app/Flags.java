package com.bbuhot.server.app;

import com.bbuhot.server.config.Configuration;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.util.JsonFormat;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Flags {

  private Flags() {}

  private static Configuration configuration;
  private static Set<Integer> adminGroups;

  static void initialize(String[] args) {
    // TODO(yhvictor): better flag handling.
    final String configurationFile;
    if (args.length == 0) {
      configurationFile = "configuration.json";
    } else {
      configurationFile = args[0];
    }

    Configuration.Builder configurationBuilder = Configuration.newBuilder();
    try {
      JsonFormat.parser().merge(new FileReader(new File(configurationFile), StandardCharsets.UTF_8),
          configurationBuilder);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    initializeWithConfiguration(configurationBuilder.build());
  }

  public static void initializeWithConfiguration(Configuration configuration) {
    Flags.configuration = configuration;
    adminGroups = ImmutableSet.copyOf(configuration.getDiscuzConfig().getAdminGroupList());
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
}
