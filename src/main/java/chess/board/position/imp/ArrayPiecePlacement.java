package chess.board.position.imp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.representations.ascii.ASCIIEncoder;
import chess.board.iterators.byposition.BitIterator;
import chess.board.iterators.SquareIterator;
import chess.board.position.PiecePlacement;

/**
 * @author Mauricio Coria
 */
public class ArrayPiecePlacement implements PiecePlacement, Cloneable {

    public ArrayPiecePlacement() {
        for (int i = 0; i < 64; i++) {
            this.setEmptySquare(Square.getSquare(i));
        }
    }

    ///////////////////////////// START positioning logic /////////////////////////////
    // Quizas podria encapsular estas operaciones en su propia clase.
    // Bitboard podria ser mas rapido? Un word por tipo de ficha
    // Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
    //
    protected PiecePositioned[] tablero = new PiecePositioned[64];


    @Override
    public PiecePositioned getPosicion(Square square) {
        return tablero[square.toIdx()];
    }

    @Override
    public PiecePositioned getElement(int idx) {
        return tablero[idx];
    }

    @Override
    public void setPosicion(PiecePositioned entry) {
        Square square = entry.getKey();
        tablero[square.toIdx()] = entry;
    }


    @Override
    public Piece getPieza(Square square) {
        return tablero[square.toIdx()].getValue();
    }


    @Override
    public void setPieza(Square square, Piece piece) {
        tablero[square.toIdx()] = PiecePositioned.getPiecePositioned(square, piece);
    }


    @Override
    public void setEmptySquare(Square square) {
        tablero[square.toIdx()] = PiecePositioned.getPiecePositioned(square, null);
    }

    @Override
    public void setEmptyPosicion(PiecePositioned captura) {
        setEmptySquare(captura.getKey());
    }


    @Override
    public boolean isEmtpy(Square square) {
        return getPieza(square) == null;
    }


    @Override
    public Iterator<PiecePositioned> iterator(SquareIterator squareIterator) {
        return new Iterator<PiecePositioned>() {

            @Override
            public boolean hasNext() {
                return squareIterator.hasNext();
            }

            @Override
            public PiecePositioned next() {
                return getPosicion(squareIterator.next());
            }

        };
    }

    @Override
    public Iterator<PiecePositioned> iterator(long posiciones) {
        return new BitIterator(this, posiciones);
    }

    @Override
    public void move(PiecePositioned from, PiecePositioned to) {
        this.setEmptySquare(from.getKey());                            //Dejamos el origen
        this.setPieza(to.getKey(), from.getValue());                //Vamos al destino
    }

    @Override
    public String toString() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baos)) {
            ASCIIEncoder output = new ASCIIEncoder();
            this.forEach(posicionPieza -> {
                output.withPiece(posicionPieza.getKey(), posicionPieza.getValue());
            });
            output.getPiecePlacement(ps);
        }
        return baos.toString();
    }


    @Override
    public ArrayPiecePlacement clone() throws CloneNotSupportedException {
        ArrayPiecePlacement clone = new ArrayPiecePlacement();
        for (int i = 0; i < 64; i++) {
            clone.tablero[i] = this.tablero[i];
        }
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArrayPiecePlacement) {
            ArrayPiecePlacement theInstance = (ArrayPiecePlacement) obj;
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
        return new Iterator<PiecePositioned>() {

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
