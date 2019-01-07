package com.bbuhot.server.service;

import com.bbuhot.server.util.BbuhotThreadPool;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.Message;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

abstract class AbstractProtobufService<InputMessage extends Message, OutputMessage extends Message>
    implements HttpHandler {

  abstract InputMessage getInputMessage(HttpServerExchangeMessageWrapper exchange, byte[] bytes);

  abstract OutputMessage callProtobufServiceImpl(InputMessage inputMessage);

  @Override
  public void handleRequest(HttpServerExchange httpServerExchange) {
    long start = System.nanoTime();
    HttpServerExchangeMessageWrapper exchange =
        new HttpServerExchangeMessageWrapper(httpServerExchange);
    ListenableFuture<InputMessage> inputMessage = Futures.transform(
        exchange.getRequestBody(),
        (byte[] bytes) -> {
          assert bytes != null;
          return getInputMessage(exchange, bytes);
        },
        MoreExecutors.directExecutor()
    );
    ListenableFuture<OutputMessage> output =
        Futures.transform(
            inputMessage, this::callProtobufServiceImpl, BbuhotThreadPool.limitedThreadPool);

    exchange.writeOutputMessage(output, start);
  }
}
