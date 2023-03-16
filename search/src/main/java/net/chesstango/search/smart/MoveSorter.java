package net.chesstango.search.smart;

import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Mauricio Coria
 *
 * DRAW (por fold repetition)
 * DEPTH = 6
 * INITIAL_FEN
 * Time taken: 139447 ms
 *
 *  Nodes visited per search level
 *  ______________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Total Nodes |
 * |      GameEvaluatorSEandImp02 white|       39 |      931 |     6172 |    56943 |   339652 |  2746252 | 13643979 |    16793929 |
 *  ------------------------------------------------------------------------------------------------------------------------------
 *
 *  Nodes visited per search level AVG
 *  ______________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | AVG Nodes/S |
 * |      GameEvaluatorSEandImp02 white|       39 |       23 |      158 |     1460 |     8709 |    70416 |   349845 |      430613 |
 *  ------------------------------------------------------------------------------------------------------------------------------
 *
 *  Max distinct moves per search level
 *  _____________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 |
 * |      GameEvaluatorSEandImp02 white|       47 |       54 |      287 |      406 |     1095 |     1955 |
 *  -----------------------------------------------------------------------------------------------------
 *
 *  Cutoff per search level (higher is better)
 *  _____________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 |
 * |      GameEvaluatorSEandImp02 white|      0 % |     80 % |     64 % |     81 % |     70 % |     84 % |
 *  -----------------------------------------------------------------------------------------------------
 *
 * ==================================================================================================================
 * WHITE WON GameEvaluatorSEandImp02
 * DEPTH = 7
 * INITIAL_FEN
 * Time taken: 504738 ms
 *
 *  Nodes visited per search level
 *  _________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 | Total Nodes |
 * |      GameEvaluatorSEandImp02 white|       14 |      434 |     2362 |    28909 |   125905 |  1320137 |  5655912 | 44790961 |    51924620 |
 *  -----------------------------------------------------------------------------------------------------------------------------------------
 *
 *  Nodes visited per search level AVG
 *  _________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 | AVG Nodes/S |
 * |      GameEvaluatorSEandImp02 white|       14 |       31 |      168 |     2064 |     8993 |    94295 |   403993 |  3199354 |     3708901 |
 *  -----------------------------------------------------------------------------------------------------------------------------------------
 *
 *  Max distinct moves per search level
 *  ________________________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 |
 * |      GameEvaluatorSEandImp02 white|       45 |       64 |      339 |      613 |     1345 |     2444 |     4095 |
 *  ----------------------------------------------------------------------------------------------------------------
 *
 *  Cutoff per search level (higher is better)
 *  ________________________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 |
 * |      GameEvaluatorSEandImp02 white|     16 % |     84 % |     65 % |     87 % |     71 % |     86 % |     78 % |
 *  ----------------------------------------------------------------------------------------------------------------
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
