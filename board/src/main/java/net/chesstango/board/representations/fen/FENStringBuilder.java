package net.chesstango.board.representations.fen;

/**
 * @author Mauricio Coria
 */
public class FENStringBuilder extends AbstractFENBuilder<String> {

    @Override
    public String getPositionRepresentation() {
        String piecePlacement = getPiecePlacement();
        String activeColor = getTurno();
        String castingsAllowed = getEnroques();
        String enPassantSquare = getEnPassant();
        String halfMoveClock = getHalfMoveClock();
        String fullMoveClock = getFullMoveClock();

        return String.format("%s %s %s %s %s %s", piecePlacement, activeColor, castingsAllowed, enPassantSquare, halfMoveClock, fullMoveClock);
    }

}
