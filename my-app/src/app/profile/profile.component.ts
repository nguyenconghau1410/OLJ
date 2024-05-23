import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { ActivatedRoute } from '@angular/router';
import { MySubmissionComponent } from './my-submission/my-submission.component';
import { UserService } from '../api/user/user.service';
import { User } from '../models/user.model';
import { FormsModule, NgForm } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CheckStatusService } from '../service/check/check-status.service';
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [NzIconModule, CommonModule, MySubmissionComponent, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  showEditForm = false
  result: string | null = null
  user!: User | null;
  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private toastr: ToastrService,
    private status: CheckStatusService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.result = params['result']
      console.log(this.result)
    })
    this.userService.getUser().subscribe(
      (data) => {
        if (data != null) {
          this.user = data
        }
      },
      (error) => {
        this.status.checkStatusCode(error.status)
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
        this.status.checkStatusCode(error.status)
      }
    )
  }

}
