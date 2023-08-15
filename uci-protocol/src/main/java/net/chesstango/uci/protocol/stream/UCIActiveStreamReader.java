package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.CmdQuit;

/**
 * @author Mauricio Coria
 */
public class UCIActiveStreamReader implements Runnable {
    protected volatile boolean keepReading;
    protected UCIInputStream input;
    protected UCIOutputStream output;

    @Override
    public void run() {
        UCIOutputStreamSwitch actionOutput = new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, this::stopReading);
        actionOutput.setOutputStream(output);

        keepReading = true;
        UCIMessage message = null;
        while (keepReading && (message = input.get()) != null) {
            actionOutput.accept(message);
        }
    }

    public void stopReading() {
        keepReading = false;
    }

    public void setInputStream(UCIInputStream input) {
        this.input = input;
    }

    public void setOutputStream(UCIOutputStream output) {
        this.output = output;
    }
}
