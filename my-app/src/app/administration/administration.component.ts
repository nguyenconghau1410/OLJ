import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { ProblemService } from '../api/problem/problem.service';
import { CheckStatusService } from '../service/check/check-status.service';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { DataService } from '../service/data/data.service';

@Component({
  selector: 'app-administration',
  standalone: true,
  imports: [MatIconModule, CommonModule],
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.scss'
})
export class AdministrationComponent {
  problems: { id: string, title: string, state: string }[] = []
  check: boolean = true
  constructor(
    private router: Router,
    private problemService: ProblemService,
    private status: CheckStatusService,
    private toastrService: ToastrService,
    private dataService: DataService
  ) { }

  ngOnInit() {
    this.dataService.user?.subscribe(
      user => {
        if (user.role!.code === 'STUDENT') {
          this.check = false
          return
        }
      }
    )
    if (!this.check) return
    this.problemService.getProblemByCreator().subscribe(
      (data) => {
        if (data) {
          this.problems = data
        }
      },
      (error) => {
        this.status.checkStatusCode(error.status)
      }
    )
  }

  createNew() {
    this.router.navigate(['administration', 'create-problem'])
  }

  edit(id: string) {
    this.router.navigate(['administration', 'edit-problem', id])
  }

  updateState(id: string) {
    this.problemService.updateState(id).subscribe(
      () => {
        for (let i = 0; i < this.problems.length; i++) {
          if (this.problems[i].id === id) {
            this.problems[i].state = "PUBLIC"
            break
          }
        }
      },
      (error) => {
        this.toastrService.error(
          `Đã có lỗi xảy ra: ${error.status}`
        )
      }
    )
  }
}
