package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.position.CareTakerRecord;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.PositionStateReader;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PositionStateImp implements PositionState, Cloneable {
    private Color currentTurn;
    private Square enPassantSquare;
    private boolean castlingWhiteQueenAllowed;
    private boolean castlingWhiteKingAllowed;
    private boolean castlingBlackQueenAllowed;
    private boolean castlingBlackKingAllowed;
    private int halfMoveClock;
    private int fullMoveClock;

    private final Deque<PositionStateImp> previousStates = new ArrayDeque<>();

    @Override
    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    @Override
    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    @Override
    public boolean isCastlingWhiteQueenAllowed() {
        return castlingWhiteQueenAllowed;
    }

    @Override
    public void setCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        this.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
    }

    @Override
    public boolean isCastlingWhiteKingAllowed() {
        return castlingWhiteKingAllowed;
    }

    @Override
    public void setCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        this.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
    }

    @Override
    public boolean isCastlingBlackQueenAllowed() {
        return castlingBlackQueenAllowed;
    }

    @Override
    public void setCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        this.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
    }

    @Override
    public boolean isCastlingBlackKingAllowed() {
        return castlingBlackKingAllowed;
    }

    @Override
    public void setCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        this.castlingBlackKingAllowed = castlingBlackKingAllowed;
    }

    @Override
    public Color getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public void setCurrentTurn(Color turn) {
        currentTurn = turn;
    }

    @Override
    public void rollTurn() {
        currentTurn = currentTurn.oppositeColor();
    }


    @Override
    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    @Override
    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
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
    public int getFullMoveClock() {
        return fullMoveClock;
    }

    @Override
    public void setFullMoveClock(int fullMoveClock) {
        this.fullMoveClock = fullMoveClock;
    }

    @Override
    public void incrementFullMoveClock() {
        if (Color.BLACK.equals(currentTurn)) {
            fullMoveClock++;
        }
    }


    @Override
    public void pushState() {
        PositionStateImp positionStateImp = clone();
        previousStates.push(positionStateImp);
    }

    @Override
    public void popState() {
        PositionStateImp positionStateImp = previousStates.pop();
        this.enPassantSquare = positionStateImp.enPassantSquare;
        this.castlingWhiteQueenAllowed = positionStateImp.castlingWhiteQueenAllowed;
        this.castlingWhiteKingAllowed = positionStateImp.castlingWhiteKingAllowed;
        this.castlingBlackKingAllowed = positionStateImp.castlingBlackKingAllowed;
        this.castlingBlackQueenAllowed = positionStateImp.castlingBlackQueenAllowed;
        this.currentTurn = positionStateImp.currentTurn;
        this.halfMoveClock = positionStateImp.halfMoveClock;
        this.fullMoveClock = positionStateImp.fullMoveClock;
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
    public PositionStateReader takeSnapshot() {
        return clone();
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
