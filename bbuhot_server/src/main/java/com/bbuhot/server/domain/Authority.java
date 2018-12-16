package com.bbuhot.server.domain;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.service.AuthReply;
import com.bbuhot.server.service.AuthRequest;
import java.util.Optional;
import javax.inject.Inject;

public class Authority {

  private final UserQueries userQueries;

  @Inject
  Authority(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  public AuthReply auth(AuthRequest authRequest) {
    Optional<UserEntity> optionalUser = userQueries.queryUserById(authRequest.getUid());
    if (optionalUser.isEmpty()) {
      return AuthReply.newBuilder().setErrorCode(AuthReply.ErrorCode.NO_SUCH_USER).build();
    }

    UserEntity userEntity = optionalUser.get();
    if (!AuthorityUtil.isValid(
        Flags.getInstance().getDiscuzConfig().getAuthkey(),
        authRequest.getSaltKey(),
        authRequest.getAuth(),
        userEntity.getUid(),
        userEntity.getPassword())) {
      return AuthReply.newBuilder().setErrorCode(AuthReply.ErrorCode.KEY_NOT_MATCHING).build();
    }

    return AuthReply.newBuilder()
        .setErrorCode(AuthReply.ErrorCode.NO_ERROR)
        .setUser(
            AuthReply.User.newBuilder()
                .setUid(userEntity.getUid())
                .setName(userEntity.getUsername()))
        .build();
  }
}
