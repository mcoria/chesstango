package net.chesstango.search.smart.minmax;

import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class MoveSorter {
    public Queue<Move> sortMoves(Iterable<Move> possibleMoves) {

        Queue<Move> queue = new PriorityQueue<Move>(new MoveComparator());

        possibleMoves.forEach(queue::add);

        return queue;
    }

    private static class MoveComparator implements Comparator<Move> {

        @Override
        public int compare(Move move1, Move move2) {
            int result = 0;

            if (move1.getTo().getPiece() == null && move2.getTo().getPiece() == null) {
                //Ambos movimientos no son capturas
                result = Math.abs(getMoveValue(move1.getFrom().getPiece())) - Math.abs(getMoveValue(move2.getFrom().getPiece()));

            } else if (move1.getTo().getPiece() == null && move2.getTo().getPiece() != null) {
                result = -1;

            } else if (move1.getTo().getPiece() != null && move2.getTo().getPiece() == null) {
                result = 1;

            } else if (move1.getTo().getPiece() != null && move2.getTo().getPiece() != null) {
                //Ambos movimientos no son capturas
                result = Math.abs(getPieceValue(move1.getTo().getPiece())) - Math.abs(getPieceValue(move2.getTo().getPiece()));
                if (result == 0) {
                    // Si capturamos, intentamos capturar con la pieza de menos valor primero
                    result = Math.abs(getMoveValue(move1.getFrom().getPiece())) - Math.abs(getMoveValue(move2.getFrom().getPiece()));
                }

            }

            if (move1.equals(move2) && result != 0) {
                throw new RuntimeException("move1.equals(move2) && result != 0");
            } else if (move2.equals(move1) && result != 0) {
                throw new RuntimeException("move2.equals(move1) && result != 0");
            }

            // Ambos son capturas o ambos no son capturas
            return -result;
        }
    }

    static public int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 1;
            case PAWN_BLACK -> -1;
            case KNIGHT_WHITE -> 3;
            case KNIGHT_BLACK -> -3;
            case BISHOP_WHITE -> 3;
            case BISHOP_BLACK -> -3;
            case ROOK_WHITE -> 5;
            case ROOK_BLACK -> -5;
            case QUEEN_WHITE -> 9;
            case QUEEN_BLACK -> -9;
            case KING_WHITE -> 10;
            case KING_BLACK -> -10;
        };
    }

    static public int getMoveValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> 1;
            case KNIGHT_WHITE, KNIGHT_BLACK -> 4;
            case BISHOP_WHITE, BISHOP_BLACK -> 3;
            case ROOK_WHITE, ROOK_BLACK -> 2;
            case QUEEN_WHITE, QUEEN_BLACK -> 4;
            case KING_WHITE, KING_BLACK -> 0;
        };
    }

}
