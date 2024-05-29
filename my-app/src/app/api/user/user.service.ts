import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL, headers } from '../../constant';
import { User } from '../../models/user.model';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private http: HttpClient,
  ) { }

  getUser(): Observable<User> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Authorization', `Bearer ${sessionStorage.getItem('access_token')}`)
    return this.http.get<User>(`${API_URL.getUser}`, { headers: headers })
  }

  updateUser(data: any): Observable<any> {
    return this.http.put(
      `${API_URL.updateUser}`,
      data,
      { headers: headers }
    )
  }

  getUserEmail(email: string): Observable<any> {
    return this.http.get(
      `${API_URL.getUserEmail}/${email}`,
      { headers: headers }
    )
  }

  getUserById(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getUserById}/${id}`,
      { headers: headers }
    )
  }
}
