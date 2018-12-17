package com.bbuhot.server.service;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.UserQueries;
import java.util.Optional;
import javax.inject.Inject;

/** A test server for test using. */
class UserProtobufService extends AbstractProtobufService<AuthReply.User, AuthReply.User> {

  private final UserQueries userQueries;

  @Inject
  UserProtobufService(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  @Override
  AuthReply.User getInputMessageDefaultInstance() {
    return AuthReply.User.getDefaultInstance();
  }

  @Override
  AuthReply.User callProtobufServiceImpl(AuthReply.User userDto) {
    if (!Flags.getInstance().isDebug()) {
      throw new IllegalStateException("Debug only service.");
    }
    Optional<UserEntity> optionalUser = userQueries.queryUserById(userDto.getUid());
    if (optionalUser.isEmpty()) {
      throw new IllegalStateException("No such user.");
    }
    UserEntity userEntity = optionalUser.get();
    return AuthReply.User.newBuilder()
        .setUid(userEntity.getUid())
        .setName(userEntity.getUsername())
        .build();
  }
}
