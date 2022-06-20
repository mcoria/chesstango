package chess.board.position.imp;

import chess.board.Color;
import chess.board.Square;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PositionState {
    private static class PositionStateData {
        private Color currentTurn;
        private Square enPassantSquare;
        private boolean castlingWhiteQueenAllowed;
        private boolean castlingWhiteKingAllowed;
        private boolean castlingBlackQueenAllowed;
        private boolean castlingBlackKingAllowed;
        private int halfMoveClock;
        private int fullMoveClock;

    }

    private final Deque<PositionStateData> stackPositionStates = new ArrayDeque<PositionStateData>();
    private PositionStateData currentPositionState = new PositionStateData();

    public Square getEnPassantSquare() {
        return currentPositionState.enPassantSquare;
    }

    public void setEnPassantSquare(Square enPassantSquare) {
        currentPositionState.enPassantSquare = enPassantSquare;
    }

    public boolean isCastlingWhiteQueenAllowed() {
        return currentPositionState.castlingWhiteQueenAllowed;
    }

    public void setCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        currentPositionState.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
    }

    public boolean isCastlingWhiteKingAllowed() {
        return currentPositionState.castlingWhiteKingAllowed;
    }

    public void setCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        currentPositionState.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
    }

    public boolean isCastlingBlackQueenAllowed() {
        return currentPositionState.castlingBlackQueenAllowed;
    }

    public void setCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        currentPositionState.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
    }

    public boolean isCastlingBlackKingAllowed() {
        return currentPositionState.castlingBlackKingAllowed;
    }

    public void setCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        currentPositionState.castlingBlackKingAllowed = castlingBlackKingAllowed;
    }

    public Color getCurrentTurn() {
        return currentPositionState.currentTurn;
    }

    public void setCurrentTurn(Color turn) {
        currentPositionState.currentTurn = turn;
    }

    public void rollTurn() {
        currentPositionState.currentTurn = currentPositionState.currentTurn.oppositeColor();
    }


    public int getHalfMoveClock() {
        return currentPositionState.halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.currentPositionState.halfMoveClock = halfMoveClock;
    }

    public void incrementHalfMoveClock() {
        this.currentPositionState.halfMoveClock++;
    }

    public void resetHalfMoveClock() {
        this.currentPositionState.halfMoveClock = 0;
    }


    public int getFullMoveClock() {
        return currentPositionState.fullMoveClock;
    }

    public void setFullMoveClock(int fullMoveClock) {
        this.currentPositionState.fullMoveClock = fullMoveClock;
    }

    public void incrementFullMoveClock() {
        if (Color.BLACK.equals(currentPositionState.currentTurn)) {
            currentPositionState.fullMoveClock++;
        }
    }


    public void pushState() {
        PositionStateData node = new PositionStateData();
        node.enPassantSquare = currentPositionState.enPassantSquare;
        node.castlingWhiteQueenAllowed = currentPositionState.castlingWhiteQueenAllowed;
        node.castlingWhiteKingAllowed = currentPositionState.castlingWhiteKingAllowed;
        node.castlingBlackQueenAllowed = currentPositionState.castlingBlackQueenAllowed;
        node.castlingBlackKingAllowed = currentPositionState.castlingBlackKingAllowed;
        node.currentTurn = currentPositionState.currentTurn;
        node.halfMoveClock = currentPositionState.halfMoveClock;
        node.fullMoveClock = currentPositionState.fullMoveClock;

        stackPositionStates.push(node);
    }

    public void popState() {
        PositionStateData lastState = stackPositionStates.pop();

        currentPositionState = lastState;
    }

    @Override
    public PositionState clone() throws CloneNotSupportedException {
        PositionState clone = new PositionState();
        clone.currentPositionState.enPassantSquare = currentPositionState.enPassantSquare;
        clone.currentPositionState.castlingWhiteQueenAllowed = currentPositionState.castlingWhiteQueenAllowed;
        clone.currentPositionState.castlingWhiteKingAllowed = currentPositionState.castlingWhiteKingAllowed;
        clone.currentPositionState.castlingBlackQueenAllowed = currentPositionState.castlingBlackQueenAllowed;
        clone.currentPositionState.castlingBlackKingAllowed = currentPositionState.castlingBlackKingAllowed;
        clone.currentPositionState.currentTurn = currentPositionState.currentTurn;
        clone.currentPositionState.halfMoveClock = currentPositionState.halfMoveClock;
        clone.currentPositionState.fullMoveClock = currentPositionState.fullMoveClock;
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PositionState) {
            PositionState theInstance = (PositionState) obj;
            return Objects.equals(currentPositionState.currentTurn, theInstance.currentPositionState.currentTurn) && Objects.equals(currentPositionState.enPassantSquare, theInstance.currentPositionState.enPassantSquare) && currentPositionState.castlingWhiteQueenAllowed == theInstance.currentPositionState.castlingWhiteQueenAllowed && currentPositionState.castlingWhiteKingAllowed == theInstance.currentPositionState.castlingWhiteKingAllowed && currentPositionState.castlingBlackQueenAllowed == theInstance.currentPositionState.castlingBlackQueenAllowed && currentPositionState.castlingBlackKingAllowed == theInstance.currentPositionState.castlingBlackKingAllowed && currentPositionState.halfMoveClock == theInstance.currentPositionState.halfMoveClock && currentPositionState.fullMoveClock == theInstance.currentPositionState.fullMoveClock;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Turno Actual: " + String.format("%-6s", currentPositionState.currentTurn.toString()) + ", pawnPasanteSquare: " + (currentPositionState.enPassantSquare == null ? "- " : currentPositionState.enPassantSquare.toString()) + ", castlingWhiteQueenAllowed: " + currentPositionState.castlingWhiteQueenAllowed + ", castlingWhiteKingAllowed: " + currentPositionState.castlingWhiteKingAllowed + ", castlingBlackQueenAllowed: " + currentPositionState.castlingBlackQueenAllowed + ", castlingBlackKingAllowed: " + currentPositionState.castlingBlackKingAllowed + ", halfMoveClock: " + currentPositionState.halfMoveClock + ", fullMoveClock: " + currentPositionState.fullMoveClock;
    }
}
