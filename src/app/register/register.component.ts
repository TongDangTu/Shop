import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { RegisterDTO } from '../dtos/register.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;

  // Khai báo các biến tương ứng với các trường dữ liệu trong form
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor (private router: Router, private userService: UserService) {
    // this.phone = "";
    // this.password = "";
    // this.retypePassword = "";
    // this.fullName = "";
    // this.address = "";
    // this.isAccepted = false;
    // this.dateOfBirth = new Date();
    this.phone = "33445566";
    this.password = "123456";
    this.retypePassword = "123456";
    this.fullName = "nguyen van test";
    this.address = "";
    this.isAccepted = true;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear()-18);
  }

  onPhoneChange () {
    console.log(`Phone number: ${this.phone}`);
  }

  onPasswordChange () {
    console.log(`Password: ${this.password}`);
  }

  register () {
    // const message = `Phone number: ${this.phone}\n`
    //               + `Password: ${this.password}\n`
    //               + `Retype Password: ${this.retypePassword}\n`
    //               + `FullName: ${this.fullName}\n`
    //               + `Address: ${this.address}\n`
    //               + `Date Of Birth: ${this.dateOfBirth}`;
    // alert(`You pressed register. ${message}`);
    const registerDTO:RegisterDTO = new RegisterDTO (
      this.fullName,
      this.phonefullName,
      this.addressfullName,
      this.passwordfullName,
      this.retypePasswordfullName,
      this.dateOfBirthfullName,
      0,
      0,
      1
    )
  
    this.userService.register(registerData).subscribe({
      next: (response: any) => {
        debugger;
        // Xử lý kết quả trả về khi đăng ký thành công
        this.router.navigate(['/login']);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        // Xử lý lỗi nếu có
        alert(`Cannot register, error: ${error.error}`);
      } 
    });
  }

  // 
  checkPasswordMatch () {
    const retypePasswordControl = this.registerForm.controls['retypePassword'];

    if (retypePasswordControl) {
      if (this.password !== this.retypePassword) {
        this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true });
      }
      else {
        this.registerForm.form.controls['retypePassword'].setErrors(null);
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
        this.registerForm.form.controls['dateOfBirth'].setErrors({ 'invalidAge': true });
      }
      else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}