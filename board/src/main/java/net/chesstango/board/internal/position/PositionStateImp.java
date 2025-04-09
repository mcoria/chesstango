package net.chesstango.board.internal.position;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.PositionStateReader;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class PositionStateImp implements PositionState, Cloneable {
    private Color currentTurn;
    private Square enPassantSquare;
    private boolean castlingWhiteQueenAllowed;
    private boolean castlingWhiteKingAllowed;
    private boolean castlingBlackQueenAllowed;
    private boolean castlingBlackKingAllowed;

    private int halfMoveClock;
    private int fullMoveClock;

    @Override
    public void rollTurn() {
        currentTurn = currentTurn.oppositeColor();
    }

    @Override
    public void incrementHalfMoveClock() {
        this.halfMoveClock++;
    }

    @Override
    public void resetHalfMoveClock() {
        this.halfMoveClock = 0;
    }

    @Override
    public void incrementFullMoveClock() {
        if (Color.BLACK.equals(currentTurn)) {
            fullMoveClock++;
        }
    }

    @Override
    public PositionStateReader takeSnapshot() {
        return clone();
    }

    @Override
    public void restoreSnapshot(PositionStateReader snapshot) {
        this.enPassantSquare = snapshot.getEnPassantSquare();
        this.castlingWhiteQueenAllowed = snapshot.isCastlingWhiteQueenAllowed();
        this.castlingWhiteKingAllowed = snapshot.isCastlingWhiteKingAllowed();
        this.castlingBlackQueenAllowed = snapshot.isCastlingBlackQueenAllowed();
        this.castlingBlackKingAllowed = snapshot.isCastlingBlackKingAllowed();
        this.currentTurn = snapshot.getCurrentTurn();
        this.halfMoveClock = snapshot.getHalfMoveClock();
        this.fullMoveClock = snapshot.getFullMoveClock();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PositionStateImp theInstance) {
            return Objects.equals(currentTurn, theInstance.currentTurn) && Objects.equals(enPassantSquare, theInstance.enPassantSquare) && castlingWhiteQueenAllowed == theInstance.castlingWhiteQueenAllowed && castlingWhiteKingAllowed == theInstance.castlingWhiteKingAllowed && castlingBlackQueenAllowed == theInstance.castlingBlackQueenAllowed && castlingBlackKingAllowed == theInstance.castlingBlackKingAllowed && halfMoveClock == theInstance.halfMoveClock && fullMoveClock == theInstance.fullMoveClock;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Turno Actual: " + String.format("%-6s", currentTurn.toString()) + ", pawnPasanteSquare: " + (enPassantSquare == null ? "- " : enPassantSquare.toString()) + ", castlingWhiteQueenAllowed: " + castlingWhiteQueenAllowed + ", castlingWhiteKingAllowed: " + castlingWhiteKingAllowed + ", castlingBlackQueenAllowed: " + castlingBlackQueenAllowed + ", castlingBlackKingAllowed: " + castlingBlackKingAllowed + ", halfMoveClock: " + halfMoveClock + ", fullMoveClock: " + fullMoveClock;
    }

    @Override
    public PositionStateImp clone() {
        PositionStateImp clone = new PositionStateImp();
        clone.enPassantSquare = enPassantSquare;
        clone.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
        clone.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
        clone.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
        clone.castlingBlackKingAllowed = castlingBlackKingAllowed;
        clone.currentTurn = currentTurn;
        clone.halfMoveClock = halfMoveClock;
        clone.fullMoveClock = fullMoveClock;
        return clone;
    }

}
