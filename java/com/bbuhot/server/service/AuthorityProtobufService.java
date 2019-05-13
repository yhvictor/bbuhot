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
  AuthRequest getInputMessage(HttpServerExchangeMessageWrapper exchange, byte[] bytes) {
    AuthRequest authRequest = exchange.generateAuthRequestFromCookie();
    if (authRequest != null) {
      return authRequest;
    }
    AuthRequest.Builder builder = AuthRequest.newBuilder();
    exchange.mergeFieldsFromBody(builder, bytes);
    return builder.build();
  }

  @Override
  AuthReply callProtobufServiceImpl(AuthRequest authRequest) {
    return authority.auth(authRequest, /* checkIsAdmin= */ false);
  }
}
