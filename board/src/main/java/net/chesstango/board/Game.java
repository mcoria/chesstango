package net.chesstango.board;

import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 *
 */
public class Game {
	
	private final ChessPosition chessPosition;

	private final MoveGenerator pseudoMovesGenerator;
	
	private final GameState gameState;
	
	private final PositionAnalyzer analyzer;

	private boolean detectRepetitions;
	
	public Game(ChessPosition chessPosition, GameState gameState, MoveGenerator pseudoMovesGenerator, PositionAnalyzer analyzer){
		this.chessPosition = chessPosition;
		this.pseudoMovesGenerator = pseudoMovesGenerator;
		this.gameState = gameState;
		this.analyzer = analyzer;

		this.chessPosition.init();
		this.analyzer.updateGameState();
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

		if(detectRepetitions){
			saveFENWithoutClocks();
		}

		analyzer.updateGameState();

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
	public void detectRepetitions(boolean flag) {
		this.detectRepetitions = flag;
		this.analyzer.detectRepetitions(flag);
		if(detectRepetitions){
			saveFENWithoutClocks();
		}
	}

	private void saveFENWithoutClocks() {
		FENEncoder encoder = new FENEncoder();

		chessPosition.constructBoardRepresentation(encoder);

		String fenWithoutClocks = encoder.getFENWithoutClocks();

		gameState.setFenWithoutClocks(fenWithoutClocks);
	}

	public MoveContainerReader getPossibleMoves() {
		return getGameState().getLegalMoves();
	}

	public GameStatus getStatus() {
		return getGameState().getStatus();
	}

	public GameState getGameState() {
		return gameState;
	}

	public ChessPositionReader getChessPosition(){
		return chessPosition;
	}

	public MoveGenerator getPseudoMovesGenerator() {
		return pseudoMovesGenerator;
	}

	public void accept(GameStateVisitor gameStateVisitor){
		gameState.accept(gameStateVisitor);
	}

	@Override
	public String toString() {
		return chessPosition.toString();
	}
}
