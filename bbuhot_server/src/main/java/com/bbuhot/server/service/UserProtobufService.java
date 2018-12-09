package com.bbuhot.server.service;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.User;
import com.bbuhot.server.persistence.UserQueries;
import java.util.Deque;
import java.util.Map;
import javax.inject.Inject;

/**
 * A test server for test using.
 */
class UserProtobufService extends ProtobufService<UserDto, UserDto> {

  private final UserQueries userQueries;

  @Inject
  UserProtobufService(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  @Override
  UserDto parseUrlParams(Map<String, Deque<String>> urlParams) {
    return UserDto.newBuilder().setUid(getIntParam(urlParams, "uid")).build();
  }

  @Override
  UserDto generateResponseMessage(UserDto userDto) {
    if (!Flags.getInstance().isDebug()) {
      throw new IllegalStateException("Debug only service.");
    }
    User user = userQueries.queryUserById(userDto.getUid());
    return UserDto.newBuilder().setUid(user.getUid()).setName(user.getUsername()).build();
  }
}
