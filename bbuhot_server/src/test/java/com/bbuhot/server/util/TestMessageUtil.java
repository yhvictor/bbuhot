package com.bbuhot.server.util;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TestMessageUtil {

  private TestMessageUtil() {}

  @SuppressWarnings("unchecked")
  public static <T extends Message> T getResourcesAsMessage(T instance, String resourceName) {
    Message.Builder builder = instance.toBuilder();
    try {
      JsonFormat.parser().merge(new InputStreamReader(
          TestMessageUtil.class.getClassLoader().getResourceAsStream(resourceName),
          StandardCharsets.UTF_8), builder);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return (T) builder.build();
  }
}
