package net.chesstango.board.representations.pgn;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.representations.SANDecoder;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
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

    @Override
    public String toString() {
        return new PGNEncoder().encode(this);
    }

    public Game buildGame(){
        Game game = FENDecoder.loadGame(this.fen == null ? FENDecoder.INITIAL_FEN : this.fen );

        SANDecoder sanDecoder = new SANDecoder();
        moveList.forEach( moveStr -> {
            MoveContainerReader legalMoves = game.getPossibleMoves();
            Move legalMoveToExecute = sanDecoder.decode(moveStr, legalMoves);
            if(legalMoveToExecute != null) {
                game.executeMove(legalMoveToExecute);
            } else {
                FENEncoder encoder = new FENEncoder();
                game.getChessPosition().constructChessPositionRepresentation(encoder);
                throw new RuntimeException(moveStr + " is not in the list of legal moves for " + encoder.getChessRepresentation());
            }
        });

        return game;
    }


    public static PGNGame createFromGame(Game game){
        PGNGame pgnGame = new PGNGame();
        pgnGame.setResult(encodeGameResult(game));
        pgnGame.setFen(game.getInitialFen());

        List<String> moveList = new ArrayList<>();
        game.accept(new GameVisitor() {
            private final SANEncoder sanEncoder = new SANEncoder();

            private String moveStrTmp = "";

            @Override
            public void visit(GameStateReader gameState) {
                if (!"".equals(moveStrTmp)) {
                    moveStrTmp = moveStrTmp + encodeGameStatusAtMove(gameState.getStatus());
                    moveList.add(moveStrTmp);
                    moveStrTmp = "";
                }

                if (gameState.getSelectedMove() != null) {
                    moveStrTmp = sanEncoder.encode(gameState.getSelectedMove() , gameState.getLegalMoves());
                }
            }
        });
        pgnGame.setMoveList(moveList);

        return pgnGame;
    }

    private static String encodeGameStatusAtMove(GameStatus gameStatus) {
        return switch (gameStatus) {
            case NO_CHECK, DRAW, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> "";
            case CHECK -> "+";
            case MATE -> "#";
            default -> throw new RuntimeException("Invalid game status");
        };
    }

    private static String encodeGameResult(Game game) {
        return switch (game.getStatus()) {
            case NO_CHECK, CHECK -> "*";
            case DRAW, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> "1/2-1/2";
            case MATE -> Color.BLACK.equals(game.getChessPosition().getCurrentTurn()) ? "1-0" : "0-1";
            default -> throw new RuntimeException("Invalid game status");
        };
    }
}
