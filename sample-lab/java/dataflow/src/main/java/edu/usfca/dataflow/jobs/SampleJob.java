package edu.usfca.dataflow.jobs;

import java.util.Arrays;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.ProcessFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.commons.lang3.StringUtils;

/**
 * You do not have to change this file, but you will need to understand what's being done.
 */
public class SampleJob {

  public static Pipeline run(String[] args) {
    // For now, don't worry about $args and $options.
    PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
    options.setRunner(DirectRunner.class);

    // Create a Pipeline object that represents our Dataflow Pipeline.
    Pipeline p = Pipeline.create(options);

    // Let's create a PCollection object from in-memory data (that contains three words as Strings).
    // Hint: https://beam.apache.org/documentation/programming-guide/#creating-a-pcollection
    PCollection<String> words =
        p.apply(Create.of(Arrays.asList("cs686", "hello", "world", "", " ", "   ", "\t", "\n", "phew!")))
            .setCoder(StringUtf8Coder.of());

    // Let's filter any word that contains a digit or any blank strings.
    // We can use lambda to express a simple filtering rule like this.
    // We'll later see more complex filters.
    PCollection<String> filtered = words.apply(Filter.by((ProcessFunction<String, Boolean>) input -> {
      if (StringUtils.isBlank(input)) {
        System.out.format("Filtering out 'blank' string\n");
        return false;
      }
      if (StringUtils.containsAny(input, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')) {
        System.out.format("Filtering out %s (that contains a digit)\n", input);
        return false;
      }
      return true;
    }));

    System.out.println(
        "--- Interestingly, this line will be printed out before any 'Filtering ...' messages from above. Why?");

    // Let's print to the console the result, for sanity check.
    filtered.apply(ParDo.of(new DoFn<String, Void>() {
      @Setup
      public void setup() {
        System.out.println("--- Starting to print out the contents. This is likely to be printed more than once. Why?");
      }

      @ProcessElement
      public void process(ProcessContext c) {
        System.out.format("Found in the final result: %s\n", c.element());
      }
    }));

    return p;
  }
}
