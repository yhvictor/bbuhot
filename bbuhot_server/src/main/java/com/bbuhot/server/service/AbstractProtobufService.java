package com.bbuhot.server.service;

import com.google.protobuf.Descriptors;
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
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

abstract class AbstractProtobufService<InputMessage extends Message, OutputMessage extends Message>
    implements HttpHandler {

  abstract InputMessage getInputMessageDefaultInstance();

  abstract OutputMessage callProtobufServiceImpl(InputMessage inputMessage);

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
      exchange.getResponseSender().send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
    }
  }

  private void handleRequestWithoutException(HttpServerExchange exchange) {
    Map<String, Deque<String>> urlParams = exchange.getQueryParameters();
    ContentType contentType = ContentType.getContentType(urlParams);
    urlParams.remove("deb");

    InputMessage inputMessage = parseRequest(urlParams, getInputMessageDefaultInstance());
    OutputMessage outputMessage = callProtobufServiceImpl(inputMessage);

    final byte[] bytes;
    switch (contentType) {
      case CONTENT_TYPE_PROTO:
        bytes = outputMessage.toByteArray();
        break;
      case CONTENT_TYPE_JSON:
        try {
          bytes = JsonFormat.printer().print(outputMessage).getBytes(StandardCharsets.UTF_8);
        } catch (InvalidProtocolBufferException e) {
          throw new IllegalStateException(e);
        }
        break;
      case CONTENT_TYPE_TEXT_PROTO:
        bytes = TextFormat.printToString(outputMessage).getBytes(StandardCharsets.UTF_8);
        break;
      default:
        throw new IllegalStateException("Not reachable code.");
    }

    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType.typeString);
    exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
  }

  private InputMessage parseRequest(
      Map<String, Deque<String>> urlParams, InputMessage defaultInstance) {
    List<String> unsentParams = new ArrayList<>();
    Message.Builder builder = defaultInstance.toBuilder();
    for (Descriptors.FieldDescriptor fieldDescriptor :
        defaultInstance.getDescriptorForType().getFields()) {
      String fullName = fieldDescriptor.getName();
      Deque<String> deque = urlParams.get(fullName);
      if (deque == null) {
        unsentParams.add(fullName);
        continue;
      }

      String rawValue = deque.getFirst();

      switch (fieldDescriptor.getJavaType()) {
        case INT:
          builder.setField(fieldDescriptor, Integer.parseInt(rawValue));
          break;
        case LONG:
          builder.setField(fieldDescriptor, Long.parseLong(rawValue));
          break;
        case STRING:
          builder.setField(fieldDescriptor, rawValue);
          break;
        default:
          throw new IllegalStateException("Non supported type of param: " + fullName);
      }
    }

    if (!unsentParams.isEmpty()) {
      throw new IllegalStateException("Required param is not sent to server: " + unsentParams);
    }

    @SuppressWarnings("unchecked")
    InputMessage inputMessage = (InputMessage) builder.build();
    return inputMessage;
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
