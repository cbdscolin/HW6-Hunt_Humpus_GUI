package mazeexceptions;

/**
 * Class of exception that cannot be recovered from.
 */
public class UnrecoverableException extends Exception {

  public UnrecoverableException(String message) {
    super(message);
  }
}
