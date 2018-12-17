package com.bbuhot.server.service;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.util.BbuhotThreadPool;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.TextFormat;
import com.google.protobuf.util.JsonFormat;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;
import javax.annotation.Nullable;

class HttpServerExchangeMessageWrapper<InputMessage extends Message> {

  private final InputMessage defaultMessage;
  private final HttpServerExchange httpServerExchange;

  HttpServerExchangeMessageWrapper(
      InputMessage defaultMessage, HttpServerExchange httpServerExchange) {
    this.defaultMessage = defaultMessage;
    this.httpServerExchange = httpServerExchange;
  }

  ListenableFuture<InputMessage> parseRequest() {
    SettableFuture<byte[]> requestBodyFuture = SettableFuture.create();
    httpServerExchange
        .getRequestReceiver()
        .receiveFullBytes(
            (exchange, message) ->
                exchange.dispatch(
                    BbuhotThreadPool.workerThreadPool, () -> requestBodyFuture.set(message)),
            (exchange, e) ->
                exchange.dispatch(
                    BbuhotThreadPool.workerThreadPool, () -> requestBodyFuture.setException(e)));

    return Futures.transform(
        requestBodyFuture, this::parseRequestBody, MoreExecutors.directExecutor());
  }

  private InputMessage parseRequestBody(byte[] bytes) {
    Builder builder = defaultMessage.toBuilder();
    try {
      switch (getInputContentType()) {
        case JSON:
          JsonFormat.parser()
              .merge(new InputStreamReader(new ByteArrayInputStream(bytes)), builder);
          break;
        case TEXT_PROTO:
          TextFormat.getParser()
              .merge(new InputStreamReader(new ByteArrayInputStream(bytes)), builder);
        case PROTO:
          builder.mergeFrom(bytes);
      }
    } catch (IOException exception) {
      throw new IllegalStateException(exception);
    }

    @SuppressWarnings("unchecked")
    InputMessage message = (InputMessage) builder.build();

    return message;
  }

  void writeOutputMessage(ListenableFuture<? extends Message> outputMessageFuture, long startTime) {
    Futures.addCallback(
        outputMessageFuture,
        new FutureCallback<Message>() {
          @Override
          public void onSuccess(@Nullable Message result) {
            assert result != null;
            writeOutputMessageInternal(result);

            if (Flags.getInstance().isDebug()) {
              long endTime = System.nanoTime();
              System.out.println("Round time consume: " + (endTime - startTime));
            }
          }

          @Override
          public void onFailure(Throwable t) {
            writeErrorMessage(t);

            if (Flags.getInstance().isDebug()) {
              long endTime = System.nanoTime();
              System.out.println("Round time consume: " + (endTime - startTime));
            }
          }
        },
        BbuhotThreadPool.workerThreadPool);
  }

  private void writeOutputMessageInternal(Message outputMessage) {
    final byte[] bytes;
    ContentType outputContentType = getOutputContentType();
    httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, outputContentType.typeString);

    switch (outputContentType) {
      case PROTO:
        bytes = outputMessage.toByteArray();
        break;
      case JSON:
        try {
          bytes = JsonFormat.printer().print(outputMessage).getBytes(StandardCharsets.UTF_8);
        } catch (InvalidProtocolBufferException e) {
          throw new IllegalStateException(e);
        }
        break;
      case TEXT_PROTO:
        bytes = TextFormat.printToString(outputMessage).getBytes(StandardCharsets.UTF_8);
        break;
      default:
        throw new IllegalStateException("Not reachable code.");
    }

    httpServerExchange.getResponseSender().send(ByteBuffer.wrap(bytes));
  }

  private void writeErrorMessage(Throwable t) {
    httpServerExchange.setStatusCode(400);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    t.printStackTrace(new PrintStream(byteArrayOutputStream));
    httpServerExchange
        .getResponseSender()
        .send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
  }

  private ContentType getInputContentType() {
    HeaderValues headerValues = httpServerExchange.getRequestHeaders().get(Headers.CONTENT_TYPE);
    if (headerValues == null) {
      return ContentType.PROTO;
    }
    String contentTypeString = headerValues.peek();
    if (contentTypeString == null) {
      return ContentType.PROTO;
    }
    if (ContentType.TEXT_PROTO.typeString.equalsIgnoreCase(contentTypeString)) {
      return ContentType.TEXT_PROTO;
    }
    if (ContentType.JSON.typeString.equalsIgnoreCase(contentTypeString)) {
      return ContentType.JSON;
    }
    return ContentType.PROTO;
  }

  private ContentType getOutputContentType() {
    Map<String, Deque<String>> urlParams = httpServerExchange.getQueryParameters();
    // TODO(yh_victor): consider whether we should limit this to debug only.
    if (urlParams.containsKey("out")) {
      for (String value : urlParams.get("out")) {
        if ("json".equals(value)) {
          return ContentType.JSON;
        } else if ("textproto".equals(value)) {
          return ContentType.TEXT_PROTO;
        }
      }
    }
    return ContentType.PROTO;
  }

  enum ContentType {
    PROTO("application/protobuf"),
    JSON("application/json"),
    TEXT_PROTO("text/plain"),
    ;

    private final String typeString;

    ContentType(String typeString) {
      this.typeString = typeString;
    }
  }
}
