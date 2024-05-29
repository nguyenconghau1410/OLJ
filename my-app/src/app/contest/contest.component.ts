import { Component } from '@angular/core';
import { ContestService } from '../api/contest/contest.service';
import { Router } from '@angular/router';
import { Contest } from '../models/contest.model';
import { CommonModule } from '@angular/common';
import { DataService } from '../service/data/data.service';
import { User } from '../models/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-contest',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contest.component.html',
  styleUrl: './contest.component.scss'
})
export class ContestComponent {
  check: boolean = false
  contestList: Contest[] = []
  contestFuture: Contest[] = []
  user!: User
  constructor(
    private contestService: ContestService,
    private router: Router,
    private dataService: DataService,
    private toastrService: ToastrService
  ) { }

  ngOnInit() {
    this.contestService.getContestList().subscribe(
      (data) => {
        if (data) {
          for (let i = 0; i < data.length; i++) {
            if (!this.checkDateTime(data[i]['endTime'], data[i]['hourEnd'])) {
              this.contestList.push(data[i])
            }
            else {
              this.contestFuture.push(data[i])
            }
          }
        }
      },
      (error) => {
        this.check = true
      }
    )
    this.dataService.user?.subscribe(
      user => {
        this.user = user
      }
    )
  }

  checkDateTime(endDate: string, endHour: string): boolean {
    const endTime = new Date(`${endDate}T${endHour}:00`).getTime();
    const now = new Date().getTime();

    let different = now - endTime;
    if (different > 0) {
      return false
    }
    return true
  }

  redirectLandingPage(contestId: string) {
    this.router.navigate(['landing', contestId])
  }

  signup(index: number) {
    for (let i = 0; i < this.contestFuture[index].signups.length; i++) {
      if (this.contestFuture[index].signups[i].email === this.user.email) {
        this.toastrService.success(
          'Bạn đã đăng ký contest này, hãy đợi duyệt!', '', { timeOut: 2000 }
        )
        return
      }
    }
    this.contestFuture[index].signups.push({ email: this.user.email, status: 'signedUp' })
    this.contestService.update(this.contestFuture[index]).subscribe(
      () => {
        this.toastrService.success(
          'Đã đăng ký, hãy đợi duyệt!', '', { timeOut: 2000 }
        )
      }
    )
  }

  checkSignUp(index: number): boolean {
    for (let i = 0; i < this.contestFuture[index].signups.length; i++) {
      if (this.contestFuture[index].signups[i].email === this.user.email) {
        return true
      }
    }
    return false
  }
}
