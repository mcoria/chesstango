package net.chesstango.board.representations.fen;

import lombok.Setter;
import net.chesstango.board.representations.polyglot.PolyglotKeyBuilder;

/**
 * @author Mauricio Coria
 */
public class FENStringBuilder extends AbstractFENBuilder<String> {

    @Setter
    boolean ignoreEnPassantSquareIfNotCapturePresente;

    @Setter
    boolean ignoreClocks;

    @Override
    public String getPositionRepresentation() {
        String piecePlacement = getPiecePlacement();
        String activeColor = getTurno();
        String castingsAllowed = getEnroques();
        String enPassantSquare = getEnPassant();
        String halfMoveClock = getHalfMoveClock();
        String fullMoveClock = getFullMoveClock();


        StringBuilder sb = new StringBuilder();

        sb.append(piecePlacement);
        sb.append(" ").append(activeColor);
        sb.append(" ").append(castingsAllowed);

        if (!ignoreEnPassantSquareIfNotCapturePresente) {
            sb.append(" ").append(enPassantSquare);
        } else if (PolyglotKeyBuilder.pawnsAttackingEnPassantSquare(this.whiteTurn, this.whitePositions, this.blackPositions, this.pawnPositions, this.enPassantSquare) != 0) {
            sb.append(" ").append(enPassantSquare);
        } else {
            sb.append(" ").append("-");
        }

        if (!ignoreClocks) {
            sb.append(" ").append(halfMoveClock);
            sb.append(" ").append(fullMoveClock);
        }

        return sb.toString();
    }

}
