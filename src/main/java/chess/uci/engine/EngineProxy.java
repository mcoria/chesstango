package chess.uci.engine;

import chess.uci.protocol.*;
import chess.uci.protocol.requests.*;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class EngineProxy extends EngineAbstract {

    private Process process;

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void main() {
        keepProcessing = true;

        startProcess();

        super.main();

        stopProcess();
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
        outputStreamProcess.println(cmdUci);
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        outputStreamProcess.println(cmdIsReady);
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
        outputStreamProcess.println(cmdUciNewGame);
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
        outputStreamProcess.println(cmdSetOption);
    }

    @Override
    public void do_position_startpos(CmdPositionStart moves) {
        outputStreamProcess.println(moves);
    }

    @Override
    public void do_position_fen(CmdPositionFen cmdPositionFen) {
        outputStreamProcess.println(cmdPositionFen);
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        outputStreamProcess.println(cmdGo);
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
        outputStreamProcess.println(cmdStop);
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        keepProcessing = false;
        outputStreamProcess.println(cmdQuit);
    }

    public void readFromProcess() {
        UCIDecoder uciDecoder = new UCIDecoder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamProcess));
        try {
            while (keepProcessing) {
                String line = reader.readLine();
                if(line != null) {
                    UCIMessage message = uciDecoder.parseMessage(line);
                    output.write(message);
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

}
