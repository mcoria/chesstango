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
    protected State state = null;

    protected BitBoard bitBoard = null;
    protected KingSquare kingSquare = null;
    protected MoveCacheBoard moveCache = null;
    protected ZobristHash zobristHash = null;

    @Override
    public void init() {
        kingSquare.init(squareBoard);
        zobristHash.init(squareBoard, state);
    }


    @Override
    public void constructChessPositionRepresentation(PositionBuilder<?> builder) {
        builder.withTurn(state.getCurrentTurn())
                .withCastlingWhiteQueenAllowed(state.isCastlingWhiteQueenAllowed())
                .withCastlingWhiteKingAllowed(state.isCastlingWhiteKingAllowed())
                .withCastlingBlackQueenAllowed(state.isCastlingBlackQueenAllowed())
                .withCastlingBlackKingAllowed(state.isCastlingBlackKingAllowed())
                .withEnPassantSquare(state.getEnPassantSquare())
                .withHalfMoveClock(state.getHalfMoveClock())
                .withFullMoveClock(state.getFullMoveClock());

        for (PiecePositioned piecePositioned : squareBoard) {
            if (piecePositioned.getPiece() != null) {
                builder.withPiece(piecePositioned.getSquare(), piecePositioned.getPiece());
            }
        }
    }

    @Override
    public Color getCurrentTurn() {
        return this.state.getCurrentTurn();
    }

    @Override
    public Square getEnPassantSquare() {
        return this.state.getEnPassantSquare();
    }

    @Override
    public boolean isCastlingWhiteQueenAllowed() {
        return this.state.isCastlingWhiteQueenAllowed();
    }

    @Override
    public boolean isCastlingWhiteKingAllowed() {
        return this.state.isCastlingWhiteKingAllowed();
    }

    @Override
    public boolean isCastlingBlackQueenAllowed() {
        return this.state.isCastlingBlackQueenAllowed();
    }

    @Override
    public boolean isCastlingBlackKingAllowed() {
        return this.state.isCastlingBlackKingAllowed();
    }

    @Override
    public int getHalfMoveClock() {
        return this.state.getHalfMoveClock();
    }

    @Override
    public int getFullMoveClock() {
        return this.state.getFullMoveClock();
    }

    @Override
    public StateReader getPreviousPositionState() {
        return this.state.getPreviousPositionState();
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
    public SquareBoardWriter getSquareBoardWriter() {
        return squareBoard;
    }

    @Override
    public BitBoardWriter getBitBoardWriter() {
        return bitBoard;
    }

    @Override
    public KingSquareWriter getKingSquareWriter() {
        return kingSquare;
    }

    @Override
    public MoveCacheBoardWriter getMoveCacheWriter() {
        return moveCache;
    }

    @Override
    public StateWriter getPositionStateWriter() {
        return state;
    }

    @Override
    public ZobristHashWriter getZobristWriter() {
        return zobristHash;
    }

    @Override
    public String toString() {
        FENEncoder fenEncoder = new FENEncoder();

        constructChessPositionRepresentation(fenEncoder);

        return fenEncoder.getChessRepresentation().toString();
    }

}
