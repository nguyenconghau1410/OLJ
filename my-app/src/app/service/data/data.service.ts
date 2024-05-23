import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserService } from '../../api/user/user.service';
import { User } from '../../models/user.model';
import { Contest } from '../../models/contest.model';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private userSubject: BehaviorSubject<any> = new BehaviorSubject<string | null>(null)
  public user: Observable<User> | undefined
  private contestSubject: BehaviorSubject<any> = new BehaviorSubject<string | null>(null)
  public contest: Observable<Contest> | undefined

  constructor() {
    this.user = this.userSubject.asObservable()
    this.contest = this.contestSubject.asObservable()
  }

  setUserSubject(user: User) {
    this.userSubject.next(user)
  }

  setContestSubject(contest: Contest) {
    this.contestSubject.next(contest)
  }
}
