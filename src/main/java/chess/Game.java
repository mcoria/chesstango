package chess;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import chess.analyzer.AnalyzerResult;
import chess.analyzer.PositionAnalyzer;
import chess.builder.ChessPositionBuilder;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.iterators.square.SquareIterator;
import chess.moves.Move;
import chess.position.ChessPosition;

/**
 * @author Mauricio Coria
 *
 */
public class Game implements ChessPositionReader {
	
	public static enum GameStatus {
		IN_PROGRESS,
		JAQUE,
		JAQUE_MATE, 
		TABLAS		
	}
	
	private ChessPosition chessPosition;
	
	private GameStack boardPila = new GameStack();
	
	private PositionAnalyzer analyzer = null;	
	
	public Game(ChessPosition tablero, PositionAnalyzer analyzer){
		this.chessPosition = tablero;
		this.analyzer = analyzer;
	}

	public GameStatus executeMove(Square from, Square to) {
		if (GameStatus.IN_PROGRESS.equals(boardPila.getStatus()) || GameStatus.JAQUE.equals(boardPila.getStatus())) {
			Move move = getMovimiento(from, to);
			if (move != null) {
				return executeMove(move);
			} else {
				throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
			}
		} else {
			throw new RuntimeException("Invalid game state");
		}
	}
	

	public GameStatus executeMove(Move move) {
		//assert(boardPila.getPossibleMoves().contains(move));
		
		boardPila.setMovimientoSeleccionado(move);
		
		boardPila.push();
		
		chessPosition.acceptForExecute(move);
		
		return updateGameStatus();
	}


	public GameStatus undoMove() {
		boardPila.pop();
		
		Move lastMove = boardPila.getMovimientoSeleccionado();
		
		chessPosition.acceptForUndo(lastMove);
		
		return getGameStatus();
	}
	

	protected GameStatus updateGameStatus() {
		AnalyzerResult analyzerResult = analyzer.analyze();
		Collection<Move> movimientosPosibles = analyzerResult.getLegalMoves();
		boolean existsLegalMove = !movimientosPosibles.isEmpty();
		GameStatus gameStatus = null;

		if (existsLegalMove) {
			if (analyzerResult.isKingInCheck()) {
				gameStatus = GameStatus.JAQUE;
			} else {
				gameStatus = GameStatus.IN_PROGRESS;
			}
		} else {
			if (analyzerResult.isKingInCheck()) {
				gameStatus = GameStatus.JAQUE_MATE;
			} else {
				gameStatus = GameStatus.TABLAS;
			}
		}

		boardPila.setStatus(gameStatus);
		boardPila.setAnalyzerResult(analyzerResult);

		return gameStatus;
	}
	
	public Move getMovimiento(Square from, Square to) {
		for (Move move : getPossibleMoves() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return chessPosition.toString();
	}

	public Collection<Move> getPossibleMoves() {
		return boardPila.getAnalyzerResult().getLegalMoves();
	}

	public GameStatus getGameStatus() {
		return boardPila.getStatus();
	}
	
	@Override
	public Color getTurnoActual() {
		return chessPosition.getTurnoActual();
	}

	@Override
	public void constructBoardRepresentation(ChessPositionBuilder<?> builder) {
		chessPosition.constructBoardRepresentation(builder);
	}

	@Override
	public Square getPawnPasanteSquare() {
		return chessPosition.getPawnPasanteSquare();
	}

	@Override
	public boolean isCastlingWhiteQueenAllowed() {
		return chessPosition.isCastlingWhiteQueenAllowed();
	}	

	@Override
	public boolean isCastlingWhiteKingAllowed() {
		return chessPosition.isCastlingWhiteKingAllowed();
	}


	@Override
	public boolean isCastlingBlackQueenAllowed() {
		return chessPosition.isCastlingBlackQueenAllowed();
	}


	@Override
	public boolean isCastlingBlackKingAllowed() {
		return chessPosition.isCastlingBlackKingAllowed();
	}
	
	public void init() {
		updateGameStatus();
	}
	
	private static class GameStack {
		private AnalyzerResult analyzerResult; 
		private Move movimientoSeleccionado;
		private GameStatus status;
		
		private static class Node {
			private AnalyzerResult analyzerResult; 
			private Move movimientoSeleccionado;
			private GameStatus status;		
		}
		
		private Deque<Node> stackNode = new ArrayDeque<Node>();

		public Move getMovimientoSeleccionado() {
			return movimientoSeleccionado;
		}

		public void setMovimientoSeleccionado(Move movimientoSeleccionado) {
			this.movimientoSeleccionado = movimientoSeleccionado;
		}

		public GameStatus getStatus() {
			return status;
		}

		public void setStatus(GameStatus status) {
			this.status = status;
		}

		public void push() {
			Node node = new Node();
			node.movimientoSeleccionado = this.movimientoSeleccionado;
			node.analyzerResult = this.analyzerResult;
			node.status = this.status;
			
			stackNode.push(node);
			
			this.movimientoSeleccionado = null;
			this.analyzerResult = null;
			this.status = null;
		}

		public void pop() {
			Node node = stackNode.pop();
			this.movimientoSeleccionado = node.movimientoSeleccionado;
			this.analyzerResult = node.analyzerResult;
			this.status = node.status;		
		}

		public AnalyzerResult getAnalyzerResult() {
			return analyzerResult;
		}

		public void setAnalyzerResult(AnalyzerResult analyzerResult) {
			this.analyzerResult = analyzerResult;
		}
	}


	@Override
	public PiecePositioned getPosicion(Square square) {
		return chessPosition.getPosicion(square);
	}


	@Override
	public Color getColor(Square square) {
		return chessPosition.getColor(square);
	}


	@Override
	public Piece getPieza(Square square) {
		return chessPosition.getPieza(square);
	}


	@Override
	public Square getKingSquare(Color color) {
		return chessPosition.getKingSquare(color);
	}


	@Override
	public SquareIterator iteratorSquare(Color color) {
		return chessPosition.iteratorSquare(color);
	}

	@Override
	public SquareIterator iteratorSquareWhitoutKing(Color color) {
		return chessPosition.iteratorSquareWhitoutKing(color);
	}

	@Override
	public PiecePlacementIterator iterator(SquareIterator squareIterator) {
		return chessPosition.iterator(squareIterator);
	}	
}
