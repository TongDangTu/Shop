import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;

  // Khai báo các biến tương ứng với các trường dữ liệu trong form
  phoneNumber: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor (private router: Router, private userService: UserService) {
    this.phoneNumber = "";
    this.password = "";
    this.retypePassword = "";
    this.fullName = "";
    this.address = "";
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear()-18);
  }

  register () {
    const registerDTO:RegisterDTO = new RegisterDTO ({
      "fullname" : this.fullName,
      "phone_number" : this.phoneNumber,
      "address" : this.address,
      "password" : this.password,
      "retype_password" : this.retypePassword,
      "date_of_birth" : this.dateOfBirth,
      "facebook_account_id" : 0,
      "google_account_id" : 0,
      "role_id" : 1
    });
  
    this.userService.register(registerDTO).subscribe({
      next: (response: any) => {
        debugger;
        // Xử lý kết quả trả về khi đăng ký thành công
        alert("Đăng ký thành công");
        this.router.navigate(['/login']);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        // Xử lý lỗi nếu có
        alert(`Cannot register, error: ${error.error.message}`);
      } 
    });
  }

  // 
  checkPasswordMatch () {
    const retypePasswordControl = this.registerForm.controls['retype-password'];

    if (retypePasswordControl) {
      if (this.password !== this.retypePassword) {
        this.registerForm.form.controls['retype-password'].setErrors({ 'passwordMismatch': true });
      }
      else {
        this.registerForm.form.controls['retype-password'].setErrors(null);
      }
    }
  }

  checkAge () {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }

      if (age < 18) {
        this.registerForm.form.controls['date-of-birth'].setErrors({ 'invalidAge': true });
      }
      else {
        this.registerForm.form.controls['date-of-birth'].setErrors(null);
      }
    }
  }
}