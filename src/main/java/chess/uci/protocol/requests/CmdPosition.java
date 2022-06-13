package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.UCIRequest;

import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class CmdPosition implements UCIRequest {

	public enum CmdType {STARTPOS, FEN};

	private final CmdType type;

	private final String fen;

	private final List<String> moves;

	public CmdPosition(CmdType type, String fen, List<String> moves) {
		this.type = type;
		this.fen = fen;
		this.moves = moves;
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
	public void execute(UCIMessageExecutor executor) {
		executor.do_position(this);
	}

	public CmdType getType(){
		return type;
	}

	public String getFen() {
		return fen;
	}
	
	public List<String> getMoves(){
		return moves;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("position ");
		if(CmdType.STARTPOS.equals(type)){
			sb.append("startpos");
		} else{
			sb.append("fen");
		}

		if(moves.size() > 0){
			sb.append("moves");
			for (String move: moves){
				sb.append(" ");
				sb.append(move);
			}
		}

		return sb.toString();
	}
}
