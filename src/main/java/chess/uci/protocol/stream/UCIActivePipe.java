package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.requests.CmdQuit;

import java.io.IOException;

public class UCIActivePipe {

    private boolean active;
    protected UCIInputStream input;

    protected UCIOutputStream output;

    public void activate() {
        UCIOutputStreamSwitch actionOut = new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, this::deactivate);
        actionOut.setOutputStream(output);
        active = true;
        UCIMessage message = null;
        while ( active && (message = input.read()) != null ) {
            actionOut.write(message);
        }
    }

    public void deactivate() {
        try {
            active = false;
            input.close();
            output.close();
        } catch (IOException e) {
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
