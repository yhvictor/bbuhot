import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

// TODO(luciusgone): intended for developers. do not show this to end user
export class HttpErrorInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';
        if (error.error instanceof ErrorEvent) {
          // client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // server-side error
          const body = String.fromCharCode.apply(null, new Uint8Array(error.error));
          errorMessage = `Error Code: ${error.status}\nMessage: ${body}`;
        }
        window.alert(errorMessage);
        return throwError(errorMessage);
      })
    );
  }
}
