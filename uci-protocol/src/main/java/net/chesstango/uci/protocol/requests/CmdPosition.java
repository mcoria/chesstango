package net.chesstango.uci.protocol.requests;

import lombok.Getter;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIRequest;

import java.util.List;

/**
 * @author Mauricio Coria
 */
@Getter
public class CmdPosition implements UCIRequest {

    public enum CmdType {STARTPOS, FEN};

    private final CmdType type;

    /**
     * FEN string tal cual se recibe
     */
    private final String fen;

    /**
     * La lista de movimientos tal cual se recibe
     */
    private final List<String> moves;

    public CmdPosition(String fen, List<String> moves) {
        this.type = CmdType.FEN;
        this.fen = fen;
        this.moves = moves;
    }

    public CmdPosition(List<String> moves) {
        this.type = CmdType.STARTPOS;
        this.fen = null;
        this.moves = moves;
    }

    public CmdPosition() {
        this.type = CmdType.STARTPOS;
        this.fen = null;
        this.moves = null;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.Request;
    }

    @Override
    public UCIRequestType getRequestType() {
        return UCIRequestType.POSITION;
    }


    @Override
    public void execute(UCIEngine executor) {
        executor.do_position(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("position ");
        if (CmdType.STARTPOS.equals(type)) {
            sb.append("startpos");
        } else {
            sb.append("fen ").append(fen);
        }

        if (moves != null && !moves.isEmpty()) {
            sb.append(" moves");
            for (String move : moves) {
                sb.append(" ");
                sb.append(move);
            }
        }

        return sb.toString();
    }
}
