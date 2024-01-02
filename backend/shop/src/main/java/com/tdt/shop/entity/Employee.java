package com.tdt.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Employee {
  @Id
  private int id;
  private String name;
  private String address;
  private int salary;
}


