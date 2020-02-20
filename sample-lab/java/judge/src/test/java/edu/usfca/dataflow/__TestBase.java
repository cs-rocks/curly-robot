package edu.usfca.dataflow;

import org.junit.Rule;
import org.junit.rules.Timeout;

public class __TestBase {
  // Note: This rule is used to stop the grading system from going down when your code gets stuck (safety measure).
  // If you think this prevents your submission from getting graded normally, ask on Piazza.
  @Rule
  public Timeout timeout = Timeout.millis(1500);
}
