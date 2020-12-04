package mazeexceptions;

/**
 * Class of exception that can be recovered from.
 */
public class RecoverableException extends Exception {

  public RecoverableException(String message) {
    super(message);
  }
}
