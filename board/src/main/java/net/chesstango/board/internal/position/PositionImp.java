package net.chesstango.board.internal.position;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PositionBuilder;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.position.*;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 */
@Setter
public class PositionImp implements Position {

    // PosicionPiezaBoard y ColorBoard son representaciones distintas del tablero. Uno con mas informacion que la otra.
    protected SquareBoard squareBoard = null;
    protected PositionState positionState = null;

    protected BitBoard bitBoard = null;
    protected KingSquare kingSquare = null;
    protected MoveCacheBoard moveCache = null;
    protected ZobristHash zobristHash = null;

    @Override
    public void init() {
        kingSquare.init(squareBoard);
        zobristHash.init(squareBoard, positionState);
    }


    @Override
    public void constructChessPositionRepresentation(PositionBuilder<?> builder) {
        builder.withTurn(positionState.getCurrentTurn())
                .withCastlingWhiteQueenAllowed(positionState.isCastlingWhiteQueenAllowed())
                .withCastlingWhiteKingAllowed(positionState.isCastlingWhiteKingAllowed())
                .withCastlingBlackQueenAllowed(positionState.isCastlingBlackQueenAllowed())
                .withCastlingBlackKingAllowed(positionState.isCastlingBlackKingAllowed())
                .withEnPassantSquare(positionState.getEnPassantSquare())
                .withHalfMoveClock(positionState.getHalfMoveClock())
                .withFullMoveClock(positionState.getFullMoveClock());

        for (PiecePositioned piecePositioned : squareBoard) {
            if (piecePositioned.getPiece() != null) {
                builder.withPiece(piecePositioned.getSquare(), piecePositioned.getPiece());
            }
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
        return squareBoard.getPosition(square);
    }

    @Override
    public Square getKingSquare(Color color) {
        return kingSquare.getKingSquare(color);
    }

    @Override
    public Square getKingSquareWhite() {
        return kingSquare.getKingSquareWhite();
    }

    @Override
    public Square getKingSquareBlack() {
        return kingSquare.getKingSquareBlack();
    }

    @Override
    public SquareIterator iteratorSquare(Color color) {
        return bitBoard.iteratorSquare(color);
    }

    @Override
    public long getPositions(Color color) {
        return bitBoard.getPositions(color);
    }

    @Override
    public long getAllPositions() {
        return bitBoard.getAllPositions();
    }

    @Override
    public long getEmptyPositions() {
        return bitBoard.getEmptyPositions();
    }

    @Override
    public long getBishopPositions() {
        return bitBoard.getBishopPositions();
    }

    @Override
    public long getRookPositions() {
        return bitBoard.getRookPositions();
    }

    @Override
    public long getQueenPositions() {
        return bitBoard.getQueenPositions();
    }

    @Override
    public long getKnightPositions() {
        return bitBoard.getKnightPositions();
    }

    @Override
    public long getPawnPositions() {
        return bitBoard.getPawnPositions();
    }

    @Override
    public Iterator<PiecePositioned> iteratorAllPieces() {
        return squareBoard.iterator(bitBoard.getAllPositions());
    }

    @Override
    public long getZobristHash() {
        return zobristHash.getZobristHash();
    }

    @Override
    public Color getColor(Square square) {
        return bitBoard.getColor(square);
    }

    @Override
    public Piece getPiece(Square square) {
        return squareBoard.getPiece(square);
    }

    @Override
    public boolean isEmpty(Square square) {
        return squareBoard.isEmpty(square);
    }


    @Override
    public Iterator<PiecePositioned> iterator(SquareIterator squareIterator) {
        return squareBoard.iterator(squareIterator);
    }

    @Override
    public Iterator<PiecePositioned> iterator(long positions) {
        return squareBoard.iterator(positions);
    }

    @Override
    public Iterator<PiecePositioned> iterator() {
        return squareBoard.iterator();
    }

    @Override
    public PiecePositioned getElement(int idx) {
        return squareBoard.getElement(idx);
    }

    @Override
    public SquareBoard getSquareBoard() {
        return squareBoard;
    }

    @Override
    public BitBoard getBitBoard() {
        return bitBoard;
    }

    @Override
    public KingSquare getKingSquare() {
        return kingSquare;
    }

    @Override
    public MoveCacheBoard getMoveCache() {
        return moveCache;
    }

    @Override
    public PositionState getPositionState() {
        return positionState;
    }

    @Override
    public ZobristHash getZobrist() {
        return zobristHash;
    }

    @Override
    public String toString() {
        FENEncoder fenEncoder = new FENEncoder();

        constructChessPositionRepresentation(fenEncoder);

        return fenEncoder.getChessRepresentation().toString();
    }

}
