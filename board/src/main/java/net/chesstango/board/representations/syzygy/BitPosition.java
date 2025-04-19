package net.chesstango.board.representations.syzygy;

import net.chesstango.board.Color;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionState;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Color.WHITE;

/**
 * @author Mauricio Coria
 */
public record BitPosition(long white,
                          long black,
                          long kings,
                          long queens,
                          long rooks,
                          long bishops,
                          long knights,
                          long pawns,
                          byte rule50,
                          byte ep,
                          boolean turn) {

    public static BitPosition from(Position chessPosition) {
        BitBoard bitBoard = chessPosition.getBitBoard();
        PositionState positionState = chessPosition.getPositionState();
        long white = bitBoard.getPositions(Color.WHITE);
        long black = bitBoard.getPositions(Color.BLACK);
        long kings = bitBoard.getKingPositions();
        long queens = bitBoard.getQueenPositions();
        long rooks = bitBoard.getRookPositions();
        long bishops = bitBoard.getBishopPositions();
        long knights = bitBoard.getKnightPositions();
        long pawns = bitBoard.getPawnPositions();

        byte rule50 = 0;
        byte ep = 0;
        if (positionState.getEnPassantSquare() != null) {
            ep = (byte) positionState.getEnPassantSquare().toIdx();
        }
        boolean turn = positionState.getCurrentTurn() == Color.WHITE;

        return new BitPosition(white,
                black,
                kings,
                queens,
                rooks,
                bishops,
                knights,
                pawns,
                rule50,
                ep,
                turn);
    }

    long pieces_by_type(SyzygyConstants.Color c, SyzygyConstants.PieceType p) {
        long mask = (c == WHITE) ? white : black;
        return switch (p) {
            case PAWN -> pawns & mask;
            case KNIGHT -> knights & mask;
            case BISHOP -> bishops & mask;
            case ROOK -> rooks & mask;
            case QUEEN -> queens & mask;
            case KING -> kings & mask;
        };
    }
}
