package org.movie.reviewer.domain.user.exception;

import java.text.MessageFormat;
import org.movie.reviewer.global.exception.ErrorMessage;

public class PasswordValidFailException extends RuntimeException {

  public PasswordValidFailException(ErrorMessage errorMessage) {
    super(MessageFormat.format(errorMessage.getMessage(), null));
  }
}
