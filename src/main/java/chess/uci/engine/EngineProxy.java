package chess.uci.engine;

import chess.uci.protocol.UCIInputStream;
import chess.uci.protocol.UCIOutputStream;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdIsReady;
import chess.uci.protocol.requests.CmdUci;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class EngineProxy extends EngineAbstract {

    private Process process;

    private InputStream inputProcess;

    private PrintStream outProcess;

    private UCIOutputStream output;
    private UCIInputStream input;


    public boolean startProcess(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Java\\projects\\chess-utils\\arena_3.5.1\\Engines\\Spike\\Spike1.4.exe");

            Process process = processBuilder.start();

            inputProcess = process.getInputStream();

            outProcess = new PrintStream(process.getOutputStream());

            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public void stopProcess() {
        try {
            process.destroyForcibly();
            process.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void main() {
        startProcess();
        super.main();
        stopProcess();
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
        outProcess.println(cmdUci);
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        outProcess.println(cmdIsReady);
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        outProcess.println(cmdGo);
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

    @Override
    public void setInputStream(UCIInputStream input) {
        this.input = input;
    }

    @Override
    public void setOutputStream(UCIOutputStream output) {
        this.output = output;
    }

}
