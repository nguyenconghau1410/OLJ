import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private access_token = new BehaviorSubject<string | null>(null)

  constructor() {
    const storedToken = sessionStorage.getItem('access_token')
    if (storedToken) {
      this.accessToken.next(storedToken)
    }
  }

  setAccessToken(token: string): void {
    sessionStorage.setItem('access_token', token)
    this.access_token.next(token)
  }

  get accessToken(): BehaviorSubject<string | null> {
    return this.access_token
  }

  clearAccessToken(): void {
    sessionStorage.removeItem('access_token')
  }

}
