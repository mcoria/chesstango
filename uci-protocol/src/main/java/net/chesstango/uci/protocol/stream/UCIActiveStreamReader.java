package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.CmdQuit;

/**
 * @author Mauricio Coria
 */
public class UCIActiveStreamReader {
    private boolean keepReading;
    protected UCIInputStream input;
    protected UCIOutputStream output;

    public void read() {
        UCIOutputStreamSwitch actionOutput = new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, this::stopReading);
        actionOutput.setOutputStream(output);
        keepReading = true;
        UCIMessage message;
        while (keepReading && (message = input.get()) != null) {
            actionOutput.accept(message);
        }
    }

    public void stopReading() {
        try {
            keepReading = false;
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

}
