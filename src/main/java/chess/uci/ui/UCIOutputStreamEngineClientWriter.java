package chess.uci.ui;

import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.stream.UCIOutputStream;
import chess.uci.protocol.UCIResponse;

public class UCIOutputStreamEngineClientWriter implements UCIOutputStream {

    private final EngineClientResponseListener listener;

    public UCIOutputStreamEngineClientWriter(EngineClientResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void write(UCIMessage message) {
        System.out.println(message);
        if(message instanceof UCIResponse){
            UCIResponse response = (UCIResponse) message;
            response.execute(listener);
        }
    }
}
