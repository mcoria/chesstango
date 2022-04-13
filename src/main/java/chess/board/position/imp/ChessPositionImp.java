package chess.board.position.imp;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.ascii.ASCIIEncoder;
import chess.board.builder.ChessPositionBuilder;
import chess.board.fen.FENEncoder;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.SquareIterator;
import chess.board.moves.Move;
import chess.board.moves.MoveKing;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionImp implements ChessPosition {

	// PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
	protected PiecePlacement piecePlacement = null;
	protected ColorBoard colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;	

	@Override
	public void acceptForExecute(Move move) {
		move.executeMove(this);
	}

	@Override
	public void executeMove(Move move) {
		move.executeMove(this.piecePlacement);

		move.executeMove(this.colorBoard);

		move.executeMove(this.moveCache);

		move.executeMove(this.positionState);	
		
	}

	@Override
	public void executeMove(MoveKing move) {
		executeMove((Move)move);
		
		move.executeMove(this.kingCacheBoard);
		
	}	

	@Override
	public void acceptForUndo(Move move) {
		move.undoMove(this);
	}

	@Override
	public void undoMove(Move move) {
		move.undoMove(this.positionState);

		move.undoMove(this.moveCache);

		move.undoMove(this.colorBoard);

		move.undoMove(this.piecePlacement);
		
	}
	

	@Override
	public void undoMove(MoveKing move) {
		undoMove((Move)move);

		move.undoMove(this.kingCacheBoard);
	}	
	
	@Override
	public void constructBoardRepresentation(ChessPositionBuilder<?> builder){		
		builder.withTurno(positionState.getTurnoActual())
				.withCastlingWhiteQueenAllowed(positionState.isCastlingWhiteQueenAllowed())
				.withCastlingWhiteKingAllowed(positionState.isCastlingWhiteKingAllowed())
				.withCastlingBlackQueenAllowed(positionState.isCastlingBlackQueenAllowed())
				.withCastlingBlackKingAllowed(positionState.isCastlingBlackKingAllowed())
				.withEnPassantSquare(positionState.getEnPassantSquare());
		
		for(PiecePositioned pieza: piecePlacement){
			builder.withPieza(pieza.getKey(), pieza.getValue());
		}
	}
	
	
	@Override
	//TODO: estaria bueno que en vez de estar concatenado sea todo mas compacto, el lado del tablero el codigo FEN
	public String toString() {
		FENEncoder fenEncoder = new FENEncoder();
		ASCIIEncoder asciiEncoder = new ASCIIEncoder();
		
		constructBoardRepresentation(fenEncoder);
		constructBoardRepresentation(asciiEncoder);
		
	    return asciiEncoder.getResult() + fenEncoder.getResult();
	}

	public void setPiecePlacement(PiecePlacement dummyBoard) {
		this.piecePlacement = dummyBoard;
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}


	public void setMoveCache(MoveCacheBoard moveCache) {
		this.moveCache = moveCache;
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}


	@Override
	public Color getTurnoActual() {
		return this.positionState.getTurnoActual();
	}

	@Override
	public Square getEnPassantSquare() {
		return this.positionState.getEnPassantSquare();
	}

	@Override
	public boolean isCastlingWhiteQueenAllowed() {
		return this.positionState.isCastlingWhiteQueenAllowed();
	}	

	@Override
	public boolean isCastlingWhiteKingAllowed() {
		return this.positionState.isCastlingWhiteKingAllowed();
	}


	@Override
	public boolean isCastlingBlackQueenAllowed() {
		return this.positionState.isCastlingBlackQueenAllowed();
	}


	@Override
	public boolean isCastlingBlackKingAllowed() {
		return this.positionState.isCastlingBlackKingAllowed();
	}

	@Override
	public void init() {
		colorBoard.init(piecePlacement);
		kingCacheBoard.init(piecePlacement);		
		initCache();
	}
	
	//TODO: esta inicializacion deberia pasar a MoveGenaratorWithCache
	private void initCache() {
		MoveGeneratorImp moveGeneratorImp = new MoveGeneratorImp();
		moveGeneratorImp.setPiecePlacement(piecePlacement);
		moveGeneratorImp.setBoardState(positionState);
		moveGeneratorImp.setColorBoard(colorBoard);
		
		for(PiecePositioned origen: piecePlacement){
			
			if(origen.getValue() != null){

				MoveGeneratorResult generatorResult = moveGeneratorImp.generatePseudoMoves(origen);
	
				moveCache.setPseudoMoves(origen.getKey(), generatorResult);
			}
		}		
		
	}

	@Override
	public PiecePositioned getPosicion(Square square) {
		return piecePlacement.getPosicion(square);
	}


	@Override
	public Square getKingSquare(Color color) {
		return kingCacheBoard.getKingSquare(color);
	}


	@Override
	public SquareIterator iteratorSquare(Color color) {
		return colorBoard.iteratorSquare(color);
	}


	@Override
	public SquareIterator iteratorSquareWhitoutKing(Color color) {
		return colorBoard.iteratorSquareWhitoutKing(color, kingCacheBoard.getKingSquare(color));
	}
	
	@Override
	public PiecePlacementIterator iteratorAllPieces(){
		return piecePlacement.iterator(colorBoard.getPosiciones(Color.WHITE) & colorBoard.getPosiciones(Color.BLACK));
	}	

	@Override
	public Color getColor(Square square) {
		return colorBoard.getColor(square);
	}

	@Override
	public Piece getPieza(Square square) {
		return piecePlacement.getPieza(square);
	}


	@Override
	public PiecePlacementIterator iterator(SquareIterator squareIterator) {
		return piecePlacement.iterator(squareIterator);
	}	
	
}