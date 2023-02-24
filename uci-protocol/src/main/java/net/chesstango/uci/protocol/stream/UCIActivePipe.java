package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.CmdQuit;

import java.io.IOException;

/**
 * @author Mauricio Coria
 */
public class UCIActivePipe implements Runnable {

    private boolean active;
    protected UCIInputStream input;
    protected UCIOutputStream output;

    public void activate() {
        UCIOutputStreamSwitch actionOutput = new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, this::deactivate);
        actionOutput.setOutputStream(output);
        active = true;
        UCIMessage message;
        while (active && (message = input.get()) != null) {
            actionOutput.accept(message);
        }
    }

    public void deactivate() {
        try {
            active = false;
            //input.close();
            //output.close();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public void setInputStream(UCIInputStream input) {
        this.input = input;
    }

    public void setOutputStream(UCIOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        activate();
    }
}
