package chess.uci.engine;

import chess.uci.protocol.*;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdIsReady;
import chess.uci.protocol.requests.CmdUci;

import java.io.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Mauricio Coria
 *
 */
public class EngineProxy extends EngineAbstract {

    private Process process;

    private InputStream inputStreamProcess;

    private PrintStream outputStreamProcess;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void main() {
        this.keepProcessing = true;

        startProcess();

        executor.execute(this::readFromProcess);

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
    public void do_go(CmdGo cmdGo) {
        outputStreamProcess.println(cmdGo);
    }

    @Override
    public void do_position_fen(String fen, List<String> moves) {

    }

    @Override
    public void do_position_startpos(List<String> moves) {

    }

    @Override
    public void do_quit() {

    }

    @Override
    public void do_setOptions() {

    }

    @Override
    public void do_stop() {

    }

    @Override
    public void do_newGame() {

    }

    public void readFromProcess() {
        UCIDecoder uciDecoder = new UCIDecoder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamProcess));
        try {
            while (keepProcessing) {
                UCIMessage message = uciDecoder.parseMessage(reader.readLine());
                output.write(message);
            }
        } catch (IOException io){
            throw new RuntimeException(io);
        }
        System.out.println("Dejamos de leer del proceso");
    }

    protected boolean startProcess(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");

            process = processBuilder.start();

            inputStreamProcess = process.getInputStream();

            outputStreamProcess = new PrintStream(process.getOutputStream(), true);

            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    protected void stopProcess() {
        try {
            process.destroyForcibly();
            process.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

}
