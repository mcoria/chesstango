package net.chesstango.uci.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.stream.UCIActiveStreamReader;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;
import net.chesstango.uci.protocol.stream.strings.StringActionSupplier;
import net.chesstango.uci.Service;
import net.chesstango.uci.ServiceVisitor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class UciProxy implements Service {
    private static final Logger logger = LoggerFactory.getLogger(UciProxy.class);
    private final ProxyConfig config;
    private Process process;
    private InputStream inputStreamProcess;
    private PrintStream outputStreamProcess;
    private UCIOutputStream responseOutputStream;
    private UCIActiveStreamReader pipe;
    private Thread readingPipeThread;


    /**
     * Para que Spike pueda leer sus settings, el working directory debe ser el del ejecutable.
     * Los settings generales para todos los engines se controlan desde EngineManagement -> UCI en Arena.
     */
    public UciProxy(ProxyConfig config) {
        this.pipe = new UCIActiveStreamReader();
        this.config = config;
    }


    @Override
    public void accept(UCIMessage message) {
        if (outputStreamProcess == null) {
            waitProcessStart();
        }

        logger.trace("proxy >> {}", message);

        outputStreamProcess.println(message);
    }


    @Override
    public void open() {
        startProcess();
        readingPipeThread = new Thread(this::readFromProcess);
        readingPipeThread.start();
    }

    @Override
    public void close() {
        pipe.stopReading();

        closeProcessIO();

        try {
            readingPipeThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        stopProcess();
    }

    @Override
    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }

    private void closeProcessIO() {
        try {
            outputStreamProcess.close();
            inputStreamProcess.close();
        } catch (IOException e) {
            logger.error("Error:", e);
        }
    }

    private void startProcess() {
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

        Supplier<String> stringSupplier = new StringSupplier(new InputStreamReader(inputStreamProcess));

        stringSupplier = new StringActionSupplier(stringSupplier, line -> logger.trace("proxy << {}", line));

        pipe.setInputStream(new UCIInputStreamAdapter(stringSupplier));
        pipe.setOutputStream(responseOutputStream);
    }

    private void stopProcess() {
        logger.debug("stopProcess() invoked");

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.debug("stopProcess() finished");
    }

    private void waitProcessStart() {
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

    private void readFromProcess() {
        logger.debug("readFromPipe(): start reading engine output");
        pipe.run();
        logger.debug("readFromPipe():end reading engine output");
    }


    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }
}
