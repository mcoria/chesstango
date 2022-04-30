/**
 * 
 */
package chess.uci.engine;

import java.util.List;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.SmartMinMax;
import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.fen.FENDecoder;
import chess.board.moves.Move;
import chess.board.moves.MovePromotion;
import chess.uci.protocol.UCIResponseChannel;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.uci.RspUci;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {
	private boolean keepProcessing = true;
	
	private final UCIResponseChannel responseChannel;

	private final BestMoveFinder bestMoveFinder;
	
	private Game game;
	
	public Engine(UCIResponseChannel responseChannel) {
		this.responseChannel = responseChannel;
		this.bestMoveFinder = new SmartMinMax();
	}


	public void do_start() {
		new RspUci().respond(responseChannel);
	}
	
	public void do_setOptions() {
	}	
	
	public void do_newGame() {
		this.game = null;
	}
	
	public void do_position_startpos(List<String> moves) {
		this.game = getGame(FENDecoder.INITIAL_FEN);
		executeMoves(moves);
	}


	public void do_position_fen(String fen, List<String> moves) {
		this.game = getGame(fen);
		executeMoves(moves);
	}
	
	public void do_go() {
		Move selectedMove = bestMoveFinder.findBestMove(game);

		new RspBestMove(encodeMove(selectedMove)).respond(responseChannel);
		//new RspBestMove("f7f6").respond(responseChannel);
	}	
	
	public void do_quit() {
		keepProcessing = false;
	}

	public void do_ping() {
		new RspReadyOk().respond(responseChannel);
	}

	public void do_stop() {
	}
	
	public boolean keepProcessing() {
		return keepProcessing;
	}

	private Game getGame(String fen) {		
		GameBuilder builder = new GameBuilder();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(fen);
		
		return builder.getResult();
	}
	
	private void executeMoves(List<String> moves) {
		for (String moveStr : moves) {
			boolean findMove = false;
			for (Move move : game.getPossibleMoves()) {
				String encodedMoveStr = encodeMove(move);
				if (encodedMoveStr.equals(moveStr)) {
					game.executeMove(move);
					findMove = true;
					break;
				}
			}
			if (!findMove) {
				throw new RuntimeException("No move found " + moveStr);
			}
		}
	}


	private String encodeMove(Move move) {
		String promotionStr = "";
		if(move instanceof MovePromotion){
			MovePromotion movePromotion = (MovePromotion) move;
			switch (movePromotion.getPromotion()) {
				case ROOK_WHITE:
				case ROOK_BLACK:
					promotionStr = "r";
					break;
				case KNIGHT_WHITE:
				case KNIGHT_BLACK:
					promotionStr = "k";
					break;
				case BISHOP_WHITE:
				case BISHOP_BLACK:
					promotionStr = "b";
					break;
				case QUEEN_WHITE:
				case QUEEN_BLACK:
					promotionStr = "q";
					break;
			default:
				throw new RuntimeException("Invalid promotion " + move);
			} 
		}
		
		return move.getFrom().getKey().toString() + move.getTo().getKey().toString() + promotionStr;
	}


	public Game getGame() {
		return game;
		
	}	
}
