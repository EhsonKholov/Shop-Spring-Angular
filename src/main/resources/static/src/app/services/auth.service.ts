import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {tap} from "rxjs/operators";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(user: any) {
    return this.http.post(`${environment.srurl}/auth/signin`, user)
      .pipe(
        tap(this.setToken)
      )
  }

  private setToken(response: any) {
    if (response) {
      localStorage.setItem('access_token', response.access_token)
      localStorage.setItem('refresh_token', response.refresh_token)
      localStorage.setItem('email', response.email)
      localStorage.setItem('firstname', response.firstname)
      localStorage.setItem('lastname', response.lastname)
      localStorage.setItem('userId', response.userId)
      localStorage.setItem('roles', JSON.stringify(response.roles))
    } else {
      window.localStorage.clear()
    }
  }

  getAccessToken() {
    return localStorage.getItem('access_token') || ''
  }

  getRefreshToken() {
    return localStorage.getItem('refresh_token') || ''
  }

  logout() {
    this.setToken(null)
  }

  isAuthenticated() {
    return !!this.getAccessToken()
  }
}
