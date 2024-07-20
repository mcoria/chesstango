package net.chesstango.tools.epdfilters;

import net.chesstango.board.representations.epd.EPD;

import java.util.function.Predicate;


/**
 * @author Mauricio Coria
 */
public class PlayerFilter implements Predicate<EPD> {

    private final String playerName;

    public PlayerFilter(String playerName) {
        this.playerName = playerName.toLowerCase();
    }

    @Override
    public boolean test(EPD epd) {
        if (epd.getC3() != null) {
            if ("w".equals(epd.getFenWithoutClocks().getActiveColor()) &&
                    epd.getC3().toLowerCase().contains(playerName)) {
                return true;
            }
        }

        if (epd.getC4() != null) {
            if ("b".equals(epd.getFenWithoutClocks().getActiveColor()) &&
                    epd.getC4().toLowerCase().contains(playerName)) {
                return true;
            }
        }

        return false;
    }
}
