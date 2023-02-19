package net.chesstango.board.representations;

import net.chesstango.board.*;
import net.chesstango.board.representations.fen.FENDecoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mauricio Coria
 */
public class PGNEncoder {

    private SANEncoder sanEncoder = new SANEncoder();

    public String encode(PGNHeader header, Game game) {
        StringBuilder sb = new StringBuilder();

        sb.append("[Event \"" + header.getEvent() + "\"]\n");
        sb.append("[Site \"" + (header.getSite() == null ? getComputerName() : header.getSite()) + "\"]\n");
        sb.append("[Date \"" + (header.getDate() == null ? getToday() : header.getDate()) + "\"]\n");
        sb.append("[Round \"" + (header.getRound() == null ? "?" : header.getRound()) + "\"]\n");
        sb.append("[White \"" + header.getWhite() + "\"]\n");
        sb.append("[Black \"" + header.getBlack() + "\"]\n");
        if (header.getFen() != null && !Objects.equals(FENDecoder.INITIAL_FEN, header.getFen())) {
            sb.append("[FEN \"" + header.getFen() + "\"]\n");
        }
        sb.append("[Result \"" + encodeGameResult(game) + "\"]\n");
        sb.append("\n");

        AtomicInteger moveCounter = new AtomicInteger();
        game.accept(new GameStateVisitor() {

            @Override
            public void visit(GameState gameState) {

            }

            @Override
            public void visit(GameState.GameStateData gameStateData) {
                if (gameStateData.selectedMove != null) {
                    String encodedMove = sanEncoder.encode(gameStateData.selectedMove, gameStateData.legalMoves);

                    int moveCounterValue = moveCounter.get();
                    if (moveCounterValue > 0) {
                        sb.append(encodeGameStatusAtMove(gameStateData.gameStatus));
                    }

                    if (moveCounterValue > 0 && moveCounterValue % 10 == 0) {
                        sb.append("\n");
                    }

                    if (moveCounterValue % 2 == 0) {
                        if (moveCounterValue % 10 == 0) {
                            sb.append((moveCounterValue / 2 + 1) + ".");
                        } else {
                            sb.append(" " + (moveCounterValue / 2 + 1) + ".");
                        }
                    }

                    sb.append(" " + encodedMove);

                    moveCounter.incrementAndGet();
                }
            }
        });

        sb.append(encodeGameStatusAtMove(game.getStatus()));

        sb.append(" " + encodeGameResult(game));

        return sb.toString();
    }

    private String encodeGameStatusAtMove(GameStatus gameStatus) {
        switch (gameStatus) {
            case NO_CHECK:
            case DRAW:
            case DRAW_BY_FIFTY_RULE:
            case DRAW_BY_FOLD_REPETITION:
                return "";
            case CHECK:
                return "+";
            case MATE:
                return "#";
            default:
                throw new RuntimeException("Invalid game status");
        }
    }

    private String encodeGameResult(Game game) {
        switch (game.getStatus()) {
            case NO_CHECK:
            case CHECK:
                return "*";
            case DRAW:
            case DRAW_BY_FIFTY_RULE:
            case DRAW_BY_FOLD_REPETITION:
                return "1/2-1/2";
            case MATE:
                return Color.BLACK.equals(game.getChessPosition().getCurrentTurn()) ? "1-0" : "0-1";
            default:
                throw new RuntimeException("Invalid game status");
        }
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

    public static class PGNHeader {
        private String event;
        private String site;
        private String date;
        private String round;
        private String white;
        private String black;
        private String fen;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }

        public String getWhite() {
            return white;
        }

        public void setWhite(String white) {
            this.white = white;
        }

        public String getBlack() {
            return black;
        }

        public void setBlack(String black) {
            this.black = black;
        }

        public String getFen() {
            return fen;
        }

        public void setFen(String fen) {
            this.fen = fen;
        }
    }

}
