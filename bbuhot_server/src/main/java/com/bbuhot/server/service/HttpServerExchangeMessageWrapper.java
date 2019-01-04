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
import com.google.protobuf.TextFormat;
import com.google.protobuf.util.JsonFormat;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Map;
import javax.annotation.Nullable;

class HttpServerExchangeMessageWrapper {

  private final HttpServerExchange httpServerExchange;

  HttpServerExchangeMessageWrapper(HttpServerExchange httpServerExchange) {
    this.httpServerExchange = httpServerExchange;
  }

  <T extends Message> ListenableFuture<T> parseRequest(Message.Builder builder) {
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
        requestBodyFuture,
        request -> {
          assert request != null;
          @SuppressWarnings("unchecked")
          T message = (T) this.parseRequestBody(builder, request);
          return message;
        },
        MoreExecutors.directExecutor());
  }

  private Message parseRequestBody(Message.Builder builder, byte[] bytes) {
    try {
      switch (getInputContentType()) {
        case JSON:
          JsonFormat.parser()
              .merge(
                  new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8),
                  builder);
          break;
        case TEXT_PROTO:
          TextFormat.getParser()
              .merge(
                  new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8),
                  builder);
          break;
        case PROTO:
          builder.mergeFrom(bytes);
      }
    } catch (IOException exception) {
      throw new IllegalStateException(exception);
    }

    return builder.build();
  }

  void writeOutputMessage(ListenableFuture<? extends Message> outputMessageFuture, long startTime) {
    Futures.addCallback(
        outputMessageFuture,
        new FutureCallback<Message>() {
          @Override
          public void onSuccess(@Nullable Message result) {
            assert result != null;
            writeOutputMessageInternal(result);

            if (Flags.isDebug()) {
              long endTime = System.nanoTime();
              System.out.println("Round time consume: " + (endTime - startTime));
            }
          }

          @Override
          public void onFailure(Throwable t) {
            writeErrorMessage(t);

            if (Flags.isDebug()) {
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
    if (Flags.getInstance().getAllowCors()) {
      httpServerExchange
          .getResponseHeaders()
          .put(new HttpString("Access-Control-Allow-Credentials"), Boolean.toString(true));
      httpServerExchange
          .getResponseHeaders()
          .put(new HttpString("Access-Control-Allow-Origin"), getOriginParam());
    }

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
    if (Flags.getInstance().getAllowCors()) {
      httpServerExchange
          .getResponseHeaders()
          .put(new HttpString("Access-Control-Allow-Credentials"), Boolean.toString(true));
      httpServerExchange
          .getResponseHeaders()
          .put(new HttpString("Access-Control-Allow-Origin"), getOriginParam());
    }
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    t.printStackTrace(new PrintStream(byteArrayOutputStream));
    httpServerExchange
        .getResponseSender()
        .send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
  }

  void modifyAuthRequestBuilder(AuthRequest.Builder builder) {
    String auth = getCookie(Flags.getAuthCookieName());
    String saltKey = getCookie(Flags.getSaltKeyCookieName());

    if (auth != null) {
      builder.setAuth(auth);
    }
    if (saltKey != null) {
      builder.setSaltKey(saltKey);
    }
  }

  private String getCookie(String key) {
    Cookie cookie = httpServerExchange.getRequestCookies().get(key);

    try {
      return cookie == null ? null : URLDecoder.decode(cookie.getValue(), "utf8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

  private String getOriginParam() {
    Map<String, Deque<String>> urlParams = httpServerExchange.getQueryParameters();
    if (!urlParams.containsKey("origin")) {
      return null;
    }

    return urlParams.get("origin").getFirst();
  }

  private ContentType getInputContentType() {
    Map<String, Deque<String>> urlParams = httpServerExchange.getQueryParameters();
    // TODO(yh_victor): consider whether we should limit this to debug only.
    if (urlParams.containsKey("in")) {
      for (String value : urlParams.get("in")) {
        if ("json".equals(value)) {
          return ContentType.JSON;
        } else if ("textproto".equals(value)) {
          return ContentType.TEXT_PROTO;
        }
      }
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
