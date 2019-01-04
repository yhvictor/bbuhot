package com.bbuhot.server.service;

import com.bbuhot.server.util.BbuhotThreadPool;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Message;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

abstract class AbstractProtobufService<InputMessage extends Message, OutputMessage extends Message>
    implements HttpHandler {

  abstract Message.Builder getInputMessageBuilder(HttpServerExchangeMessageWrapper exchange);

  abstract OutputMessage callProtobufServiceImpl(InputMessage inputMessage);

  @Override
  public void handleRequest(HttpServerExchange httpServerExchange) {
    long start = System.nanoTime();
    HttpServerExchangeMessageWrapper exchange =
        new HttpServerExchangeMessageWrapper(httpServerExchange);
    ListenableFuture<InputMessage> inputMessage =
        exchange.parseRequest(getInputMessageBuilder(exchange));
    ListenableFuture<OutputMessage> output =
        Futures.transform(
            inputMessage, this::callProtobufServiceImpl, BbuhotThreadPool.limitedThreadPool);

    exchange.writeOutputMessage(output, start);
  }
}
