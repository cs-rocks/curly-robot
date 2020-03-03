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
    private static String exceptionLogs;

    @Override
    protected void starting(Description desc) {
    //nothing to do here
        System.out.println("shazam!");
    }

    @Override
    protected void failed(Throwable e, Description description) {
        exceptionLogs = description + "\n";
        List<String> exceptions = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList());
        exceptionLogs += "EXCEPTION: " +String.join("\t\n", exceptions) + "\n\n";
        writeException();
    }

    @Override
    protected void succeeded(Description description) {
        //nothing to do here
    }

    public void writeException() {
        try {
            File file = new File("errors");
            CharSink chs = Files.asCharSink(
                    file, Charsets.UTF_8, FileWriteMode.APPEND);
            chs.write(exceptionLogs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fails() {
        //fail();
    }

    @Test
    public void succeeds() {
    }
};
