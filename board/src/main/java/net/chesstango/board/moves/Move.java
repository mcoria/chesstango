package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.*;

//TODO: Y si en vez de PosicionPieza utilizamos Square para To?
//      La mayoria de los movimientos posibles es a bysquare vacios
//      Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.
//TODO: y se implementamos un cache de movimientos? Implementar flyweight  pattern

/**
 * @author Mauricio Coria
 */
public interface Move extends Comparable<Move> {
    PiecePositioned getFrom();

    PiecePositioned getTo();

    default void executeMove(ChessPositionWriter chessPosition) {
        chessPosition.executeMove(this);
    }

    default void undoMove(ChessPositionWriter chessPosition) {
        chessPosition.undoMove(this);
    }

    default boolean filter(MoveFilter filter) {
        return filter.filterMove(this);
    }

    void executeMove(SquareBoardWriter board);

    void undoMove(SquareBoardWriter board);

    void executeMove(PositionStateWriter positionState);

    void undoMove(PositionStateWriter positionStateWriter);

    void executeMove(BitBoardWriter colorBoard);

    void undoMove(BitBoardWriter colorBoard);

    void executeMove(MoveCacheBoardWriter moveCache);

    void undoMove(MoveCacheBoardWriter moveCache);

    void executeMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board);

    void undoMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board);

    /**
     * "move" is a bit field with the following meaning (bit 0 is the least significant bit)
     * bits                meaning
     * ===================================
     * 0,1,2               to file
     * 3,4,5               to row
     * 6,7,8               from file
     * 9,10,11             from row
     * 12,13,14            promotion piece
     * <p>
     * "promotion piece" is encoded as follows
     * none       0
     * knight     1
     * bishop     2
     * rook       3
     * queen      4
     *
     * @return
     */
    default short binaryEncoding() {
        Square fromSquare = getFrom().getSquare();
        Square toSquare = getTo().getSquare();

        int toFile = toSquare.getFile();
        int toRow = toSquare.getRank() << 3;

        int fromFile = fromSquare.getFile() << 6;
        int fromRow = fromSquare.getRank() << 9;

        return (short) (fromRow | fromFile | toRow | toFile);
    }

    Cardinal getMoveDirection();

    @Override
    default int compareTo(Move theOther) {
        //Comparamos from
        if (getFrom().getSquare().getRank() > theOther.getFrom().getSquare().getRank()) {
            return 1;
        } else if (getFrom().getSquare().getRank() < theOther.getFrom().getSquare().getRank()) {
            return -1;
        }


        if (getFrom().getSquare().getFile() < theOther.getFrom().getSquare().getFile()) {
            return 1;
        } else if (getFrom().getSquare().getFile() > theOther.getFrom().getSquare().getFile()) {
            return -1;
        }

        //---------------
        //Son iguales asi que comparamos to
        if (getTo().getSquare().getRank() < theOther.getTo().getSquare().getRank()) {
            return 1;
        } else if (getTo().getSquare().getRank() > theOther.getTo().getSquare().getRank()) {
            return -1;
        }


        if (getTo().getSquare().getFile() < theOther.getTo().getSquare().getFile()) {
            return -1;
        } else if (getTo().getSquare().getFile() > theOther.getTo().getSquare().getFile()) {
            return 1;
        }

        //--------------- Desde y hasta coinciden, que hacemos ?

        return 0;
    }
}