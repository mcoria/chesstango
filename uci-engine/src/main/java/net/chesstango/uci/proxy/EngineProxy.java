package net.chesstango.uci.proxy;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.stream.UCIActivePipe;
import net.chesstango.uci.protocol.stream.UCIInputStreamAdapter;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.strings.StringSupplier;
import net.chesstango.uci.protocol.stream.strings.StringSupplierLogger;
import net.chesstango.uci.service.UCIService;

import java.io.*;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EngineProxy implements UCIService {

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private UCIOutputStream responseOutputStream;

    private UCIActivePipe pipe;

    private ProcessBuilder processBuilder;
    private Process process;

    private Thread processingThread;
    private boolean logging;


    /**
     * Para que Spike pueda leer sus settings, el working directory debe ser el del ejecutable.
     * Los settings generales para todos los engines se controlan desde EngineManagement -> UCI en Arena.
     */
    public EngineProxy() {
        processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");
        processBuilder.directory(new File("C:\\Java\\projects\\chess\\chess-utils\\arena_3.5.1\\Engines\\Spike"));
        pipe = new UCIActivePipe();
    }

    public void activate() {
        startProcess();

        pipe.activate();

        stopProcess();
    }

    protected void startProcess() {
        try {
            synchronized (processBuilder) {
                process = processBuilder.start();

                inputStreamProcess = process.getInputStream();
                outputStreamProcess = new PrintStream(process.getOutputStream(), true);
            }

            Supplier<String> stringSupplier = new StringSupplier(new InputStreamReader(inputStreamProcess));
            if(logging) {
                stringSupplier = new StringSupplierLogger("proxy << ", stringSupplier);
            }

            pipe.setInputStream(new UCIInputStreamAdapter( stringSupplier ));
            pipe.setOutputStream(responseOutputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void stopProcess() {
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(UCIMessage message) {
        if (outputStreamProcess == null) {
            int counter = 0;
            try {
                do {
                    counter++;
                    synchronized (processBuilder) {
                        processBuilder.wait(100);
                    }
                } while (outputStreamProcess == null && counter < 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (outputStreamProcess == null) {
                throw new RuntimeException("Process has not started yet");
            }
        }
        if(logging) {
            System.out.println("proxy >> " + message);
        }
        outputStreamProcess.println(message);
    }

    @Override
    public void open() {
        processingThread = new Thread(this::activate);
        processingThread.start();
    }

    @Override
    public void close() {
        outputStreamProcess.close();
        try {
            processingThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }

    public EngineProxy setLogging(boolean flag){
        this.logging = flag;
        return this;
    }
}
