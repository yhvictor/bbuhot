package com.bbuhot.server.domain;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.service.AuthReply;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.AuthRequest;
import javax.inject.Inject;

public class Authority {

  private final UserQueries userQueries;

  @Inject
  Authority(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  public AuthReply auth(AuthRequest authRequest, boolean checkIsAdmin) {
    UserEntity userEntity = userQueries.queryUserById(authRequest.getUid()).orElseGet(() -> {
      throw new IllegalStateException("No user with such uid: " + authRequest.getUid());
    });

    if (!AuthorityUtil.isValid(
        Flags.getInstance().getDiscuzConfig().getAuthkey(),
        authRequest.getSaltKey(),
        authRequest.getAuth(),
        userEntity.getUid(),
        userEntity.getPassword())) {
      return AuthReply.newBuilder().setErrorCode(AuthReply.AuthErrorCode.KEY_NOT_MATCHING).build();
    }

    if (checkIsAdmin && !isAdminGroup(userEntity)) {
      return AuthReply.newBuilder().setErrorCode(AuthErrorCode.PERMISSION_DENY).build();
    }

    return AuthReply.newBuilder()
        .setErrorCode(AuthReply.AuthErrorCode.NO_ERROR)
        .setUser(
            AuthReply.User.newBuilder()
                .setUid(userEntity.getUid())
                .setName(userEntity.getUsername()))
        .build();
  }

  /**
   * Very simplify admin group check. We might need to support more in future.
   */
  private boolean isAdminGroup(UserEntity userEntity) {
    return Flags.getInstance().getAdminGroups().contains(userEntity.getGroupId());
  }
}
