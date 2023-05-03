package net.chesstango.search.smart.movesorters;

import net.chesstango.board.moves.Move;

import java.util.Comparator;

class MoveComparator implements Comparator<Move> {

    @Override
    public int compare(Move move1, Move move2) {
        int result = 0;

        if (move1.getTo().getPiece() == null && move2.getTo().getPiece() == null) {
            //Ambos movimientos no son capturas
            result = Math.abs(DefaultMoveSorter.getMoveValue(move1.getFrom().getPiece())) - Math.abs(DefaultMoveSorter.getMoveValue(move2.getFrom().getPiece()));

        } else if (move1.getTo().getPiece() == null && move2.getTo().getPiece() != null) {
            result = -1;

        } else if (move1.getTo().getPiece() != null && move2.getTo().getPiece() == null) {
            result = 1;

        } else if (move1.getTo().getPiece() != null && move2.getTo().getPiece() != null) {
            //Ambos movimientos no son capturas
            result = Math.abs(DefaultMoveSorter.getPieceValue(move1.getTo().getPiece())) - Math.abs(DefaultMoveSorter.getPieceValue(move2.getTo().getPiece()));
            if (result == 0) {
                // Si capturamos, intentamos capturar con la pieza de menos valor primero
                result = Math.abs(DefaultMoveSorter.getMoveValue(move1.getFrom().getPiece())) - Math.abs(DefaultMoveSorter.getMoveValue(move2.getFrom().getPiece()));
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
