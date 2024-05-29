import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import Editor from 'ckeditor5-custom-build/build/ckeditor';
import { Contest } from '../../models/contest.model';
import { ContestService } from '../../api/contest/contest.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ProblemService } from '../../api/problem/problem.service';
import { UserService } from '../../api/user/user.service';
import { User } from '../../models/user.model';
import { WebsocketService } from '../../service/websocket/websocket.service';
@Component({
  selector: 'app-detail-contest',
  standalone: true,
  imports: [CommonModule, CKEditorModule, FormsModule, MatIconModule],
  templateUrl: './detail-contest.component.html',
  styleUrl: './detail-contest.component.scss'
})
export class DetailContestComponent {
  public Editor = Editor
  contest!: Contest
  isChecked: boolean = false
  index = 0
  showAddProblemForm: boolean = false
  // index 1
  problemsSearch: { id: string, title: string, email: string, point: number }[] = []
  problems: { id: string, title: string, email: string, point: number }[] = []
  problem: { id: string, title: string, email: string, point: number } = {
    id: '',
    title: '',
    email: '',
    point: 0
  }
  // index 3
  users: User[] = []
  userSearch: string = ''
  user: User = {
    id: '',
    email: '',
    name: '',
    tag: '',
    avatar: '',
    faculty: '',
    classname: ''
  }
  // index 4
  signups: User[] = []
  // index 2
  selected: string = ''
  content: string = ''

