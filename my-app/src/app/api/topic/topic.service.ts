import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, headers } from '../../constant';
import { Topic } from '../../models/topic.model';
@Injectable({
  providedIn: 'root'
})
export class TopicService {

  constructor(
    private http: HttpClient
  ) { }

  getAllTopic(): Observable<Topic[]> {
    return this.http.get<Topic[]>(API_URL.getAllTopic, { headers: headers });
  }

  getTopicOfProblem(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getTopicOfProblem}/${id}`,
      { headers: headers }
    )
  }
}
