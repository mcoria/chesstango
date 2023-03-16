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

    public String encode(PGNGame game) {
        StringBuilder sb = new StringBuilder();

        sb.append("[Event \"" + (game.getEvent() == null ? "?" : game.getEvent()) + "\"]\n");
        sb.append("[Site \"" + (game.getSite() == null ? getComputerName() : game.getSite()) + "\"]\n");
        sb.append("[Date \"" + (game.getDate() == null ? getToday() : game.getDate()) + "\"]\n");
        sb.append("[Round \"" + (game.getRound() == null ? "?" : game.getRound()) + "\"]\n");
        sb.append("[White \"" + (game.getWhite() == null ? "X" : game.getWhite()) + "\"]\n");
        sb.append("[Black \"" + (game.getBlack() == null ? "X" : game.getBlack()) + "\"]\n");
        if (game.getFen() != null && !Objects.equals(FENDecoder.INITIAL_FEN, game.getFen())) {
            sb.append("[FEN \"" + game.getFen() + "\"]\n");
        }
        sb.append("[Result \"" + game.getResult() + "\"]\n");
        sb.append("\n");

        int moveCounter = 0;
        for (String moveStr: game.getMoveList()) {
            if (moveCounter > 0 && moveCounter % 10 == 0) {
                sb.append("\n");
            }

            if (moveCounter % 2 == 0) {
                if (moveCounter % 10 == 0) {
                    sb.append((moveCounter / 2 + 1) + ".");
                } else {
                    sb.append(" " + (moveCounter / 2 + 1) + ".");
                }
            }

            sb.append(" " + moveStr);

            moveCounter++;
        }

        sb.append(" " + game.getResult());

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

    public static String encodeGame(Game game){
        PGNGame pgnGame = PGNGame.createFromGame(game);
        PGNEncoder encoder = new PGNEncoder();
        return encoder.encode(pgnGame);
    }

}
