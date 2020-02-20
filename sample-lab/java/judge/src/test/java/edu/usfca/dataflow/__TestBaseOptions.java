package edu.usfca.dataflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class __TestBaseOptions extends __TestBase {

  static MyOptions options = PipelineOptionsFactory.create().as(MyOptions.class);

  @Test
  public void testDummy() {
    options.setProject("some gcp project");
    options.setJobName("some dataflow job name");
    assertEquals("some gcp project", options.getProject());
    assertEquals("some dataflow job name", options.getJobName());
  }

  @Test
  public void testCourse() {
    assertEquals("cs686", options.getCourse());
  }

  @Test
  public void testDirectRunner() {
    assertTrue(options.getDirectRunner());
  }

  @Test
  public void testJob() {
    assertFalse(StringUtils.isBlank(options.getJob()));
  }

  @Test
  public void testDebug() {
    assertFalse(options.getDebug());
  }

  // ------------------------------------------------------------

  @Test
  public void __shareable__testAllProperties() {
    assertEquals("cs686", options.getCourse());
    assertTrue(options.getDirectRunner());
    assertFalse(StringUtils.isBlank(options.getJob()));
    assertFalse(options.getDebug());
  }

  // ------------------------------------------------------------

  @Test
  public void __hidden__testAllProperties() {
    assertEquals("cs686", options.getCourse());
    assertTrue(options.getDirectRunner());
    assertFalse(StringUtils.isBlank(options.getJob()));
    assertFalse(options.getDebug());
  }
}
