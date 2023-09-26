package net.chesstango.uci.protocol.requests;

import lombok.Getter;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 */
public class CmdSetOption implements UCIRequest {

    @Getter
    private final String id;

    @Getter
    private final String value;

    public CmdSetOption(String id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.Request;
    }

    @Override
    public UCIRequestType getRequestType() {
        return UCIRequestType.SETOPTION;
    }


    @Override
    public void execute(UCIEngine executor) {
        executor.do_setOption(this);
    }

    @Override
    public String toString() {
        return value == null ? String.format("setoption name %s", id) : String.format("setoption name %s value %s", id, value);
    }
}
