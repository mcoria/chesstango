package chess.uci.engine;

import chess.uci.protocol.*;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;
import chess.uci.protocol.stream.UCIActivePipe;
import chess.uci.protocol.stream.UCIInputStream;
import chess.uci.protocol.stream.UCIOutputStream;
import chess.uci.protocol.stream.UCIOutputStreamExecutor;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class EngineProxy implements Engine {

    private boolean keepProcessing;

    private Process process;

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UCIOutputStream output;

    @Override
    public void setOutputStream(UCIOutputStream output){
        this.output = output;
    }


    public void activate() {
        keepProcessing = true;

        startProcess();

        //super.mainReadRequestLoop();

        stopProcess();
    }

    public void readFromProcess() {
        UCIDecoder uciDecoder = new UCIDecoder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamProcess));
        try {
            while (keepProcessing) {
                String line = reader.readLine();
                if(line != null) {
                    UCIMessage message = uciDecoder.parseMessage(line);
                    //output.write(message);
                }
            }
        } catch (IOException io){
            throw new RuntimeException(io);
        }
        System.out.println("Bye");
    }

    protected void startProcess(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");

            process = processBuilder.start();

            inputStreamProcess = process.getInputStream();

            outputStreamProcess = new PrintStream(process.getOutputStream(), true);

            executorService.execute(this::readFromProcess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void stopProcess() {
        try {
            outputStreamProcess.close();
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(UCIMessage message) {

    }

    @Override
    public void close() throws IOException {

    }
}
