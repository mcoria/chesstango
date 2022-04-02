/**
 * 
 */
package uci.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import chess.Game;
import chess.PiecePositioned;
import chess.builder.imp.GameBuilder;
import chess.fen.FENDecoder;
import chess.moves.Move;
import chess.moves.MovePromotion;
import uci.protocol.UCIResponseChannel;
import uci.protocol.responses.RspBestMove;
import uci.protocol.responses.RspReadyOk;
import uci.protocol.responses.uci.RspUci;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {
	private boolean keepProcessing = true;
	
	private final UCIResponseChannel responseChannel;

	private Game game;
	
	public Engine(UCIResponseChannel responseChannel) {
		this.responseChannel = responseChannel;
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
		this.game = getDefaultGame();
		executeMoves(moves);
	}


	public void do_position_fen(String fen, List<String> moves) {

	}
	
	public void do_go() {
		Collection<Move> moves = this.game.getPossibleMoves();

		Map<PiecePositioned, Collection<Move>> moveMap = new HashMap<PiecePositioned, Collection<Move>>();

		for (Move move : moves) {
			PiecePositioned key = move.getFrom();
			Collection<Move> positionMoves = moveMap.get(key);
			if (positionMoves == null) {
				positionMoves = new ArrayList<Move>();
				moveMap.put(key, positionMoves);
			}
			positionMoves.add(move);
		}

		PiecePositioned[] pieces = moveMap.keySet().toArray(new PiecePositioned[moveMap.keySet().size()]);
		PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

		Collection<Move> selectedMovesCollection = moveMap.get(selectedPiece);
		Move[] selectedMovesArray = selectedMovesCollection.toArray(new Move[selectedMovesCollection.size()]);

		Move selectedMove = selectedMovesArray[ThreadLocalRandom.current().nextInt(0, selectedMovesArray.length)];

		new RspBestMove(encodeMove(selectedMove)).respond(responseChannel);
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

	private Game getDefaultGame() {		
		GameBuilder builder = new GameBuilder();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(FENDecoder.INITIAL_FEN);
		
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
			if (findMove == false) {
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


	public Game getGeme() {
		return game;
		
	}	
}
