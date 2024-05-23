import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-my-submission',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-submission.component.html',
  styleUrl: './my-submission.component.scss'
})
export class MySubmissionComponent {
  numbers: number[] = []

  ngOnInit(): void {
    for (let i = 1; i <= 20; i++) {
      this.numbers.push(i);
    }
  }
}
