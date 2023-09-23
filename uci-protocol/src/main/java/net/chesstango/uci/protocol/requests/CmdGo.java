package net.chesstango.uci.protocol.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmdGo implements UCIRequest {

    public enum GoType {
        NO_SUBCOMMAND,
        INFINITE,
        DEPTH,
        MOVE_BYCLOCK,
        MOVE_TIME
    }

    private GoType type;

    private int depth;

    private int timeOut;

    private int wTime;

    private int wInc;

    private int bTime;

    private int bInc;

    public CmdGo() {
        this.type = GoType.NO_SUBCOMMAND;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.Request;
    }

    @Override
    public UCIRequestType getRequestType() {
        return UCIRequestType.GO;
    }

    @Override
    public void execute(UCIEngine executor) {
        executor.do_go(this);
    }

    @Override
    public String toString() {
        if (GoType.NO_SUBCOMMAND.equals(type)) {
            return "go";
        } else if (GoType.INFINITE.equals(type)) {
            return "go infinite";
        } else if (GoType.DEPTH.equals(type)) {
            return "go depth " + depth;
        } else if (GoType.MOVE_TIME.equals(type)) {
            return "go movetime " + timeOut;
        } else if (GoType.MOVE_BYCLOCK.equals(type)) {
            return String.format("go wtime %d btime %d winc %d binc %d", wTime, bTime, wInc, bInc);
        }
        throw new RuntimeException("Invalid go command");
    }
}
