import { HttpClient, HttpEvent, HttpEventType, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Message } from 'google-protobuf';
import { Observable } from 'rxjs';
import { filter , map } from 'rxjs/operators';
import { Game, ListGameReply, ListGameRequest } from '../proto/bbuhot/service/game_pb';

@Injectable()
export class ApiService {

    static MessageHttpRequest = class extends HttpRequest<ArrayBufferLike> {
        constructor(url: string, message: Message) {
            super('POST', url, message.serializeBinary().buffer, {
                responseType: 'arraybuffer'
            });
        }
    };

    constructor(private http: HttpClient) {}

     private callServiceImpl<I extends Message, O>(input: I, path: string, transform: (bytes: Uint8Array) => O): Observable<O> {
        return this.http.request<Uint8Array>(new ApiService.MessageHttpRequest('http://165.227.17.140:8080' + path, input))
        .pipe(
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
}
