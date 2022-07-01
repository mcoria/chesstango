package net.chesstango.board;

import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public class Game {
	
	private final ChessPosition chessPosition;

	private final MoveGenerator pseudoMovesGenerator;
	
	private final GameState gameState;
	
	private final PositionAnalyzer analyzer;

	private boolean analyze;
	
	public Game(ChessPosition chessPosition, MoveGenerator pseudoMovesGenerator, PositionAnalyzer analyzer, GameState gameState){
		this.chessPosition = chessPosition;
		this.pseudoMovesGenerator = pseudoMovesGenerator;
		this.analyzer = analyzer;
		this.gameState = gameState;
		this.analyze = true;
	}

	public Game executeMove(Square from, Square to) {
		Move move = getMove(from, to);
		if (move != null) {
			return executeMove(move);
		} else {
			throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
		}
	}

	public Game executeMove(Move move) {
		gameState.setSelectedMove(move);
		
		gameState.push();
		
		chessPosition.acceptForExecute(move);

		analyze = true;

		return this;
	}


	public Game undoMove() {
		gameState.pop();
		
		Move lastMove = gameState.getSelectedMove();
		
		chessPosition.acceptForUndo(lastMove);

		return this;
	}

	public Move getMove(Square from, Square to) {
		for (Move move : getPossibleMoves() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	public Move getMove(Square from, Square to, Piece promotionPiece) {
		for (Move move : getPossibleMoves() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey()) && (move instanceof MovePromotion)){
				MovePromotion movePromotion = (MovePromotion) move;
				if(movePromotion.getPromotion().equals(promotionPiece)) {
					return move;
				}
			}
		}
		return null;
	}

	public MoveContainerReader getPossibleMoves() {
		return getGameState().getLegalMoves();
	}

	public GameState.Status getStatus() {
		return getGameState().getStatus();
	}

	public GameState getGameState() {
		if(analyze){
			analyzer.updateGameStatus();
			analyze = false;
		}
		return gameState;
	}
	
	public ChessPositionReader getChessPosition(){
		return chessPosition;
	}

	@Override
	public String toString() {
		return chessPosition.toString();
	}

	public void init() {
		chessPosition.init();
	}

	public MoveGenerator getPseudoMovesGenerator() {
		return pseudoMovesGenerator;
	}
}
