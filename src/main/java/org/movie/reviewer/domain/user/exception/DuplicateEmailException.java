package org.movie.reviewer.domain.user.exception;

import java.text.MessageFormat;
import org.movie.reviewer.global.exception.ErrorMessage;

public class DuplicateEmailException extends RuntimeException {

  public DuplicateEmailException(ErrorMessage errorMessage, String input) {
    super(MessageFormat.format(errorMessage.getMessage(), input));
  }
}
