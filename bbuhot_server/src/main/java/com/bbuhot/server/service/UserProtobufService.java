package com.bbuhot.server.service;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.User;
import com.bbuhot.server.persistence.UserQueries;
import java.util.Optional;
import javax.inject.Inject;

/** A test server for test using. */
class UserProtobufService extends AbstractProtobufService<UserReply, UserReply> {

  private final UserQueries userQueries;

  @Inject
  UserProtobufService(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  @Override
  UserReply getInputMessageDefaultInstance() {
    return UserReply.getDefaultInstance();
  }

  @Override
  UserReply callProtobufServiceImpl(UserReply userDto) {
    if (!Flags.getInstance().isDebug()) {
      throw new IllegalStateException("Debug only service.");
    }
    Optional<User> optionalUser = userQueries.queryUserById(userDto.getUid());
    if (optionalUser.isEmpty()) {
      throw new IllegalStateException("No such user.");
    }
    User user = optionalUser.get();
    return UserReply.newBuilder().setUid(user.getUid()).setName(user.getUsername()).build();
  }
}
