package com.tdt.shop.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fullname", length = 100)
  private String fullName;

  @Column(name = "phone_number", length = 10, nullable = false)
  private String phoneNumber;

  @Column(name = "address", length = 200)
  private String address;

  @Column(name = "password", length = 200, nullable = false)
  private String password;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "facebook_account_id")
  private int facebookAccountId;

  @Column(name = "google_account_id")
  private int googleAccountId;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  // Phương thức này lấy ra các quyền (authorities) mà người dùng cụ thể có. (Bảng Roles)
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Để Roles tương thích với GrantedAuthority. Ta cần convert sang
    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();     // Lớp SimpleGrantedAuthority dùng để biểu diễn quyền người dùng
      // Thêm quyền vào danh sách. Với "ROLE_" là một quy ước trong Spring Security
    authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRole().getName().toUpperCase()));
//    authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
//    authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    return authorityList;
  }

  // Phương thức này trả về tên đăng nhập của người dùng
  @Override
  public String getUsername() {
    // Do ta đang sử dụng phoneNumber để làm tên đăng nhập nên sẽ return về phoneNumber
    return phoneNumber;
  }

  // Phương thức này xác định xem tài khoản người dùng có hợp lệ (không hết hạn) hay không
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // Phương thức này xác định xem tài khoản người dùng có bị khóa hay không
  @Override
  public boolean isAccountNonLocked() {
    // Non Locked : -> true = không khóa
    return true;
  }

  // Phương thức này xác định xem thông tin xác thực (credentials) của người dùng có hợp lệ (không hết hạn) hay không
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // Phương thức này xác định xem tài khoản người dùng có được kích hoạt hay không
  @Override
  public boolean isEnabled() {
    return true;
  }
}