  constructor(
    private contestService: ContestService,
    private problemService: ProblemService,
    private toastrService: ToastrService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private websocketService: WebsocketService
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.contestService.getContest(params['id']).subscribe(
          (data) => {
            if (data) {
              this.contest = data
              if (!this.contest.endTime && !this.contest.hourEnd) {
                this.isChecked = true;
              }
            }
          },
          (error) => {
            this.toastrService.error(
              `Đã có lỗi xảy ra: ${error.status}`, '', { timeOut: 2000 }
            )
          }
        )
      }
    })
  }

  saveChange(index: number) {
    if (index === 1) {
      let list: { id: string, point: number }[] = []
      for (let i = 0; i < this.problems.length; i++) {
        list.push({ id: this.problems[i].id, point: this.problems[i].point })
      }
      this.contest.problems = list
    }
    this.contestService.update(this.contest).subscribe(
      (data) => {
        this.toastrService.success(
          'Lưu thành công', '',
          { timeOut: 2000 }
        )
      },
      (error) => {
        this.toastrService.info(
          `Đã có lỗi xảy ra, hãy thử lại: ${error.status}`, '',
          { timeOut: 2000 }
        )
      }
    )
  }

  change() {
    this.isChecked = !this.isChecked
  }

  clickTab(index: number) {
    if (index === 4) {
      if (this.contest.mode === 'NRR') {
        this.toastrService.info(
          'Chế độ contest của bạn không phù hợp với chức năng này', '',
          { timeOut: 2000 }
        )
        return
      }
      this.contestService.getSignups(this.contest.id).subscribe(
        (data) => {
          this.signups = data
        }
      )
    }
    else if (index === 1) {
      this.contestService.getChallenges(this.contest.id).subscribe(
        (data) => {
          if (data) {
            this.problems = data
          }
        },
        (error) => {
          this.toastrService.info(
            `Đã có lỗi xảy ra: ${error.status}`, '',
            { timeOut: 2000 }
          )
        }
      )
    }
    else if (index === 3) {
      this.contestService.getParticipants(this.contest.id).subscribe(
        (data) => {
          if (data) {
            this.users = data
          }
        },
        (error) => {
          this.toastrService.info(
            `Đã có lỗi xảy ra: ${error.status}`, '',
            { timeOut: 2000 }
          )
        }
      )
    }
    this.index = index
  }

  closeForm() {
    this.showAddProblemForm = false
    this.problemsSearch = []
  }

  openForm() {
    this.showAddProblemForm = true
  }

  changeState() {
    if (this.contest.state === "PRIVATE") {
      this.contest.state = "PUBLIC"
    }
    else {
      this.contest.state = "PRIVATE"
    }
  }

  onInputChange(event: Event) {
    const inputElement = event.target as HTMLInputElement
    const newValue = inputElement.value
    if (newValue !== "") {
      this.problemService.getProblemByKeyword(newValue).subscribe(
        (data) => {
          if (data) {
            this.problemsSearch = data
          }
        },
      )
    }
    else {
      this.problemsSearch = []
    }
  }

  onInputChangeUser(event: Event) {
    const inputElement = event.target as HTMLInputElement
    const newValue = inputElement.value
    if (newValue !== "") {
      this.userService.getUserEmail(newValue).subscribe(
        (data) => {
          if (data) {
            this.user = data
          }
        },
      )
    }
  }

  // index 2
  chooseProblem(p: { id: string, title: string, email: string, point: number }) {
    this.problem = p
    this.problemsSearch = []
  }

  addProblem() {
    this.problems.push(this.problem)
    console.log(this.problems)
    this.problem = {
      id: '',
      title: '',
      email: '',
      point: 0
    }
    this.showAddProblemForm = false
  }

  removeTask(i: number) {
    this.problems.splice(i, 1)
  }

  validatePositiveNumber(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const value = inputElement.value;
    if (parseFloat(value) < 0) {
      inputElement.value = '';
    }
  }

  // redirect
  redirect(id: string) {
    this.router.navigate(['problems', id])
  }

  redirectLandingPage(contestId: string) {
    this.router.navigate(['landing', contestId])
  }

  redirectChallengesPage(contestId: string) {
    this.router.navigate(['contests', contestId, 'challenges'])
  }

  // index 3
  clickUser() {
    this.userSearch = this.user.email
    this.user.id = ''
    // this.user = {
    //   id: '',
    //   email: '',
    //   name: '',
    //   tag: '',
    //   avatar: '',
    //   faculty: '',
    //   classname: ''
    // }
  }

  addMember() {
    for (let i = 0; i < this.contest.participants.length; i++) {
      if (this.contest.participants[i].email === this.user.email) {
        this.toastrService.info(
          `${this.user.email} đã tồn tại trong danh sách!`, '', { timeOut: 2000 }
        )
        return
      }
    }
    this.contest.participants.push({ email: this.user.email, joined: false })
    this.users.push(this.user)
    this.user = {
      id: '',
      email: '',
      name: '',
      tag: '',
      avatar: '',
      faculty: '',
      classname: ''
    }
    this.contestService.update(this.contest).subscribe(
      (data) => {
        this.toastrService.success(
          'Thêm thành công', '',
          { timeOut: 2000 }
        )
      },
      (error) => {
        this.toastrService.info(
          `Đã có lỗi xảy ra, hãy thử lại: ${error.status}`, '',
          { timeOut: 2000 }
        )
      }
    )
  }

  handYN(i: number): string {
    return this.contest.participants[i].joined ? "YES" : "NO"
  }

  //index 4

  accepted(index: number) {
    this.contest.signups[index].status = 'Accepted'
    this.contest.participants.push({ email: this.contest.signups[index].email, joined: false })
    this.toastrService.success(
      `Đã chấp nhập`, '', { timeOut: 2000 }
    )
  }

  refuse(index: number) {
    this.contest.signups[index].status = 'Refuse'
    this.toastrService.success(
      `Đã từ chối`, '', { timeOut: 2000 }
    )
  }

  acceptedAll() {
    for (let i = 0; i < this.contest.signups.length; i++) {
      if (this.contest.signups[i].status === 'signedUp') {
        this.contest.signups[i].status = 'Accepted'
        this.contest.participants.push({ email: this.contest.signups[i].email, joined: false })
      }
    }
    this.toastrService.success(
      `Đã chấp nhập tất cả`, '', { timeOut: 2000 }
    )
  }

  refuseAll() {
    for (let i = 0; i < this.contest.signups.length; i++) {
      if (this.contest.signups[i].status === 'signedUp')
        this.contest.signups[i].status = 'Refuse'
    }
    this.toastrService.success(
      `Đã từ chối tất cả`, '', { timeOut: 2000 }
    )
  }

  // index 2
  sendNotification() {
    this.websocketService.sendMessage('/app/notify', { id: this.contest.id, content: this.content })
  }

}
