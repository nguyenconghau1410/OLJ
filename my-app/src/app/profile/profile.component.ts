import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../api/user/user.service';
import { User } from '../models/user.model';
import { FormsModule, NgForm } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CheckStatusService } from '../service/check/check-status.service';
import { SubmissionService } from '../api/submission/submission.service';
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [NzIconModule, CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  showEditForm = false
  result: string | null = null
  user!: User | null;
  total!: number
  error: boolean = false
  figure: any
  constructor(
    private userService: UserService,
    private submissionService: SubmissionService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    this.userService.getUser().subscribe(
      (data) => {
        if (data != null) {
          this.user = data
        }
      },
      (error) => {
        this.error = true
      }
    )
    this.submissionService.countMySubmission().subscribe(
      (data) => {
        if (data) {
          this.total = data['total'];
        }
      },
      (error) => {
        this.error = true
      }
    )
    this.submissionService.getFigure().subscribe(
      (data) => {
        this.figure = data
      }
    )
  }

  openForm() {
    this.showEditForm = true
  }

  closeForm() {
    this.showEditForm = false
  }

  onSubmit(form: NgForm) {
    this.userService.updateUser(form.value).subscribe(
      (data) => {
        this.toastr.success('Lưu thành công', '', {
          timeOut: 1000,
        })
        this.showEditForm = false
        this.user!.name = form.value.name
        this.user!.faculty = form.value.faculty
        this.user!.classname = form.value.classname
      },
      (error) => {
        this.toastr.error(
          'Đã có lỗi xảy ra, hãy thử lại!', '', { timeOut: 2000 }
        )
      }
    )
  }

}
