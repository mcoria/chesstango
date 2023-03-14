package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.ChessRepresentationBuilder;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.representations.ascii.ASCIIEncoder;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.Iterator;


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
	public void constructBoardRepresentation(ChessRepresentationBuilder<?> builder){
		builder.withTurn(positionState.getCurrentTurn())
				.withCastlingWhiteQueenAllowed(positionState.isCastlingWhiteQueenAllowed())
				.withCastlingWhiteKingAllowed(positionState.isCastlingWhiteKingAllowed())
				.withCastlingBlackQueenAllowed(positionState.isCastlingBlackQueenAllowed())
				.withCastlingBlackKingAllowed(positionState.isCastlingBlackKingAllowed())
				.withEnPassantSquare(positionState.getEnPassantSquare())
				.withHalfMoveClock(positionState.getHalfMoveClock())
				.withFullMoveClock(positionState.getFullMoveClock());
		
		for(PiecePositioned pieza: piecePlacement){
			builder.withPiece(pieza.getSquare(), pieza.getPiece());
		}
	}
	
	
	@Override
	//TODO: estaria bueno que en vez de estar concatenado sea todo mas compacto, el lado del tablero el codigo FEN
	public String toString() {
		FENEncoder fenEncoder = new FENEncoder();
		ASCIIEncoder asciiEncoder = new ASCIIEncoder();
		
		constructBoardRepresentation(fenEncoder);
		constructBoardRepresentation(asciiEncoder);
		
	    return asciiEncoder.getChessRepresentation() + fenEncoder.getChessRepresentation();
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
	public Color getCurrentTurn() {
		return this.positionState.getCurrentTurn();
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
	public int getHalfMoveClock() {
		return this.positionState.getHalfMoveClock();
	}

	@Override
	public int getFullMoveClock() {
		return this.positionState.getFullMoveClock();
	}

	@Override
	public void init() {
		colorBoard.init(piecePlacement);
		kingCacheBoard.init(piecePlacement);
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
	public SquareIterator iteratorSquareWithoutKing(Color color) {
		return colorBoard.iteratorSquareWithoutKing(color, kingCacheBoard.getKingSquare(color));
	}

	@Override
	public Iterator<PiecePositioned> iteratorAllPieces(){
		return piecePlacement.iterator(colorBoard.getPosiciones(Color.WHITE) | colorBoard.getPosiciones(Color.BLACK));
	}

	@Override
	public Color getColor(Square square) {
		return colorBoard.getColor(square);
	}

	@Override
	public Piece getPiece(Square square) {
		return piecePlacement.getPiece(square);
	}

	@Override
	public boolean isEmpty(Square square) {
		return piecePlacement.isEmpty(square);
	}

	@Override
	public long getPositions(Color color) {
		return colorBoard.getPosiciones(color);
	}

	@Override
	public Iterator<PiecePositioned> iterator(SquareIterator squareIterator) {
		return piecePlacement.iterator(squareIterator);
	}

	@Override
	public Iterator<PiecePositioned> iterator(long positions) {
		return piecePlacement.iterator(positions);
	}

	@Override
	public Iterator<PiecePositioned> iterator() {
		return piecePlacement.iterator();
	}

	@Override
	public PiecePositioned getElement(int idx) {
		return piecePlacement.getElement(idx);
	}
}
