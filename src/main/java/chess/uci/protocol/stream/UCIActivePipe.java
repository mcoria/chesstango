package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.IOException;

public class UCIActivePipe {

    private boolean active;
    protected UCIInputStream input;

    protected UCIOutputStream output;

    public void activate() {
        active = true;
        UCIMessage message = null;
        while ( active && (message = input.read()) != null ) {
            output.write(message);
        }
    }

    public void deactivate() {
        try {
            active = false;
            if(input != null) {
                input.close();
            }
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
