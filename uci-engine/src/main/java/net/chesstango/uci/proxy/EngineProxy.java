package net.chesstango.uci.proxy;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.stream.UCIActiveStreamReader;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;
import net.chesstango.uci.protocol.stream.strings.StringSupplierLogger;
import net.chesstango.uci.service.Service;
import net.chesstango.uci.service.ServiceVisitor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EngineProxy implements Service {
    private final ProxyConfig config;
    private Process process;
    private InputStream inputStreamProcess;
    private PrintStream outputStreamProcess;
    private UCIOutputStream responseOutputStream;
    private UCIActiveStreamReader pipe;
    private Thread readingPipeThread;
    private boolean logging;


    /**
     * Para que Spike pueda leer sus settings, el working directory debe ser el del ejecutable.
     * Los settings generales para todos los engines se controlan desde EngineManagement -> UCI en Arena.
     */
    public EngineProxy(ProxyConfig config) {
        this.pipe = new UCIActiveStreamReader();
        this.config = config;
    }


    @Override
    public void accept(UCIMessage message) {
        if (outputStreamProcess == null) {
            waitProcessStart();
        }
        if (logging) {
            System.out.println("proxy >> " + message);
        }
        outputStreamProcess.println(message);
    }


    @Override
    public void open() {
        startProcess();
        readingPipeThread = new Thread(this::readFromPipe);
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

    public EngineProxy setLogging(boolean flag) {
        this.logging = flag;
        return this;
    }

    private void closeProcessIO() {
        try {
            outputStreamProcess.close();
            inputStreamProcess.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void startProcess() {
        List<String> commandAndArguments = new ArrayList<>();

        commandAndArguments.add(config.getExe());

        if (config.getParams() != null) {
            String[] parameters = config.getParams().split(" ");
            if(parameters.length > 0){
                commandAndArguments.addAll( Arrays.stream(parameters).toList() );
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

            Supplier<String> stringSupplier = new StringSupplier(new InputStreamReader(inputStreamProcess));

            if (logging) {
                stringSupplier = new StringSupplierLogger("proxy << ", stringSupplier);
            }

            pipe.setInputStream(new UCIInputStreamAdapter(stringSupplier));
            pipe.setOutputStream(responseOutputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopProcess() {
        if (logging) {
            System.out.println("proxy: EngineProxy::stopProcess() invoked");
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (logging) {
            System.out.println("proxy: EngineProxy::stopProcess() finished");
        }
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

    private void readFromPipe() {
        if (logging) {
            System.out.println("proxy: EngineProxy::readFromPipe(): start reading engine output");
        }
        pipe.run();
        if (logging) {
            System.out.println("proxy: EngineProxy::readFromPipe():end reading engine output");
        }
    }


    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }
}
