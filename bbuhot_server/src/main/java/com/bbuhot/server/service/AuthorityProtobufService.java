package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import java.util.Deque;
import java.util.Map;
import javax.inject.Inject;

class AuthorityProtobufService extends ProtobufService<AuthRequest, AuthDto> {

  private final Authority authority;

  @Inject
  AuthorityProtobufService(Authority authority) {
    this.authority = authority;
  }

  @Override
  AuthRequest parseUrlParams(Map<String, Deque<String>> urlParams) {
    return AuthRequest.newBuilder()
        .setAuth(getStringParam(urlParams, "auth"))
        .setSaltKey(getStringParam(urlParams, "saltKey"))
        .setUid(getIntParam(urlParams, "uid"))
        .build();
  }

  @Override
  AuthDto generateResponseMessage(AuthRequest authRequest) {
    return authority.auth(authRequest);
  }
}
