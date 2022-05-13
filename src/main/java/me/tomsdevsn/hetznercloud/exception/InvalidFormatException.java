package me.tomsdevsn.hetznercloud.exception;

/**
 * The Exception will be called, if an invalid format has been used for the volume.
 */
public class InvalidFormatException extends RuntimeException {

  private static final long serialVersionUID = 51574845136874L;

  public InvalidFormatException(String message) {
    super(message);
  }
}