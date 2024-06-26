import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CodemirrorModule } from '@ctrl/ngx-codemirror';
import { ActivatedRoute, Router } from '@angular/router';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { ContestService } from '../../api/contest/contest.service';
import { ToastrService } from 'ngx-toastr';
import { WebsocketService } from '../../service/websocket/websocket.service';
import { DataService } from '../../service/data/data.service';
import { User } from '../../models/user.model';
import { ResultTest } from '../../models/result-test.model';
import { DetailContest } from '../../models/detail-contest.model';


@Component({
  selector: 'app-result-contest',
  standalone: true,
  imports: [CodemirrorModule, CommonModule, FormsModule, NzIconModule],
  templateUrl: './result-contest.component.html',
  styleUrl: './result-contest.component.scss'
})
export class ResultContestComponent {
  detailContest!: DetailContest
  user!: User
  code: string = ""
  options = {}
  completed = false
  resultList: ResultTest[] = []
  error = false

  constructor(
    private contestService: ContestService,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private websocketService: WebsocketService,
    private router: Router,
    private dataService: DataService
  ) { }

  ngOnInit() {
    if (!sessionStorage.getItem('access_token')) {
      this.router.navigate(['login'])
      return
    }
    this.dataService.user?.subscribe(
      user => {
        this.user = user
      }
    )

    this.route.params.subscribe(params => {
      const id = params['submissionId']
      if (params['submissionId']) {
        this.contestService.getSubmission(params['submissionId']).subscribe(
          (data) => {
            if (data) {
              this.detailContest = data

              this.code = this.detailContest.source
              if (this.detailContest.language === "java") {
                this.options = {
                  lineNumbers: true,
                  theme: 'default',
                  mode: 'text/x-java',
                }
              }
              else if (this.detailContest.language === 'python') {
                this.options = {
                  lineNumbers: true,
                  theme: 'default',
                  mode: 'text/x-python',
                }
              }
              else if (this.detailContest.language === 'cpp') {
                this.options = {
                  lineNumbers: true,
                  theme: 'default',
                  mode: 'text/x-c++src',
                }
              }
              else {
                this.options = {
                  lineNumbers: true,
                  theme: 'default',
                  mode: 'text/x-csrc',
                }
              }

              if (this.user.role!.code === 'STUDENT' && this.user.id !== this.detailContest.userId) {
                this.error = true
                return
              }

              if (!this.detailContest.result) {
                // send id
                if (this.detailContest.language === 'java') {
                  this.websocketService.sendMessage('/app/contest/execute', id)
                }
                else if (this.detailContest.language === 'python') {
                  this.websocketService.sendMessage('/app/contest/execute-python', id)
                }
                else {
                  this.websocketService.sendMessage('/app/contest/execute-mingw', id)
                }

                // listen websocket
                let destination = `/user/${this.detailContest.userId}/queue/messages`
                let i = 0
                this.websocketService.subscribe(destination, (frame: any) => {
                  ++i
                  const decoder = new TextDecoder('utf-8');
                  const messageText = decoder.decode(frame.binaryBody);
                  const message = JSON.parse(messageText);
                  if (i <= this.detailContest.totalTest) {
                    if (message['result'] === "COMPILATION ERROR") {
                      this.detailContest.result = message['result']
                    }
                    else
                      this.resultList.push(message)
                  }
                  else {
                    this.detailContest.result = message['result']
                    this.detailContest.point = parseFloat(message['scored'])
                  }
                })

              }
              else {
                this.resultList = this.detailContest.testcases
              }
            }
          },
          (error) => {
            this.toastrService.info(
              'Không tìm thấy dữ liệu!', '', { timeOut: 2000 }
            )
          }
        )
      }
    })
  }

  formatNumber(num: number) {
    return parseFloat(num.toFixed(2))
  }


  check(result: string): boolean {
    if (result === 'TIME LIMIT EXCEEDED') {
      return false
    }
    return true
  }

  normalizeString(language: string): string {
    if (language === 'java')
      return "Java"
    else if (language === 'python')
      return "Python"
    else if (language === 'cpp')
      return "C++"
    else
      return "C"
  }

  redirect(id: string) {
    this.router.navigate(['contests', id, 'challenges'])
  }
}
