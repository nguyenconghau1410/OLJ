import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzPaginationModule } from 'ng-zorro-antd/pagination';
import { Problem } from '../models/problem.model';
import { ProblemService } from '../api/problem/problem.service';
import { ToastrService } from 'ngx-toastr';
import { timeout } from 'rxjs';
@Component({
  selector: 'app-problems',
  standalone: true,
  imports: [NzIconModule, CommonModule, NzPaginationModule],
  templateUrl: './problems.component.html',
  styleUrl: './problems.component.scss'
})
export class ProblemsComponent {
  problems: Problem[] = [];

  constructor(
    private problemService: ProblemService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.problemService.getAllProblem().subscribe(
      (data) => {
        if (data) {
          this.problems = data
        }
      },
      (error) => {
        if (error.status !== 401) {
          this.toastr.error(
            `Đã có lỗi xẩy ra ${error.status}`, '', { timeOut: 2000 }
          )
        }
      }
    )

  }
}
