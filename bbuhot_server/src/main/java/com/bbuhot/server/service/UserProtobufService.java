package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Deque;
import java.util.Map;
import javax.inject.Inject;

public class UserProtobufService extends ProtobufService<UserDto> {

  private final Authority authority;

  @Inject
  UserProtobufService(Authority authority) {
    this.authority = authority;
  }

  @Override
  protected ListenableFuture<UserDto> generateResponseMessage(
      Map<String, Deque<String>> urlParams) {
    return MoreExecutors.newDirectExecutorService()
        .submit(
            () -> {
              int uid = getParam(urlParams, "uid");
              return authority.testWorkflow(uid);
            });
  }

  private int getParam(Map<String, Deque<String>> urlParams, String param) {
    Deque<String> deque = urlParams.get(param);
    if (deque == null) {
      throw new IllegalStateException("Param not in url param map: " + param);
    }
    return Integer.parseInt(deque.getFirst());
  }
}
