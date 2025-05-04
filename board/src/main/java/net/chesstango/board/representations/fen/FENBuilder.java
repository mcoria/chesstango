package net.chesstango.board.representations.fen;

/**
 * @author Mauricio Coria
 */
public class FENBuilder extends AbstractFENBuilder<FEN> {

    @Override
    public FEN getPositionRepresentation() {
        String piecePlacement = getPiecePlacement();
        String activeColor = getTurno();
        String castingsAllowed = getEnroques();
        String enPassantSquare = getEnPassant();
        String halfMoveClock = getHalfMoveClock();
        String fullMoveClock = getFullMoveClock();

        return new FEN(piecePlacement,
                activeColor,
                castingsAllowed,
                enPassantSquare,
                halfMoveClock,
                fullMoveClock);
    }
}
