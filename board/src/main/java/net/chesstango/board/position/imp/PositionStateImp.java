package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.PositionStateWriter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PositionStateImp implements PositionState {
    private static class PositionStateData implements PositionStateReader {
        private Color currentTurn;
        private Square enPassantSquare;
        private boolean castlingWhiteQueenAllowed;
        private boolean castlingWhiteKingAllowed;
        private boolean castlingBlackQueenAllowed;
        private boolean castlingBlackKingAllowed;
        private int halfMoveClock;
        private int fullMoveClock;

        @Override
        public Square getEnPassantSquare() {
            return enPassantSquare;
        }

        @Override
        public boolean isCastlingWhiteQueenAllowed() {
            return castlingWhiteQueenAllowed;
        }

        @Override
        public boolean isCastlingWhiteKingAllowed() {
            return castlingWhiteKingAllowed;
        }

        @Override
        public boolean isCastlingBlackQueenAllowed() {
            return castlingBlackQueenAllowed;
        }

        @Override
        public boolean isCastlingBlackKingAllowed() {
            return castlingBlackKingAllowed;
        }

        @Override
        public Color getCurrentTurn() {
            return currentTurn;
        }

        @Override
        public int getHalfMoveClock() {
            return halfMoveClock;
        }

        @Override
        public int getFullMoveClock() {
            return fullMoveClock;
        }
    }

    private final Deque<PositionStateData> stackPositionStates = new ArrayDeque<PositionStateData>();
    private PositionStateData currentPositionState = new PositionStateData();


    @Override
    public Square getEnPassantSquare() {
        return currentPositionState.enPassantSquare;
    }

    @Override
    public void setEnPassantSquare(Square enPassantSquare) {
        currentPositionState.enPassantSquare = enPassantSquare;
    }

    @Override
    public boolean isCastlingWhiteQueenAllowed() {
        return currentPositionState.castlingWhiteQueenAllowed;
    }

    @Override
    public void setCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        currentPositionState.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
    }

    @Override
    public boolean isCastlingWhiteKingAllowed() {
        return currentPositionState.castlingWhiteKingAllowed;
    }

    @Override
    public void setCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        currentPositionState.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
    }

    @Override
    public boolean isCastlingBlackQueenAllowed() {
        return currentPositionState.castlingBlackQueenAllowed;
    }

    @Override
    public void setCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        currentPositionState.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
    }

    @Override
    public boolean isCastlingBlackKingAllowed() {
        return currentPositionState.castlingBlackKingAllowed;
    }

    @Override
    public void setCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        currentPositionState.castlingBlackKingAllowed = castlingBlackKingAllowed;
    }

    @Override
    public Color getCurrentTurn() {
        return currentPositionState.currentTurn;
    }

    @Override
    public void setCurrentTurn(Color turn) {
        currentPositionState.currentTurn = turn;
    }

    @Override
    public void rollTurn() {
        currentPositionState.currentTurn = currentPositionState.currentTurn.oppositeColor();
    }


    @Override
    public int getHalfMoveClock() {
        return currentPositionState.halfMoveClock;
    }

    @Override
    public void setHalfMoveClock(int halfMoveClock) {
        this.currentPositionState.halfMoveClock = halfMoveClock;
    }

    @Override
    public void incrementHalfMoveClock() {
        this.currentPositionState.halfMoveClock++;
    }

    @Override
    public void resetHalfMoveClock() {
        this.currentPositionState.halfMoveClock = 0;
    }


    @Override
    public int getFullMoveClock() {
        return currentPositionState.fullMoveClock;
    }

    @Override
    public void setFullMoveClock(int fullMoveClock) {
        this.currentPositionState.fullMoveClock = fullMoveClock;
    }

    @Override
    public void incrementFullMoveClock() {
        if (Color.BLACK.equals(currentPositionState.currentTurn)) {
            currentPositionState.fullMoveClock++;
        }
    }

    @Override
    public PositionStateReader getCurrentState(){
        return currentPositionState;
    }

    @Override
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

        stackPositionStates.push(currentPositionState);

        currentPositionState = node;
    }

    @Override
    public void popState() {
        PositionStateData lastState = stackPositionStates.pop();

        currentPositionState = lastState;
    }

    @Override
    public PositionStateWriter clone() throws CloneNotSupportedException {
        PositionStateImp clone = new PositionStateImp();
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
        if (obj instanceof PositionStateImp) {
            PositionStateImp theInstance = (PositionStateImp) obj;
            return Objects.equals(currentPositionState.currentTurn, theInstance.currentPositionState.currentTurn) && Objects.equals(currentPositionState.enPassantSquare, theInstance.currentPositionState.enPassantSquare) && currentPositionState.castlingWhiteQueenAllowed == theInstance.currentPositionState.castlingWhiteQueenAllowed && currentPositionState.castlingWhiteKingAllowed == theInstance.currentPositionState.castlingWhiteKingAllowed && currentPositionState.castlingBlackQueenAllowed == theInstance.currentPositionState.castlingBlackQueenAllowed && currentPositionState.castlingBlackKingAllowed == theInstance.currentPositionState.castlingBlackKingAllowed && currentPositionState.halfMoveClock == theInstance.currentPositionState.halfMoveClock && currentPositionState.fullMoveClock == theInstance.currentPositionState.fullMoveClock;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Turno Actual: " + String.format("%-6s", currentPositionState.currentTurn.toString()) + ", pawnPasanteSquare: " + (currentPositionState.enPassantSquare == null ? "- " : currentPositionState.enPassantSquare.toString()) + ", castlingWhiteQueenAllowed: " + currentPositionState.castlingWhiteQueenAllowed + ", castlingWhiteKingAllowed: " + currentPositionState.castlingWhiteKingAllowed + ", castlingBlackQueenAllowed: " + currentPositionState.castlingBlackQueenAllowed + ", castlingBlackKingAllowed: " + currentPositionState.castlingBlackKingAllowed + ", halfMoveClock: " + currentPositionState.halfMoveClock + ", fullMoveClock: " + currentPositionState.fullMoveClock;
    }
}
