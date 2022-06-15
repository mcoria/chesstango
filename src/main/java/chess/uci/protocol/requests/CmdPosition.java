package chess.uci.protocol.requests;

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
			sb.append("fen " + fen);
		}

		if(moves != null && moves.size() > 0){
			sb.append(" moves");
			for (String move: moves){
				sb.append(" ");
				sb.append(move);
			}
		}

		return sb.toString();
	}
}
