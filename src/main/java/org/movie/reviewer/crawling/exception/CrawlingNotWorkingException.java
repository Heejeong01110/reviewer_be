package org.movie.reviewer.crawling.exception;

import java.text.MessageFormat;
import org.movie.reviewer.global.exception.ErrorMessage;

public class CrawlingNotWorkingException extends RuntimeException {

  public CrawlingNotWorkingException(ErrorMessage errorMessage) {
    super(MessageFormat.format(errorMessage.getMessage(), ""));
  }
}
