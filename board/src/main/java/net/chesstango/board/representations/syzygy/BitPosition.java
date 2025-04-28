package net.chesstango.board.representations.syzygy;

import net.chesstango.board.Color;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionState;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Color.WHITE;

/**
 * @author Mauricio Coria
 */
public class BitPosition {
    long white;
    long black;
    long kings;
    long queens;
    long rooks;
    long bishops;
    long knights;
    long pawns;
    int rule50;
    int castling;
    int ep;
    boolean turn;

    public static BitPosition from(Position chessPosition) {
        BitBoard bitBoard = chessPosition.getBitBoard();
        PositionState positionState = chessPosition.getPositionState();

        BitPosition bitPosition = new BitPosition();

        bitPosition.black = bitBoard.getPositions(Color.BLACK);
        bitPosition.white = bitBoard.getPositions(Color.WHITE);

        bitPosition.kings = bitBoard.getKingPositions();
        bitPosition.queens = bitBoard.getQueenPositions();
        bitPosition.rooks = bitBoard.getRookPositions();
        bitPosition.bishops = bitBoard.getBishopPositions();
        bitPosition.knights = bitBoard.getKnightPositions();
        bitPosition.pawns = bitBoard.getPawnPositions();

        byte rule50 = 0;


        if (positionState.getEnPassantSquare() != null) {
            bitPosition.ep = (byte) positionState.getEnPassantSquare().toIdx();
        }

        bitPosition.turn = positionState.getCurrentTurn() == Color.WHITE;

        bitPosition.castling = positionState.isCastlingWhiteKingAllowed() ||
                positionState.isCastlingWhiteQueenAllowed() ||
                positionState.isCastlingBlackKingAllowed() ||
                positionState.isCastlingBlackQueenAllowed() ? 1 : 0;

        return bitPosition;
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
