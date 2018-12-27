import { map , filter } from 'rxjs/operators';
import { HttpClient, HttpRequest, HttpEvent, HttpHeaderResponse, HttpResponse, HttpErrorResponse, HttpSentEvent, HttpEventType } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Game, ListGameReply } from '../proto/bbuhot/service/game_pb';
import { Message } from 'google-protobuf';
import { ListGameRequest } from 'build/generated/source/proto/main/ts/bbuhot/service/game_pb';
import { Observable } from 'rxjs';

@Injectable()
export class ApiService {

    static MessageHttpRequest = class extends HttpRequest<ArrayBufferLike> {
        constructor(url:string, message:Message) {
            super('POST', url, message.serializeBinary().buffer, {
                responseType: 'arraybuffer'
            });
        }
    }

    constructor(private http: HttpClient) {}

     private callServiceImpl<I extends Message, O extends Message>(input:I, path:string, transform:(bytes: Uint8Array) => O) : Observable<O> {
        return this.http.request<Uint8Array>(new ApiService.MessageHttpRequest('http://localhost:8080' + path, input))
        .pipe(
            filter((httpEvent:HttpEvent<Uint8Array>) => {
                return httpEvent.type == HttpEventType.Response;
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

    public listGames(listGameRequest:ListGameRequest): Observable<ListGameReply> {
        return this.callServiceImpl(listGameRequest, "/api/bet/list_game", ListGameReply.deserializeBinary);
    }
}