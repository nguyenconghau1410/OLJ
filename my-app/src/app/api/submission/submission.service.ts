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

  getAll(): Observable<any> {
    return this.http.get(API_URL.getAllSubmission)
  }

  getSubmissionOfProblem(problemId: string): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmissionOfProblem}/${problemId}`,
      { headers: headers }
    )
  }

  getSubmissionByProblem(problemId: string): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmissionByProblem}/${problemId}`,
      { headers: headers }
    )
  }
}
