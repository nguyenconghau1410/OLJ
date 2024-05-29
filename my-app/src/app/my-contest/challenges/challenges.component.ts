import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { ContestService } from '../../api/contest/contest.service';
import { Contest } from '../../models/contest.model';
import { Subscription, interval } from 'rxjs';
import { CommonModule } from '@angular/common';
import { DataService } from '../../service/data/data.service';
import { DetailProblemComponent } from '../../detail-problem/detail-problem.component';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-challenges',
  standalone: true,
  imports: [MatIconModule, CommonModule, DetailProblemComponent],
  templateUrl: './challenges.component.html',
  styleUrl: './challenges.component.scss'
})
export class ChallengesComponent {
  contest!: Contest
  problems: { id: string, title: string, email: string, point: number }[] = []
  isFinished = false
  countdown: string = ''
  check: boolean = true
  isTaskView = false
  user!: User
  private countdownSubscription!: Subscription
  constructor(
    private router: Router,
    private contestService: ContestService,
    private activatedRoute: ActivatedRoute,
    private dataService: DataService
  ) { }

  ngOnInit() {
    if (!sessionStorage.getItem('access_token')) {
      this.router.navigate(['login'])
      return
    }
    this.activatedRoute.params.subscribe(params => {
      if (params['id']) {
        this.contestService.getContest(params['id']).subscribe(
          (data) => {
            if (data) {
              this.contest = data
              this.handleTime()
              this.dataService.user?.subscribe(
                user => {
                  this.user = user
                  if (this.countdown === 'Chưa bắt đầu') {
                    if (user.email !== this.contest.createdBy) {
                      this.check = false
                      return
                    }
                  }
                }
              )
            }
          }
        )
        this.contestService.getChallenges(params['id']).subscribe(
          (data) => {
            if (data) {
              this.problems = data
            }
          }
        )
      }
    })

    this.activatedRoute.params.subscribe(params => {
      if (params['problemId']) {
        this.isTaskView = true
      }
    })
  }

  handleTime() {
    if (this.calculateTimeRemaining() === false) {
      return
    }
    this.countdownSubscription = interval(1000).subscribe(() => {
      this.calculateTimeRemaining();
    });

    this.calculateTimeRemaining();
  }

  calculateTimeRemaining() {
    const endDate = new Date(`${this.contest.endTime}T${this.contest.hourEnd}:00`).getTime();
    const startDate = new Date(`${this.contest.startTime}T${this.contest.hourStart}:00`).getTime();
    let now = new Date().getTime();
    let isStarted = now - startDate;
    if (isStarted <= 0) {
      this.countdown = "Chưa bắt đầu"
      return false
    }
    let difference = endDate - now;
    if (difference > 0) {
      const days = Math.floor(difference / (1000 * 60 * 60 * 24));
      const hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = Math.floor((difference % (1000 * 60)) / 1000);

      this.countdown = `${days}d ${hours}h ${minutes}m ${seconds}s`;
    }
    else {
      this.isFinished = true
      this.countdown = "Hết thời gian"
      // this.countdownSubscription.unsubscribe()
      return false
    }
    return true
  }

  redirectTask(id: string) {
    if (!this.isFinished) {
      const currentUrl = this.router.url
      this.router.navigate([currentUrl, id])
      this.isTaskView = true
    }
    else {
      this.router.navigate(['problems', id])
    }
  }

  redirectLeaderBoard() {
    this.router.navigate(['contests', this.contest.id, 'leaderboard', 'all'])
  }

  redirectAmin(contestId: string) {
    this.router.navigate(['my-contest'])
  }

  redirectSubmission(contestId: string, userId: string, problemId: string) {
    if (contestId && problemId) {
      this.router.navigate(['contests', contestId, problemId])
    }
    else if (contestId && userId) {
      this.router.navigate(['contests', contestId, 'my-submission'])
    }
    else {
      this.router.navigate(['contests', contestId, 'all-submission'])
    }
  }


  ngOnDestroy() {
    if (this.countdownSubscription) {
      this.countdownSubscription.unsubscribe();
    }
  }

  isAdmin(): boolean {
    if (this.contest.createdBy === this.user.email) {
      return true
    }
    return false
  }
}
