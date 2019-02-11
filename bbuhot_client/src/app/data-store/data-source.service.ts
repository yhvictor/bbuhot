import { HttpClient, HttpEvent, HttpEventType, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Message } from 'google-protobuf';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';

import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';
import {
  AdminGameReply,
  AdminGameRequest,
  AdminGameStatusReply,
  AdminGameStatusRequest,
  BetReply,
  BetRequest,
  ListGameReply,
  ListGameRequest
} from '../proto/bbuhot/service/game_pb';

class MessageHttpRequest extends HttpRequest<ArrayBufferLike> {
  constructor(url: string, message: Message) {
    super('POST', url, message.serializeBinary().buffer, {
      responseType: 'arraybuffer',
      withCredentials: true
    });
  }
}

@Injectable()
export class DataSourceService {
  constructor(private http: HttpClient) {}

  private callServiceImpl<I extends Message, O>(
    input: I,
    path: string,
    transform: (bytes: Uint8Array) => O
  ): Observable<O> {
    return this.http.request<Uint8Array>(new MessageHttpRequest(path, input)).pipe(
      filter((httpEvent: HttpEvent<Uint8Array>) => {
        return httpEvent.type === HttpEventType.Response;
      }),
      map((httpEvent) => {
        if (httpEvent instanceof HttpResponse) {
          return transform(httpEvent.body);
        } else {
          throw new Error(JSON.stringify(httpEvent));
        }
      })
    );
  }

  public listGames(listGameRequest: ListGameRequest): Observable<ListGameReply> {
    return this.callServiceImpl(listGameRequest, '/api/bet/list_game', ListGameReply.deserializeBinary);
  }

  public updateGame(adminGameRequest: AdminGameRequest): Observable<AdminGameReply> {
    return this.callServiceImpl(adminGameRequest, '/api/bet/admin_game', AdminGameReply.deserializeBinary);
  }

  public changeStatus(adminGameStatusRequest: AdminGameStatusRequest): Observable<AdminGameStatusReply> {
    return this.callServiceImpl(
      adminGameStatusRequest,
      '/api/bet/admin_status',
      AdminGameStatusReply.deserializeBinary
    );
  }

  public betOnGame(betRequest: BetRequest): Observable<BetReply> {
    return this.callServiceImpl(betRequest, '/api/bet/bet', BetReply.deserializeBinary);
  }

  public userLogin(authRequest: AuthRequest): Observable<AuthReply> {
    return this.callServiceImpl(authRequest, '/api/auth', AuthReply.deserializeBinary);
  }
}
