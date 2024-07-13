package net.chesstango.board.representations.epd;

import net.chesstango.board.representations.fen.FEN;

/**
 * @author Mauricio Coria
 */
public class EPDEncoder {

    public String encode(EPD epd) {
        StringBuilder stringBuilder = new StringBuilder();

        FEN fen = epd.getFenWithoutClocks();
        stringBuilder.append(String.format("%s %s %s %s", fen.getPiecePlacement(), fen.getActiveColor(), fen.getCastingsAllowed(), fen.getEnPassantSquare()));

        if (epd.getSuppliedMoveStr() != null) {
            stringBuilder.append(" sm ");
            stringBuilder.append(epd.getSuppliedMoveStr());
            stringBuilder.append(";");
        }

        if (epd.getC0() != null) {
            stringBuilder.append(" c0 \"");
            stringBuilder.append(epd.getC0());
            stringBuilder.append("\";");
        }

        if (epd.getC1() != null) {
            stringBuilder.append(" c1 \"");
            stringBuilder.append(epd.getC1());
            stringBuilder.append("\";");
        }

        if (epd.getC2() != null) {
            stringBuilder.append(" c2 \"");
            stringBuilder.append(epd.getC2());
            stringBuilder.append("\";");
        }

        if (epd.getC3() != null) {
            stringBuilder.append(" c3 \"");
            stringBuilder.append(epd.getC3());
            stringBuilder.append("\";");
        }

        if (epd.getC4() != null) {
            stringBuilder.append(" c4 \"");
            stringBuilder.append(epd.getC4());
            stringBuilder.append("\";");
        }

        if (epd.getC5() != null) {
            stringBuilder.append(" c5 \"");
            stringBuilder.append(epd.getC5());
            stringBuilder.append("\";");
        }

        if (epd.getC6() != null) {
            stringBuilder.append(" c6 \"");
            stringBuilder.append(epd.getC6());
            stringBuilder.append("\";");
        }

        if (epd.getId() != null) {
            stringBuilder.append(" id \"");
            stringBuilder.append(epd.getId());
            stringBuilder.append("\";");
        }

        return stringBuilder.toString();
    }
}
