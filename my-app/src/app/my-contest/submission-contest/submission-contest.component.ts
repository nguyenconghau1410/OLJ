import { Component } from '@angular/core';
import { DetailContest } from '../../models/detail-contest.model';
import { ContestService } from '../../api/contest/contest.service';
import { ActivatedRoute, Router, UrlSegment } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DataService } from '../../service/data/data.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-submission-contest',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './submission-contest.component.html',
  styleUrl: './submission-contest.component.scss'
})
export class SubmissionContestComponent {
  detailContestList: DetailContest[] = []
  error = ''
  problemId = ''
  user!: User
  constructor(
    private contestService: ContestService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private dataService: DataService
  ) { }

  ngOnInit() {
    if (!sessionStorage.getItem('access_token')) {
      this.router.navigate(['login'])
      return
    }
    this.checkForMySubmission()
    this.dataService.user?.subscribe(
      user => {
        this.user = user
      }
    )
  }

  checkForMySubmission() {
    this.activatedRoute.url.subscribe((segments: UrlSegment[]) => {
      const hasAllSubmission = segments.some(segment => segment.path === 'all-submission');
      const hasMySubmission = segments.some(segment => segment.path === 'my-submission');
      if (hasAllSubmission) {
        this.activatedRoute.params.subscribe(params => {
          const id = params['id']
          if (id) {
            this.contestService.getAllSubmissionOfContest(id).subscribe(
              (data) => {
                if (data) {
                  this.detailContestList = data
                }
              },
              (error) => {
                this.error = `Đã có lỗi xảy ra: ${error.status}`
              }
            )
          }
        })
      }
      else if (hasMySubmission) {
        this.activatedRoute.params.subscribe(params => {
          const id = params['id']
          if (id) {
            this.contestService.getMySubmissionOfContest(id).subscribe(
              (data) => {
                if (data) {
                  this.detailContestList = data
                }
              },
              (error) => {
                this.error = `Đã có lỗi xảy ra: ${error.status}`
              }
            )
          }
        })
      }
      else {
        this.activatedRoute.params.subscribe(params => {
          const id = params['id']
          const problemId = params['problemId']
          this.problemId = problemId
          if (id && problemId) {
            this.contestService.getSubmissionOfProblemInContest(id, problemId).subscribe(
              (data) => {
                if (data) {
                  this.detailContestList = data
                }
              },
              (error) => {
                this.error = `Đã có lỗi xảy ra: ${error.status}`
              }
            )
          }
        })
      }
    });
  }

  formatNumber(num: number) {
    return parseFloat(num.toFixed(2))
  }

  normalizeString(language: string): string {
    if (language === 'java')
      return "Java"
    else if (language === 'python')
      return "Python"
    else
      return "C++"
  }

  viewSubmission(id: string) {
    this.router.navigate(['contests', id])
  }

  check(userId: string): boolean {
    if (this.user.role!.code !== 'STUDENT')
      return true
    let ok = true
    if (this.problemId === '') {
      return true
    }
    if (this.user.id !== userId) {
      ok = false
    }
    return ok
  }
}



