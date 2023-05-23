package net.chesstango.uci.protocol.responses;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 */
public class RspReadyOk implements UCIResponse {


    @Override
    public MessageType getMessageType() {
        return MessageType.Response;
    }

    @Override
    public void execute(UCIGui executor) {
        executor.do_readyOk(this);
    }

    @Override
    public UCIResponseType getResponseType() {
        return UCIResponseType.READYOK;
    }


    @Override
    public String toString() {
        return "readyok";
    }

}
