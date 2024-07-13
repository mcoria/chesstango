package net.chesstango.board.representations.epd;

/**
 * @author Mauricio Coria
 */
public class EPDEncoder {
    public String encode(EPD epd) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(epd.getFen().toString());

        if (epd.getSuppliedMoveStr() != null) {
            stringBuilder.append(" sm ");
            stringBuilder.append(epd.getSuppliedMoveStr());
            stringBuilder.append(";");
        }

        if (epd.getId() != null) {
            stringBuilder.append(" id \"");
            stringBuilder.append(epd.getId());
            stringBuilder.append("\";");
        }

        return stringBuilder.toString();
    }
}
