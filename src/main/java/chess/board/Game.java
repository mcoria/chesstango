package chess.board;

import chess.board.analyzer.PositionAnalyzer;
import chess.board.moves.Move;
import chess.board.moves.MovePromotion;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.position.ChessPosition;
import chess.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public class Game {
	
	private final ChessPosition chessPosition;
	
	private final GameState gameState;
	
	private final PositionAnalyzer analyzer;

	private boolean analyze;
	
	public Game(ChessPosition chessPosition, PositionAnalyzer analyzer, GameState gameState){
		this.chessPosition = chessPosition;
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
}
