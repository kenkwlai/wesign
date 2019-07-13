package org.wesignproject.utils;

import com.fasterxml.uuid.Generators;


/**
 * Util class generating random-time-based id
 */
public class UUID {
  /**
   * Generate Id on a random-time-based approach
   * @return id
   */
  public String randomId() {
    return String.format("%s-%s",
        Generators.randomBasedGenerator().generate(),
        Generators.timeBasedGenerator().generate());
  }
}
