package org.movie.reviewer.global.exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

  public NotFoundException(ErrorMessage errorMessage, Long id) {
    super(MessageFormat.format(errorMessage.getMessage(), id));
  }

  public NotFoundException(ErrorMessage errorMessage, String email) {
    super(MessageFormat.format(errorMessage.getMessage(), email));
  }

}
