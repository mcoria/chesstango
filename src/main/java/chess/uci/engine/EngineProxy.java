package chess.uci.engine;

import chess.uci.protocol.*;
import chess.uci.protocol.stream.UCIActivePipe;
import chess.uci.protocol.stream.UCIInputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStream;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class EngineProxy implements Engine, Runnable {
    private Process process;

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private UCIOutputStream responseOutputStream;

    private UCIActivePipe pipe;

    public void activate() {
        startProcess();

        pipe.activate();

        stopProcess();
    }

    protected void startProcess(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");

            process = processBuilder.start();

            inputStreamProcess = process.getInputStream();

            outputStreamProcess = new PrintStream(process.getOutputStream(), true);

            pipe = new UCIActivePipe();
            this.pipe.setInputStream(new UCIInputStreamAdapter(new InputStreamReader(inputStreamProcess)));
            this.pipe.setOutputStream(responseOutputStream);

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
    public void write(UCIMessage message) {
        outputStreamProcess.println(message);
    }

    @Override
    public void close() throws IOException {
        outputStreamProcess.close();
    }

    @Override
    public void setResponseOutputStream(UCIOutputStream output){
        this.responseOutputStream = output;
    }

    @Override
    public void run() {
        activate();
    }
}
