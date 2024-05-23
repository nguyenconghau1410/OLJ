import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CheckStatusService } from '../../service/check/check-status.service';
import { Problem } from '../../models/problem.model';
import { Observable } from 'rxjs';
import { Topic } from '../../models/topic.model';
import { API_URL, headers } from '../../constant';

@Injectable({
  providedIn: 'root'
})
export class ProblemService {

  constructor(
    private http: HttpClient,
  ) { }

  addProblem(problem: Problem, topics: {}[]): Observable<any> {
    return this.http.post(
      API_URL.addProblem,
      {
        problem: problem,
        topics: topics
      },
      { headers: headers }
    )
  }

  getAllProblem(): Observable<any> {
    return this.http.get(
      API_URL.getAllProblem,
    )
  }

  getProblem(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getProblem}/${id}`,
    )
  }

  getProblemByCreator(): Observable<any> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Authorization', `Bearer ${sessionStorage.getItem('access_token')}`)
    return this.http.get(
      API_URL.getProblemByCreator,
      { headers: headers }
    )
  }

  updateState(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.updateState}/${id}`,
      { headers: headers }
    )
  }

  update(problem: Problem, topics: Topic[]): Observable<any> {
    return this.http.put(
      API_URL.updateProblem,
      {
        problem: problem,
        topics: topics
      },
      { headers: headers }
    )
  }

  getProblemByKeyword(keyword: string): Observable<any> {
    return this.http.get(
      `${API_URL.getProblemByKeyword}/${keyword}`,
      { headers: headers }
    )
  }
}
