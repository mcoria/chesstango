/**
 * 
 */
package chess.uci.engine;

import java.util.List;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.MinMax;
import chess.ai.imp.smart.MinMaxPrunning;
import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.representations.MoveEncoder;
import chess.board.representations.fen.FENDecoder;
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

	private final MoveEncoder moveEncoder;

	private Game game;
	
	public Engine(UCIResponseChannel responseChannel) {
		this.responseChannel = responseChannel;
		//this.bestMoveFinder = new MinMax();
		this.bestMoveFinder = new MinMaxPrunning();
		this.moveEncoder = new MoveEncoder();
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
		this.game = loadGame(FENDecoder.INITIAL_FEN);
		executeMoves(moves);
	}


	public void do_position_fen(String fen, List<String> moves) {
		this.game = loadGame(fen);
		executeMoves(moves);
	}
	
	public void do_go() {
		Move selectedMove = bestMoveFinder.findBestMove(game);

		new RspBestMove(moveEncoder.encode(selectedMove)).respond(responseChannel);
	}	
	
	public void do_quit() {
		keepProcessing = false;
	}

	public void do_ping() {
		new RspReadyOk().respond(responseChannel);
	}

	public void do_stop() {
		//TODO: implementar stop
	}
	
	public boolean keepProcessing() {
		return keepProcessing;
	}

	public Game getGame(){ return game;}

	private Game loadGame(String fen) {
		GameBuilder builder = new GameBuilder();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(fen);
		
		return builder.getResult();
	}

	private void executeMoves(List<String> moves) {
		for (String moveStr : moves) {
			boolean findMove = false;
			for (Move move : game.getPossibleMoves()) {
				String encodedMoveStr = moveEncoder.encode(move);
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

}
