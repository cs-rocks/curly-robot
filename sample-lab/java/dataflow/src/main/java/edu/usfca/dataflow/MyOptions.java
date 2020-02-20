package edu.usfca.dataflow;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

/**
 * In future projects, we will use these options to configure our Dataflow Pipelines before uploading to GCP.
 *
 * For now, in case you are not familiar with the Java Annotations, let's get you expose to this.
 */
public interface MyOptions extends DataflowPipelineOptions {
  @Description("DirectRunner will be used if true")
  @Default.String("cs686")
  String getCourse();

  // TODO: Add a setter for "course".
  void setCourse(String course);

  @Description("DirectRunner will be used if true")
  // TODO: Add an annotation here to fix unit test.
  @Default.Boolean(true)
  boolean getDirectRunner();

  void setDirectRunner(boolean value);

  @Description("Job")
  // TODO: Add an annotation here to fix unit test.
  @Default.String("my job")
  String getJob();

  void setJob(String value);

  @Description("Debug flag for development.")
  // TODO: Add an annotation here to fix unit test.
  @Default.Boolean(false)
  Boolean getDebug();

  void setDebug(Boolean value);
}
