package net.chesstango.uci.protocol.requests;

import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 */
public class CmdGo implements UCIRequest {

    public enum GoType {NO_SUBCOMMAND, INFINITE, DEPTH};

    private GoType type;

    private int depth;

    public CmdGo() {
        this.type = GoType.NO_SUBCOMMAND;
        this.depth = 0;
    }

    public GoType getGoType() {
        return type;
    }

    public int getDepth() {
        return depth;
    }


    public CmdGo setGoType(GoType type) {
        this.type = type;
        return this;
    }

    public CmdGo setDepth(int depth) {
        setGoType(GoType.DEPTH);
        this.depth = depth;
        return this;
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
        }
        throw new RuntimeException("Invalid go command");
    }
}
