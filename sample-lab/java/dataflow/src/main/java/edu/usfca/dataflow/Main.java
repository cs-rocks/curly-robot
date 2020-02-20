package edu.usfca.dataflow;

import edu.usfca.dataflow.jobs.SampleJob;

/**
 * Follow the instructions on github.
 *
 * You'll find it easier to complete this lab if you make incremental progress as suggested.
 */
public class Main {
  // TODO: Make sure you change USER_EMAIL below to your @dons email address.
  final private static String USER_EMAIL = "hlee84@usfca.edu";

  static String getUserEmail() {
    return USER_EMAIL;
  }

  // Lab02 has only one short answer question:
  // In this lab, if you run this program (e.g., do "<..>/java $ gradle run -p dataflow"),
  // then you'll see some messages on the console coming from SampleJob.run().
  // Precisely, you will see 11-13 lines (most likely 13 lines).
  // Copy and paste the sixth line printed (1-based) to pass the hidden unit test.
  // (To be clear, the first line printed will be "--- Interestingly, ... Why?")
  public static String shortAnswer() {
    // TODO: Copy and paste the sixth line you got, which should be 43 characters long.
    return "Filtering out cs686 (that contains a digit)";
  }

  public static void main(String[] args) {
    SampleJob.run(new String[] {}).run().waitUntilFinish();
  }
}
