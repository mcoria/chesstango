package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIRequest;

public class UCIActivePipe {

    protected boolean keepProcessing;

    protected UCIInputStream input;

    protected UCIOutputStream output;

    public void activate() {
        while (keepProcessing) {
            UCIMessage message = input.read();
            output.write(message);
        }
    }

    public void deactivate() {
        keepProcessing = false;
    }


    public void setInputStream(UCIInputStream input) {
        this.input = input;
    }

    public void setOutputStream(UCIOutputStream output) {
        this.output = output;
    }
}
