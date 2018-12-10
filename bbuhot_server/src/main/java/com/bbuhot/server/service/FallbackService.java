package com.bbuhot.server.service;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import javax.inject.Inject;

public class FallbackService implements HttpHandler {

  @Inject
  FallbackService() {}

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    exchange.setStatusCode(404).getResponseSender().send("404 not found.");
  }
}
