package chess.board.representations;

import chess.board.Color;
import chess.board.Game;
import chess.board.GameState;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public class PGNEncoder {

    private SANEncoder sanEncoder = new SANEncoder();

    public String encode(PGNHeader header, Game game) {
        StringBuilder sb = new StringBuilder();

        sb.append("[Event \"" + header.getEvent() + "\"]\n");
        sb.append("[Site \"" + header.getSite() + "\"]\n");
        sb.append("[Date \"" + header.getDate() + "\"]\n");
        sb.append("[Round \"" + header.getRound() + "\"]\n");
        sb.append("[White \"" + header.getWhite() + "\"]\n");
        sb.append("[Black \"" + header.getBlack() + "\"]\n");


        sb.append("[Result \"" + encodeGameResult(game, false) +"\"]\n");

        sb.append("\n");

        int moveCounter = 0;
        Iterator<GameState.GameStateNode> gameStateIterator = game.getGameState().iterateGameStates();
        while (gameStateIterator.hasNext()){
            GameState.GameStateNode gameStateNode = gameStateIterator.next();

            String encodedMove = sanEncoder.encode(gameStateNode.selectedMove, gameStateNode.legalMoves);

            if(moveCounter > 0 && moveCounter % 10 == 0){
                sb.append("\n");
            }

            if(moveCounter % 2 == 0) {
                if (moveCounter % 10 == 0) {
                    sb.append((moveCounter / 2 + 1) + ".");
                } else {
                    sb.append(" " + (moveCounter / 2 + 1) + ".");
                }
            }


            sb.append(" " + encodedMove);

            moveCounter++;
        }


        sb.append(encodeGameResult(game, true));

        return sb.toString();
    }

    private String encodeGameResult(Game game, boolean forMoveList) {
        switch (game.getGameStatus()){
            case IN_PROGRESS:
                return (forMoveList ? " " : "") +  "*";
            case MATE:
                return (forMoveList ? "# " : "") + (Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn())  ? "1-0" : "0-1");
            default:
                throw new RuntimeException("Invalid game status");
        }
    }


    public static class PGNHeader{
        private String event;
        private String site;
        private String date;
        private String round;
        private String white;
        private String black;

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
    }
}
