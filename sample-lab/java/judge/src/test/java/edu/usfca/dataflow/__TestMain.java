package edu.usfca.dataflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

// NOTE: You should not modify this file, as the grading system will use this file as-is (and additional hidden tests)
// in order to grade your submission.
public class __TestMain extends __TestBase {
  @Test
  public void testGetUserEmail() {
    assertNotEquals("", Main.getUserEmail());
  }

  @Test
  public void testShortAnswer() {
    assertNotEquals("", Main.shortAnswer());
  }

  // ----------------
  @Test
  public void __shareable__testShortAnswer() {
    assertEquals(43, Main.shortAnswer().length());
  }

  // ----------------
  @Test
  public void __hidden__testShortAnswer() {
    assertEquals("Filtering out cs686 (that contains a digit)", Main.shortAnswer());
  }

  @Test
  public void __hidden__testUserEmail() {
    assertTrue(
        StringUtils.endsWith(Main.getUserEmail(), "@dons.usfca.edu") || "hlee84@usfca.edu".equals(Main.getUserEmail()));
  }
}
