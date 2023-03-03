package net.chesstango.board.representations.pgn;

import net.chesstango.board.*;
import net.chesstango.board.representations.SANEncoder;

import java.util.ArrayList;
import java.util.List;

public class PGNGame {
    private String event;
    private String site;
    private String date;
    private String round;
    private String white;
    private String black;
    private String fen;
    private String result;

    private List<String> moveList;


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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getMoveList() {
        return moveList;
    }

    public void setMoveList(List<String> moveList) {
        this.moveList = moveList;
    }



    public static PGNGame createFromGame(Game game){
        PGNGame pgnGame = new PGNGame();

        pgnGame.setResult(encodeGameResult(game));

        List<String> moveList = new ArrayList<>();

        game.accept(new GameVisitor() {
            private SANEncoder sanEncoder = new SANEncoder();

            private String moveStrTmp = "";

            @Override
            public void visit(GameState gameState) {
            }

            @Override
            public void visit(GameState.GameStateData gameStateData) {
                if (!"".equals(moveStrTmp)) {
                    moveStrTmp = moveStrTmp + encodeGameStatusAtMove(gameStateData.gameStatus);
                    moveList.add(moveStrTmp);
                    moveStrTmp = "";
                }

                if (gameStateData.selectedMove != null) {
                    moveStrTmp = sanEncoder.encode(gameStateData.selectedMove, gameStateData.legalMoves);
                }
            }
        });

        pgnGame.setMoveList(moveList);

        return pgnGame;
    }

    public static String encodeGameStatusAtMove(GameStatus gameStatus) {
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

    public static String encodeGameResult(Game game) {
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
}
