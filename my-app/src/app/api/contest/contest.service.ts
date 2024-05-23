import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contest } from '../../models/contest.model';
import { API_URL, headers } from '../../constant';
import { Submit } from '../../models/submit.model';

@Injectable({
  providedIn: 'root'
})
export class ContestService {

  constructor(
    private http: HttpClient
  ) { }

  create(contest: Contest): Observable<any> {
    return this.http.post(
      API_URL.addContest,
      contest,
      { headers: headers }
    )
  }

  getOne(): Observable<any> {
    return this.http.get(
      API_URL.getContest,
      { headers: headers }
    )
  }

  getContest(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getContest}/${id}`,
      { headers: headers }
    )
  }

  update(contest: Contest): Observable<any> {
    return this.http.put(
      API_URL.updateContest,
      contest,
      { headers: headers }
    )
  }

  getChallenges(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getChallenges}/${id}`,
      { headers: headers },
    )
  }

  getParticipants(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getParticipants}/${id}`,
      { headers: headers }
    )
  }

  addSubmission(submit: Submit): Observable<any> {
    return this.http.post(
      API_URL.addSubmissionContest,
      submit,
      { headers: headers }
    )
  }

  getSubmission(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmissionContest}/${id}`,
      { headers: headers }
    )
  }

  getAllSubmissionOfContest(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getAllSubmissionOfContest}/${id}`,
      { headers: headers }
    )
  }

  getMySubmissionOfContest(id: string): Observable<any> {
    return this.http.get(
      `${API_URL.getMySubmissionOfContest}/${id}`,
      { headers: headers }
    )
  }

  getSubmissionOfProblemInContest(id: string, problemId: string): Observable<any> {
    return this.http.get(
      `${API_URL.getSubmissionOfProblemInContest}/${id}/${problemId}`,
      { headers: headers }
    )
  }
}
