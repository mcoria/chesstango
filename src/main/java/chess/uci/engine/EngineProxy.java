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

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private UCIOutputStream responseOutputStream;

    private UCIActivePipe pipe;

    private ProcessBuilder processBuilder;
    private Process process;

    public EngineProxy(){
        processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");
        pipe = new UCIActivePipe();
    }

    public void activate() {
        startProcess();

        pipe.activate();

        stopProcess();
    }

    protected void startProcess(){
        try {
            synchronized (processBuilder) {
                process = processBuilder.start();

                inputStreamProcess = process.getInputStream();
                outputStreamProcess = new PrintStream(process.getOutputStream(), true);
            }

            pipe.setInputStream(new UCIInputStreamAdapter(new InputStreamReader(inputStreamProcess)));
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
    public void write(UCIMessage message) {
        if(outputStreamProcess == null){
            int counter = 0;
            try {
                do {
                    counter++;
                    synchronized (processBuilder) {
                        processBuilder.wait(100);
                    }
                } while(outputStreamProcess == null && counter < 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(outputStreamProcess == null){
                throw new RuntimeException("Process has not started yet");
            }
        }
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
