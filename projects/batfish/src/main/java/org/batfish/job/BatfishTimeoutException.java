package org.batfish.job;

import org.batfish.common.BatfishException;

public class BatfishTimeoutException extends BatfishException {

  public BatfishTimeoutException(String msg) {
    super(msg);
  }

  /** */
  private static final long serialVersionUID = 1L;
}
