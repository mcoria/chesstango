package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class CmdPositionFen implements UCIRequest {

	private final List<String> moves;
	
	private final String fen;
	

	public CmdPositionFen(String fen, List<String> moves) {
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
	public void execute(Engine engine) {
		engine.do_position_fen(fen, moves);
	}


	public String fen() {
		return fen;
	}
	
	public List<String> getMoves(){
		return moves;
	}

}
