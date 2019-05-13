package com.bbuhot.server.util;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TestMessageUtil {

    private String prefix;

    public TestMessageUtil(String prefix) {
        this.prefix = prefix;
    }

  @SuppressWarnings("unchecked")
  public <T extends Message> T getResourcesAsMessage(T instance, String resourceName) {
    Message.Builder builder = instance.toBuilder();
    try {
        InputStream inputStream =
                TestMessageUtil.class.getClassLoader().getResourceAsStream(prefix + resourceName);
        if (inputStream == null) {
            throw new NullPointerException();
        }
      JsonFormat.parser()
              .merge(new InputStreamReader(inputStream, StandardCharsets.UTF_8), builder);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return (T) builder.build();
  }
}
