import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';
import { TokenService } from './service/token/token.service';
import { UserService } from './api/user/user.service';
import { WebsocketService } from './service/websocket/websocket.service';
import { DataService } from './service/data/data.service';
import { User } from './models/user.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  isLoggedIn = false
  name!: String | null
  user!: User
  message: { title: string, content: string } = {
    title: '',
    content: ''
  }

  openForm: boolean = false
  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private websocketService: WebsocketService,
    private dataService: DataService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.tokenService.accessToken.subscribe(
      token => {
        if (token !== null) {
          this.isLoggedIn = true
          this.userService.getUser().subscribe(
            (data) => {
              if (data != null) {
                this.name! = data.name
                this.user = data
                this.dataService.setUserSubject(data)


                let destination = `/user/${this.user.email}/queue/notifications`
                this.websocketService.subscribe(destination, (frame: any) => {
                  const decoder = new TextDecoder('utf-8');
                  const messageText = decoder.decode(frame.binaryBody);
                  const message = JSON.parse(messageText);

                  if (message) {
                    this.openForm = true
                    this.message = message
                  }
                })
              }
            }
          )
        }
      }
    )
  }

  closeForm() {
    this.openForm = false
  }

  signOut(): void {
    this.tokenService.clearAccessToken()
    this.isLoggedIn = false
    this.router.navigate(["/login"])
  }

}
