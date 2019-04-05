package me.tomsdevsn.hetznercloud.exception;

/**
 * The Exception will be called, if you use an invalid format for the volume.
 */
public class InvalidFormatException extends RuntimeException {

  private static final long serialVersionUID = 51574845136874L;

  public InvalidFormatException(String message) {
    super(message);
  }
}