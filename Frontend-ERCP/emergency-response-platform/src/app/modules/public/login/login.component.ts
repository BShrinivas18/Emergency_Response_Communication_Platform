
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { IncidentManagementService } from '../../../core/services/incident-management.service';
import { ResponderService } from '../../../core/services/responder.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError: string = '';
  
  // Statistical placeholders for emergency response platform
  emergencyStats = [
    { label: 'Total Incidents This Year', value: '12,547' },
    { label: 'Average Response Time', value: '8.3 mins' },
    { label: 'Lives Saved', value: '2,345' }
  ];

  featuredServices = [
    {
      title: 'Rapid Dispatch',
      description: 'Advanced routing and real-time resource allocation',
      icon: 'flash_on'
    },
    {
      title: 'Incident Tracking',
      description: 'Comprehensive monitoring and reporting system',
      icon: 'track_changes'
    },
    {
      title: 'Resource Management',
      description: 'Optimize personnel and equipment deployment',
      icon: 'people'
    }
  ];

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService,
    private router: Router,
    private http: HttpClient,
    private incidentManagementService: IncidentManagementService,
    private responderservice: ResponderService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['']
    });
  }

  // onSubmit() {
  //   const loginRequest = {
  //    username: this.loginForm.get('username')!.value,
  //    password: this.loginForm.get('password')!.value, 
  //     role: this.loginForm.get('role')!.value
  //   };

  //   // const { username, password, role } = loginRequest;
  //   if (this.loginForm.valid) {
  //     this.authService.login(this.loginForm.value).subscribe({
  //       next: (token) => {
  //         console.log('Login successful', token);
  //         this.authService.saveToken(token.JwtToken);


          
  //         this.incidentManagementService.getUserId().subscribe({
  //           next: (userId) => {
  //             if(userId!==null){
  //               sessionStorage.setItem('userId', userId.toString());
              
  //             console.log('User ID:', userId);
  //             if(loginRequest.role ==="ADMIN"){
  //               this.router.navigate(['/admin-dashboard']);
  //             }
  //             else {
  //               this.router.navigate(['/responder-dashboard'], {queryParams:{userId:userId}});
  //             }
  //           }
  //           },
  //           error: (err) => {
  //             console.error('Error retrieving user ID:', err);
  //           }
  //         });
  //       },
       
  //       error: (err) => {
  //         this.loginError = 'Invalid username or password';
  //       }
        
  //     });
  //   }
  // }




  onSubmit() {

    if (this.loginForm.valid) {
      const { username, password, role } = this.loginForm.value;
  
      const loginRequest = {
        username,
        password,
        role
      };
      // console.log('Login request:', loginRequest);
      this.authService.login(loginRequest).subscribe({
        next: (response) => {
          console.log('Login response:', response);
          if (response && response.jwtToken) {
            this.authService.saveToken(response.jwtToken);

            // this.incidentManagementService.getUserId().subscribe({
            //   next: (userId) => {
            //     console.log('User ID:', userId);
            //     if (role === 'ADMIN' && response.userId != null) {
            //       sessionStorage.setItem('userId', response.userId.toString());
            //       this.router.navigate(['/admin-dashboard']);
            //     } else if (role === 'RESPONDER' && response.responderId != null) {
            //       sessionStorage.setItem('responderId', response.responderId.toString());
            //       this.router.navigate(['/responder-dashboard'], {
            //         queryParams: { responderId: response.responderId }
            //       });
            //     } else {
            //       this.loginError = 'Unexpected role or missing ID in response.';
            //     }
            //   },
            //   error: (err) => {
            //     console.error('Error retrieving user ID:', err);
            //   }
            // });

            //Save user ID or responder ID depending on the role
            if (role === 'ADMIN' && response.userId != null) {
              sessionStorage.setItem('userId', response.userId.toString());
              this.router.navigate(['/admin-dashboard']);
            } else if (role === 'RESPONDER' && response.responderId != null) {
              sessionStorage.setItem('responderId', response.responderId.toString());
              console.log('Responder ID:', response.responderId);
              this.responderservice.getResponderById(response.responderId).subscribe({
                next: (responder) => {
                  console.log('Responder data fetched:', responder);
                  console.log('Responder ID:', responder.responderId);
                  console.log('Responder Name:', responder.name);
                  console.log('Station Location:', responder.stationLocation);
                  console.log('Status:', responder.status);
                  console.log('Responder Type:', responder.type);
                }
              });  
              
              this.router.navigate(['/responder-dashboard'], {
                queryParams: { responderId: response.responderId }
              });
            } else {
              this.loginError = 'Unexpected role or missing ID in response.';
            }
          } else {
            this.loginError = 'Invalid response from server.';
          }
        },
        error: (err) => {
          console.error('Login failed:', err);
          this.loginError = 'Invalid username or password.';
        }
        
      });
    
  }
  
  

}
}


