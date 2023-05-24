package net.chesstango.uci.protocol.responses;

import net.chesstango.uci.protocol.UCIGui;
import net.chesstango.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 */
public class RspInfo implements UCIResponse {

    private final String info;

    public RspInfo(String info){
        this.info = info;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.Response;
    }

    @Override
    public UCIResponseType getResponseType() {
        return UCIResponseType.INFO;
    }

    @Override
    public void execute(UCIGui executor) {
        executor.do_info(this);
    }

    @Override
    public String toString() {
        return "info " + info;
    }
}
