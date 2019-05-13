package com.bbuhot.server.service;

import com.bbuhot.server.config.Flags;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.service.AuthReply.User;

import javax.inject.Inject;
import java.util.Optional;

/**
 * A test server for test using.
 */
class UserProtobufService extends AbstractProtobufService<AuthReply.User, AuthReply.User> {

  private final UserQueries userQueries;

  @Inject
  UserProtobufService(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  @Override
  User getInputMessage(HttpServerExchangeMessageWrapper exchange, byte[] bytes) {
    User.Builder builder = User.newBuilder();
    exchange.mergeFieldsFromBody(builder, bytes);
    return builder.build();
  }

  @Override
  AuthReply.User callProtobufServiceImpl(AuthReply.User userDto) {
    if (!Flags.isDebug()) {
      throw new IllegalStateException("Debug only service.");
    }
    Optional<UserEntity> optionalUser = userQueries.queryUserById(userDto.getUid());
    if (!optionalUser.isPresent()) {
      throw new IllegalStateException("No such user.");
    }
    UserEntity userEntity = optionalUser.get();
    return AuthReply.User.newBuilder()
        .setUid(userEntity.getUid())
        .setName(userEntity.getUsername())
        .build();
  }
}
