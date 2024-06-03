import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Submit } from '../../models/submit.model';
import { Observable } from 'rxjs';
import { API_URL, headers } from '../../constant';
@Injectable({
  providedIn: 'root'
})
export class SubmissionService {

  constructor(
    private http: HttpClient
  ) { }

  submit(submit: Submit): Observable<any> {
    return this.http.post(
      API_URL.submit,
      submit,
      { headers: headers }
    )
  }

  getSubmission(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmission}/${id}`,
      { headers: headers }
    )
  }

  getAll(pageNumber: number): Observable<any> {
    return this.http.get(`${API_URL.getAllSubmission}/${pageNumber}`, { headers: headers })
  }

  getSubmissionOfProblem(problemId: string, pageNumber: number): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmissionOfProblem}/${problemId}/${pageNumber}`,
      { headers: headers }
    )
  }

  getSubmissionByProblem(problemId: string, pageNumber: number): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmissionByProblem}/${problemId}/${pageNumber}`,
      { headers: headers }
    )
  }

  count(): Observable<any> {
    return this.http.get(
      API_URL.countTotalSubmissions,
      { headers: headers }
    )
  }

  countSubmissionProblem(problemId: string, type: string): Observable<any> {
    return this.http.get(
      `${API_URL.countSubmissionProblem}/${problemId}/${type}`,
      { headers: headers }
    )
  }

  getStatistic(): Observable<any> {
    return this.http.get(
      API_URL.getStatistic,
      { headers: headers }
    )
  }

  countMySubmission(): Observable<any> {
    return this.http.get(
      API_URL.countMySubmissions,
      { headers: headers }
    )
  }

  getMySubmission(pageNumber: number): Observable<any> {
    return this.http.get(
      `${API_URL.getMySubmisisons}/${pageNumber}`,
      { headers: headers }
    )
  }

  getFigure(): Observable<any> {
    return this.http.get(
      API_URL.getFigure,
      { headers: headers }
    )
  }
}
