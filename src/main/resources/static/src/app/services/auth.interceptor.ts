import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    if (this.authService.isAuthenticated()) {
      request = request.clone({
        headers: request.headers.append('Authorization', this.authService.getAccessToken())
      })

    }

    return next.handle(request)
      .pipe(
        catchError(error => {
          if (error.status === 401) {
            this.authService.logout()
            this.router.navigate(['/admin', 'login'])
          }
          return throwError(error)
        })
      );
  }
}
