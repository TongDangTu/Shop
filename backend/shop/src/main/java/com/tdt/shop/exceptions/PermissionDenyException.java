package com.tdt.shop.exceptions;

public class PermissionDenyException extends Exception {
  public PermissionDenyException(String message) {
    super(message);
  }
}
