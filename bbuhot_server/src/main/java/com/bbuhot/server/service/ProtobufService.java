package com.bbuhot.server.service;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class ProtobufService<T extends Message> implements HttpHandler {

  private static final String CONTENT_TYPE_PROTOBUF = "application/protobuf";
  private static final String CONTENT_TYPE_JSON = "application/json";

  protected abstract ListenableFuture<T> generateResponseMessage(
      Map<String, Deque<String>> urlParams);

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    // TODO(yh_victor): consider whether we should limit this to debug only.
    boolean useJson = exchange.getQueryParameters().containsKey("json");

    FluentFuture.from(generateResponseMessage(exchange.getQueryParameters()))
        .transform(
            message -> {
              assert message != null;
              if (useJson) {
                try {
                  return JsonFormat.printer().print(message).getBytes(StandardCharsets.UTF_8);
                } catch (InvalidProtocolBufferException e) {
                  throw new IllegalStateException(e);
                }
              } else {
                return message.toByteArray();
              }
            },
            MoreExecutors.directExecutor())
        .addCallback(
            new FutureCallback<byte[]>() {
              @Override
              public void onSuccess(@Nullable byte[] bytes) {
                assert bytes != null;
                exchange
                    .getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, useJson ? CONTENT_TYPE_JSON : CONTENT_TYPE_PROTOBUF);
                exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
              }

              @Override
              public void onFailure(Throwable t) {
                exchange.setStatusCode(400);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                t.printStackTrace(new PrintStream(byteArrayOutputStream));
                exchange
                    .getResponseSender()
                    .send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
              }
            },
            MoreExecutors.directExecutor());
  }
}
