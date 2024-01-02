package com.tdt.shop.components;

import com.tdt.shop.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
  @Value("${jwt.expiration}")
  private long expiration;   // save to an environment variable
  @Value("${jwt.secretKey}")
  private String secretKey;

  public String generateToken (User user) throws Exception {
    // properties => claims
    Map<String, Object> claims = new HashMap<>();
//    this.generateSecretKey();
    claims.put("phoneNumber", user.getPhoneNumber());
    try {
      String token = Jwts.builder()
        .setClaims(claims)      // Đặt thông tin (claims) vào token
        .setSubject(user.getPhoneNumber())    // Đặt chủ thể của token là phoneNumber
        .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // đặt thời điểm hết hạn của token
        .signWith(getSignKey(), SignatureAlgorithm.HS256)   // Ký token bằng thuật toán HS256 (1 loại khóa đối xứng)
        .compact();
      return token;
    }
    catch (Exception e) {
      throw new InvalidParameterException("Cannot create jwt token"+ e.getMessage());
    }
  }

  private Key getSignKey () {
    byte[] bytes = Decoders.BASE64.decode(secretKey);   // Giải mã BASE64 chuỗi "secretKey" thành mảng byte
    return Keys.hmacShaKeyFor(bytes);   // Tạo khóa bí mật "Key" từ mảng byte đã giải mã
  }

  private String generateSecretKey () {
    SecureRandom random = new SecureRandom();
    byte[] keyBytes = new byte[32];   // 256-bit key
    random.nextBytes(keyBytes);
    String serectKey = Encoders.BASE64.encode(keyBytes);
    return serectKey;
  }

  private Claims extractAllClaims (String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  //check expiration
  private  boolean isTokenExpired (String token) {
    Date expirationDate = this.extractClaim(token, Claims::getExpiration);
    return expirationDate.before(new Date());
  }

  public String extractPhoneNumber (String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean validateToken (String token, UserDetails userDetails) {
    String phoneNumber = extractPhoneNumber(token);
    return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }
}
