package org.movie.reviewer.domain.movie.exception;

import java.text.MessageFormat;
import org.movie.reviewer.global.exception.ErrorMessage;

public class NotFoundException extends RuntimeException {

  public NotFoundException(ErrorMessage errorMessage, Long id) {
    super(MessageFormat.format(errorMessage.getMessage(), id));
  }
}
