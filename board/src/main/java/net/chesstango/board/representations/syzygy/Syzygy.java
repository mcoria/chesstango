package net.chesstango.board.representations.syzygy;

import net.chesstango.board.Color;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionState;

/**
 * @author Mauricio Coria
 */
public class Syzygy {
    protected BitPosition toPosition(Position chessPosition) {
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


    protected record BitPosition(long white,
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
    }
}
