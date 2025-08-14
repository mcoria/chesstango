package net.chesstango.board.internal.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.BitIterator;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.gardel.ascii.ASCIIBuilder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class SquareBoardImp implements SquareBoard, Cloneable {

    public SquareBoardImp() {
        for (int i = 0; i < 64; i++) {
            this.setEmptySquare(Square.squareByIdx(i));
        }
    }

    /// ////////////////////////// START positioning logic /////////////////////////////
    // Quizas podria encapsular estas operaciones en su propia clase.
    // Bitboard podria ser mas rapido? Un word por tipo de ficha
    // Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
    //
    protected final PiecePositioned[] tablero = new PiecePositioned[64];


    @Override
    public PiecePositioned getPosition(Square square) {
        return tablero[square.idx()];
    }

    @Override
    public PiecePositioned getElement(int idx) {
        return tablero[idx];
    }

    @Override
    public void setPosition(PiecePositioned piecePositioned) {
        Square square = piecePositioned.square();
        tablero[square.idx()] = piecePositioned;
    }


    @Override
    public Piece getPiece(Square square) {
        return tablero[square.idx()].piece();
    }


    @Override
    public void setPiece(Square square, Piece piece) {
        tablero[square.idx()] = PiecePositioned.of(square, piece);
    }


    @Override
    public void setEmptySquare(Square square) {
        tablero[square.idx()] = PiecePositioned.of(square, null);
    }

    @Override
    public void setEmptyPosition(PiecePositioned piecePositioned) {
        setEmptySquare(piecePositioned.square());
    }


    @Override
    public boolean isEmpty(Square square) {
        return getPiece(square) == null;
    }


    @Override
    public Iterator<PiecePositioned> iterator(SquareIterator squareIterator) {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return squareIterator.hasNext();
            }

            @Override
            public PiecePositioned next() {
                return getPosition(squareIterator.next());
            }

        };
    }

    @Override
    public Iterator<PiecePositioned> iterator(long positions) {
        return new BitIterator<>(this, positions);
    }

    @Override
    public void move(PiecePositioned from, PiecePositioned to) {
        this.setEmptySquare(from.square());                            //Dejamos el origen
        this.setPiece(to.square(), from.piece());                   //Vamos al destino
    }

    @Override
    public String toString() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baos)) {
            ASCIIBuilder builder = new ASCIIBuilder();
            forEach(piecePositioned -> {
                if (piecePositioned.piece() != null) {
                    int file = piecePositioned.square().getFile();
                    int rank = piecePositioned.square().getRank();
                    switch (piecePositioned.piece()) {
                        case PAWN_WHITE -> builder.withWhitePawn(file, rank);
                        case KNIGHT_WHITE -> builder.withWhiteKnight(file, rank);
                        case BISHOP_WHITE -> builder.withWhiteBishop(file, rank);
                        case ROOK_WHITE -> builder.withWhiteRook(file, rank);
                        case QUEEN_WHITE -> builder.withWhiteQueen(file, rank);
                        case KING_WHITE -> builder.withWhiteKing(file, rank);
                        case PAWN_BLACK -> builder.withBlackPawn(file, rank);
                        case KNIGHT_BLACK -> builder.withBlackKnight(file, rank);
                        case BISHOP_BLACK -> builder.withBlackBishop(file, rank);
                        case ROOK_BLACK -> builder.withBlackRook(file, rank);
                        case QUEEN_BLACK -> builder.withBlackQueen(file, rank);
                        case KING_BLACK -> builder.withBlackKing(file, rank);
                    }
                }
            });
            builder.printPiecePlacement(ps);
        }
        return baos.toString();
    }


    @Override
    public SquareBoardImp clone() throws CloneNotSupportedException {
        SquareBoardImp clone = new SquareBoardImp();
        System.arraycopy(this.tablero, 0, clone.tablero, 0, 64);
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SquareBoardImp theInstance) {
            for (int i = 0; i < 64; i++) {
                if (!this.tablero[i].equals(theInstance.tablero[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Iterator<PiecePositioned> iterator() {
        return new Iterator<>() {

            private int idx = 0;

            @Override
            public boolean hasNext() {
                return this.idx < 64;
            }

            @Override
            public PiecePositioned next() {
                return tablero[idx++];
            }

        };
    }

}
