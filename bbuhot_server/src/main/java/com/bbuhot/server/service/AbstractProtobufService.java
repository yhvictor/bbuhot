package com.bbuhot.server.service;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
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

abstract class AbstractProtobufService<InputMessage extends Message, OutputMessage extends Message>
    implements HttpHandler {

  abstract InputMessage parseUrlParams(Map<String, Deque<String>> urlParams);

  abstract OutputMessage generateResponseMessage(InputMessage inputMessage);

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    // TODO(yh_victor): consider thread limit control.
    handleRequestInternal(exchange);
  }

  private void handleRequestInternal(HttpServerExchange exchange) {
    try {
      handleRequestWithoutException(exchange);
    } catch (Throwable t) {
      exchange.setStatusCode(400);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      t.printStackTrace(new PrintStream(byteArrayOutputStream));
      exchange
          .getResponseSender()
          .send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
    }
  }

  private void handleRequestWithoutException(HttpServerExchange exchange) {
    ContentType contentType = ContentType.getContentType(exchange.getQueryParameters());

    byte[] bytes = generateResponse(exchange.getQueryParameters(), contentType);
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType.typeString);
    exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
  }

  private byte[] generateResponse(Map<String, Deque<String>> urlParams, ContentType contentType) {
    InputMessage inputMessage = parseUrlParams(urlParams);
    OutputMessage outputMessage = generateResponseMessage(inputMessage);

    switch (contentType) {
      case CONTENT_TYPE_PROTO:
        return outputMessage.toByteArray();
      case CONTENT_TYPE_JSON:
        try {
          return JsonFormat.printer().print(outputMessage).getBytes(StandardCharsets.UTF_8);
        } catch (InvalidProtocolBufferException e) {
          throw new IllegalStateException(e);
        }
      case CONTENT_TYPE_TEXT_PROTO:
        return TextFormat.printToString(outputMessage).getBytes(StandardCharsets.UTF_8);
    }

    throw new IllegalStateException("Not reachable code.");
  }

  int getIntParam(Map<String, Deque<String>> urlParams, String param) {
    Deque<String> deque = urlParams.get(param);
    if (deque == null) {
      throw new IllegalStateException("Param not in url param map: " + param);
    }
    return Integer.parseInt(deque.getFirst());
  }

  String getStringParam(Map<String, Deque<String>> urlParams, String param) {
    Deque<String> deque = urlParams.get(param);
    if (deque == null) {
      throw new IllegalStateException("Param not in url param map: " + param);
    }
    return deque.getFirst();
  }

  private enum ContentType {
    CONTENT_TYPE_PROTO("application/protobuf"),
    CONTENT_TYPE_JSON("application/json"),
    CONTENT_TYPE_TEXT_PROTO("text/plain"),
    ;

    public final String typeString;

    ContentType(String typeString) {
      this.typeString = typeString;
    }

    private static ContentType getContentType(Map<String, Deque<String>> urlParams) {
      // TODO(yh_victor): consider whether we should limit this to debug only.
      if (urlParams.containsKey("deb")) {
        for (String value : urlParams.get("deb")) {
          if ("json".equals(value)) {
            return ContentType.CONTENT_TYPE_JSON;
          } else if ("textproto".equals(value)) {
            return ContentType.CONTENT_TYPE_TEXT_PROTO;
          }
        }
      }
      return ContentType.CONTENT_TYPE_PROTO;
    }
  }
}
