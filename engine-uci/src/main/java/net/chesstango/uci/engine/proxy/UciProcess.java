package net.chesstango.uci.engine.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
class UciProcess {
    private static final Logger logger = LoggerFactory.getLogger(UciProcess.class);
    private final ProxyConfig config;
    private Process process;

    InputStream inputStreamProcess;
    PrintStream outputStreamProcess;

    UciProcess(ProxyConfig config) {
        this.config = config;
    }

    void startProcess() {
        List<String> commandAndArguments = new ArrayList<>();

        commandAndArguments.add(config.getExe());

        if (config.getParams() != null) {
            String[] parameters = config.getParams().split(" ");
            if (parameters.length > 0) {
                commandAndArguments.addAll(Arrays.stream(parameters).toList());
            }
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commandAndArguments);
        processBuilder.directory(new File(config.getDirectory()));

        try {
            synchronized (this) {
                process = processBuilder.start();
                inputStreamProcess = process.getInputStream();
                outputStreamProcess = new PrintStream(process.getOutputStream(), true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void stopProcess() {
        logger.debug("stopProcess() invoked");

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.debug("stopProcess() finished");
    }

    void waitProcessStart() {
        int counter = 0;
        try {
            do {
                counter++;
                synchronized (this) {
                    this.wait(100);
                }
            } while (outputStreamProcess == null && counter < 10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (outputStreamProcess == null) {
            throw new RuntimeException("Process has not started yet");
        }
    }

    void closeProcessIO() {
        try {
            outputStreamProcess.close();
            inputStreamProcess.close();
        } catch (IOException e) {
            logger.error("Error:", e);
        }
    }
}
