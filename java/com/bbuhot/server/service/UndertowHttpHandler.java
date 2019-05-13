package com.bbuhot.server.service;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;
import java.util.Map;

public class UndertowHttpHandler implements HttpHandler {

  private final HttpHandler defaultService;
  private final Map<String, HttpHandler> serviceMap;

  @Inject
  UndertowHttpHandler(FallbackService fallbackService, Map<String, HttpHandler> serviceMap) {
    this.defaultService = fallbackService;
    this.serviceMap = serviceMap;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    serviceMap.getOrDefault(exchange.getRequestPath(), defaultService).handleRequest(exchange);
  }
}
