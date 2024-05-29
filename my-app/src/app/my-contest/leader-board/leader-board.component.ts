import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ContestService } from '../../api/contest/contest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-leader-board',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './leader-board.component.html',
  styleUrl: './leader-board.component.scss'
})
export class LeaderBoardComponent {
  leaderBoardList: any[] = []
  contestId: string = ''
  error = ''
  constructor(
    private contestService: ContestService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    if (!sessionStorage.getItem('access_token')) {
      this.router.navigate(['login'])
      return
    }
    this.activatedRoute.params.subscribe(params => {
      if (params['id']) {
        this.contestId = params['id']
        this.contestService.getLeaderBoardList(params['id']).subscribe(
          (data) => {
            if (data) {
              this.leaderBoardList = data
            }
          },
          (error) => {
            this.error = "Error"
          }
        )
      }
    })
  }

  redirect(userId: string) {
    this.router.navigate(['contests', this.contestId, 'leaderboard', userId])
  }
}
