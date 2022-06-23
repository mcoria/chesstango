package chess.uci.engine.imp;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.stream.UCIActivePipe;
import chess.uci.protocol.stream.UCIInputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStream;
import chess.uci.protocol.stream.strings.StringSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class EngineProxy implements Engine {

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private UCIOutputStream responseOutputStream;

    private UCIActivePipe pipe;

    private ProcessBuilder processBuilder;
    private Process process;

    private Thread processingThread;

    public EngineProxy() {
        processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");
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

            pipe.setInputStream(new UCIInputStreamAdapter(new StringSupplier(new InputStreamReader(inputStreamProcess))));
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
        outputStreamProcess.println(message);
        System.out.println(">>" + message);
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


}
