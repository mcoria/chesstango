package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PGNEncoder {

    public String encode(PGN game) {
        StringBuilder sb = new StringBuilder();

        sb.append("[Event \"").append(game.getEvent() == null ? "?" : game.getEvent()).append("\"]\n");
        sb.append("[Site \"").append(game.getSite() == null ? getComputerName() : game.getSite()).append("\"]\n");
        sb.append("[Date \"").append(game.getDate() == null ? getToday() : game.getDate()).append("\"]\n");
        sb.append("[Round \"").append(game.getRound() == null ? "?" : game.getRound()).append("\"]\n");
        sb.append("[White \"").append(game.getWhite() == null ? "X" : game.getWhite()).append("\"]\n");
        sb.append("[Black \"").append(game.getBlack() == null ? "X" : game.getBlack()).append("\"]\n");
        if (game.getFen() != null && !Objects.equals(FENDecoder.INITIAL_FEN, game.getFen())) {
            sb.append("[FEN \"").append(game.getFen()).append("\"]\n");
        }
        sb.append("[Result \"").append(game.getResult()).append("\"]\n");
        sb.append("\n");

        int moveCounter = 0;
        for (String moveStr: game.getMoveList()) {
            if (moveCounter > 0 && moveCounter % 10 == 0) {
                sb.append("\n");
            }

            if (moveCounter % 2 == 0) {
                if (moveCounter % 10 == 0) {
                    sb.append((moveCounter / 2 + 1)).append(".");
                } else {
                    sb.append(" ").append(moveCounter / 2 + 1).append(".");
                }
            }

            sb.append(" ").append(moveStr);

            moveCounter++;
        }

        sb.append(" ").append(game.getResult());

        return sb.toString();
    }

    private String getToday() {
        String pattern = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    private String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

}
