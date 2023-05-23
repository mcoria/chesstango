package net.chesstango.uci.protocol.responses;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 */
public class RspUciOk implements UCIResponse {

    @Override
    public MessageType getMessageType() {
        return MessageType.Response;
    }

    @Override
    public UCIResponseType getResponseType() {
        return UCIResponseType.UCIOK;
    }

    @Override
    public void execute(UCIGui executor) {
        executor.do_uciOk(this);
    }

    @Override
    public String toString() {
        return "uciok";
    }

}
