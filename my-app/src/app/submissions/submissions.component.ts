import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubmissionService } from '../api/submission/submission.service';
import { ToastrService } from 'ngx-toastr';
import { Submission } from '../models/submission.model';
import { Router } from '@angular/router';
import { WebsocketService } from '../service/websocket/websocket.service';
import { DataService } from '../service/data/data.service';
import { User } from '../models/user.model';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-submissions',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './submissions.component.html',
  styleUrl: './submissions.component.scss'
})
export class SubmissionsComponent {
  submissionList: Submission[] = []
  user!: User
  constructor(
    private submissionService: SubmissionService,
    private toastrService: ToastrService,
    private router: Router,
    private websocketService: WebsocketService,
    private dataService: DataService
  ) { }

  ngOnInit() {
    this.submissionService.getAll().subscribe(
      (data) => {
        if (data) {
          this.submissionList = data
        }
      },
      (error) => {
        this.toastrService.error(
          `Đã có lỗi xảy ra: ${error.status}`, '', { timeOut: 2000 }
        )
      }
    )
    let destination = `/user/public/queue/messages`
    this.websocketService.subscribe(destination, (frame: any) => {
      const decoder = new TextDecoder('utf-8');
      const messageText = decoder.decode(frame.binaryBody);
      const message = JSON.parse(messageText);
      this.submissionList.unshift(message)
    })

    this.dataService.user?.subscribe(
      user => {
        this.user = user
      }
    )
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
