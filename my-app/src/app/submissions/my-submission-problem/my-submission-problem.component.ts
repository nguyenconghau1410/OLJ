import { Component } from '@angular/core';
import { Submission } from '../../models/submission.model';
import { User } from '../../models/user.model';
import { SubmissionService } from '../../api/submission/submission.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router, UrlSegment } from '@angular/router';
import { WebsocketService } from '../../service/websocket/websocket.service';
import { DataService } from '../../service/data/data.service';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-my-submission-problem',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './my-submission-problem.component.html',
  styleUrl: './my-submission-problem.component.scss'
})
export class MySubmissionProblemComponent {
  submissionList: Submission[] = []
  user!: User
  constructor(
    private submissionService: SubmissionService,
    private toastrService: ToastrService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private dataService: DataService
  ) { }

  ngOnInit() {
    this.checkForMySubmission()
    this.dataService.user?.subscribe(
      user => {
        this.user = user
      }
    )
  }

  checkForMySubmission() {
    this.activatedRoute.url.subscribe((segments: UrlSegment[]) => {
      const hasAllSubmission = segments.some(segment => segment.path === 'all-submissions');
      const hasMySubmission = segments.some(segment => segment.path === 'my-submissions');
      if (hasMySubmission) {
        this.activatedRoute.params.subscribe(params => {
          if (params['problemId']) {
            this.submissionService.getSubmissionOfProblem(params['problemId']).subscribe(
              (data) => {
                if (data) {
                  this.submissionList = data
                }
              },
              (error) => {
                this.toastrService.error(
                  `Đã có lỗi xảy ra: ${error.status}`
                )
              }
            )
          }
        })
      }
      else if (hasAllSubmission) {
        this.activatedRoute.params.subscribe(params => {
          if (params['problemId']) {
            this.submissionService.getSubmissionByProblem(params['problemId']).subscribe(
              (data) => {
                if (data) {
                  this.submissionList = data
                }
              },
              (error) => {
                this.toastrService.error(
                  `Đã có lỗi xảy ra: ${error.status}`
                )
              }
            )
          }
        })
      }
    });
  }

  adjustingString(result: string) {
    if (result === 'ACCEPTED') {
      return "AC"
    }
    else if (result === 'WRONG ANSWER') {
      return "WA"
    }
    else if (result === 'RUNTIME ERROR') {
      return "RE"
    }
    else if (result === 'TIME LIMIT EXCEEDED') {
      return "TLE"
    }
    else if (result == 'COMPILATION ERROR') {
      return "CE"
    }
    return ""
  }

  check(x: string): boolean {
    if (this.adjustingString(x) === 'TLE' || this.adjustingString(x) === 'CE') {
      return false
    }
    return true
  }

  checkVisible(userId: string): boolean {
    if (this.user.role?.code !== 'STUDENT') {
      return true
    }
    if (this.user.id !== userId) {
      return false
    }
    return true
  }

  redirect(id: string) {
    this.router.navigate(['problems', id])
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

  click(id: string) {
    if (this.user.role!.code !== 'USER') {
      this.router.navigate(['submissions', id])
    }
  }
}
