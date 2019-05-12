import {
  HttpClient,
  HttpEvent,
  HttpEventType,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Message } from 'google-protobuf';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';
import { ListGameReply, ListGameRequest } from '../proto/bbuhot/service/game_pb';

export class ApiRemoteError extends Error {
  constructor(error: any) {
    super(JSON.stringify(error));

    Object.setPrototypeOf(this, ApiRemoteError.prototype);
  }
}

@Injectable()
export class ApiService {
  static MessageHttpRequest = class extends HttpRequest<ArrayBufferLike> {
    constructor(url: string, message: Message) {
      super('POST', url, message.serializeBinary().buffer, {
        responseType: 'arraybuffer',
        withCredentials: true
      });
    }
  };

  constructor(private http: HttpClient) {}

  private callServiceImpl<I extends Message, O>(
    input: I,
    path: string,
    transform: (bytes: Uint8Array) => O
  ): Observable<O> {
    return this.http.request<Uint8Array>(new ApiService.MessageHttpRequest(path, input)).pipe(
      filter((httpEvent: HttpEvent<Uint8Array>) => {
        return httpEvent.type === HttpEventType.Response;
      }),
      map((httpEvent) => {
        if (httpEvent instanceof HttpResponse) {
          return transform(httpEvent.body as Uint8Array);
        } else {
          throw new ApiRemoteError(httpEvent);
        }
      })
    );
  }

  public listGames(listGameRequest: ListGameRequest): Observable<ListGameReply> {
    return this.callServiceImpl(
      listGameRequest,
      '/api/bet/list_game',
      ListGameReply.deserializeBinary
    );
  }

  public auth(authRequest: AuthRequest): Observable<AuthReply> {
    return this.callServiceImpl(authRequest, '/api/auth', AuthReply.deserializeBinary);
  }
}
