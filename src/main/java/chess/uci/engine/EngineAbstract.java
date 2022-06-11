package chess.uci.engine;

import chess.uci.protocol.UCIInputStream;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIOutputStream;
import chess.uci.protocol.UCIRequest;

public abstract class EngineAbstract implements Engine {

    protected boolean keepProcessing;

    protected UCIInputStream input;

    protected UCIOutputStream output;

    @Override
    public void main() {
        while (keepProcessing) {
            UCIMessage message = input.read();
            if(message != null && message instanceof UCIRequest){
                UCIRequest request = (UCIRequest) message;
                request.execute(this);
            }
        }
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
