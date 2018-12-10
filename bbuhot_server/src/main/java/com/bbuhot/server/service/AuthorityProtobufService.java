package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import javax.inject.Inject;

class AuthorityProtobufService extends AbstractProtobufService<AuthRequest, AuthReply> {

  private final Authority authority;

  @Inject
  AuthorityProtobufService(Authority authority) {
    this.authority = authority;
  }

  @Override
  AuthRequest getInputMessageDefaultInstance() {
    return AuthRequest.getDefaultInstance();
  }

  @Override
  AuthReply callProtobufServiceImpl(AuthRequest authRequest) {
    return authority.auth(authRequest);
  }
}
