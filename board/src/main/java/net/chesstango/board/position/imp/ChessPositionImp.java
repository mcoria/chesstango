package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.ChessRepresentationBuilder;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.position.*;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionImp implements ChessPosition {

	// PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
	protected Board board = null;
	protected ColorBoard colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;
	protected ZobristHash zobristHash = null;

	@Override
	public void init() {
		colorBoard.init(board);
		kingCacheBoard.init(board);
		zobristHash.init(board, positionState);
	}

	@Override
	public void acceptForDo(Move move) {
		move.executeMove(this);
	}

	@Override
	public void executeMove(Move move) {
		move.executeMove(this.board);

		move.executeMove(this.colorBoard);

		move.executeMove(this.moveCache);

		PositionStateReader oldPositionState = positionState.getCurrentState();

		move.executeMove(this.positionState);

		move.executeMove(this.zobristHash, oldPositionState, this.positionState, this.board);

	}

	@Override
	public void executeMoveKing(MoveKing move) {
		executeMove(move);
		
		move.executeMove(this.kingCacheBoard);

	}	

	@Override
	public void acceptForUndo(Move move) {
		move.undoMove(this);
	}

	@Override
	public void undoMove(Move move) {
		PositionStateReader oldPositionState = positionState.getCurrentState();

		move.undoMove(this.positionState);

		move.undoMove(this.moveCache);

		move.undoMove(this.colorBoard);

		move.undoMove(this.board);

		move.undoMove(this.zobristHash, oldPositionState, this.positionState, this.board);
		
	}

	@Override
	public void undoMoveKing(MoveKing move) {
		undoMove(move);

		move.undoMove(this.kingCacheBoard);
	}	
	
	@Override
	public void constructChessPositionRepresentation(ChessRepresentationBuilder<?> builder){
		builder.withTurn(positionState.getCurrentTurn())
				.withCastlingWhiteQueenAllowed(positionState.isCastlingWhiteQueenAllowed())
				.withCastlingWhiteKingAllowed(positionState.isCastlingWhiteKingAllowed())
				.withCastlingBlackQueenAllowed(positionState.isCastlingBlackQueenAllowed())
				.withCastlingBlackKingAllowed(positionState.isCastlingBlackKingAllowed())
				.withEnPassantSquare(positionState.getEnPassantSquare())
				.withHalfMoveClock(positionState.getHalfMoveClock())
				.withFullMoveClock(positionState.getFullMoveClock());
		
		for(PiecePositioned pieza: board){
			builder.withPiece(pieza.getSquare(), pieza.getPiece());
		}
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
	public PiecePositioned getPosition(Square square) {
		return board.getPosition(square);
	}

	@Override
	public Square getKingSquare(Color color) {
		return kingCacheBoard.getKingSquare(color);
	}

	@Override
	public Square getKingSquareWhite() {
		return kingCacheBoard.getKingSquareWhite();
	}

	@Override
	public Square getKingSquareBlack() {
		return kingCacheBoard.getKingSquareBlack();
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
		return board.iterator(colorBoard.getPositions(Color.WHITE) | colorBoard.getPositions(Color.BLACK));
	}

	@Override
	public long getZobristHash() {
		return zobristHash.getZobristHash();
	}

	@Override
	public Color getColor(Square square) {
		return colorBoard.getColor(square);
	}

	@Override
	public Piece getPiece(Square square) {
		return board.getPiece(square);
	}

	@Override
	public boolean isEmpty(Square square) {
		return board.isEmpty(square);
	}

	@Override
	public long getColorPositions(Color color) {
		return colorBoard.getPositions(color);
	}

	@Override
	public Iterator<PiecePositioned> iterator(SquareIterator squareIterator) {
		return board.iterator(squareIterator);
	}

	@Override
	public Iterator<PiecePositioned> iterator(long positions) {
		return board.iterator(positions);
	}

	@Override
	public Iterator<PiecePositioned> iterator() {
		return board.iterator();
	}

	@Override
	public PiecePositioned getElement(int idx) {
		return board.getElement(idx);
	}

	@Override
	public String toString() {
		FENEncoder fenEncoder = new FENEncoder();

		constructChessPositionRepresentation(fenEncoder);

		return fenEncoder.getChessRepresentation();
	}

	public void setPiecePlacement(Board board) {
		this.board = board;
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

	public void setZobristHash(ZobristHash zobristHash) {
		this.zobristHash = zobristHash;
	}

}
