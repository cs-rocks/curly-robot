package edu.usfca.dataflow;


import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class Watchdog extends TestWatcher {

    @Override
    protected void starting(Description desc) {
    //nothing to do here
    }

    @Override
    protected void failed(Throwable e, Description description) {
        String exceptionLogs;
        exceptionLogs = description + "\n";
        writeToFile("Failed : " + exceptionLogs + "\n", "status");
        if(e.getMessage() != null){
            exceptionLogs += e.getMessage() + "\n";
        }
        exceptionLogs += e.getClass().getName() + "\n";
        List<String> exceptions = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList());
        exceptionLogs += "EXCEPTION: " +String.join("\t\n", exceptions) + "\n\n";
        writeToFile(exceptionLogs, "errors");
    }

    @Override
    protected void succeeded(Description description) {
        String succeededLogs;
        succeededLogs = "Passed : " + description + "\n\n";
        writeToFile(succeededLogs, "status");
    }

    private void writeToFile(String logs, String fileName) {
        try {
            File file = new File(fileName);
            CharSink chs = Files.asCharSink(
                    file, Charsets.UTF_8, FileWriteMode.APPEND);
            chs.write(logs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fails() {
        //This function can be called from the fail function
        //Can be used to extend functionality
    }

    @Test
    public void succeeds() {
    }
};
