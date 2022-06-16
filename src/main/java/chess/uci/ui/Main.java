package chess.uci.ui;

import chess.board.Game;
import chess.board.GameState;
import chess.board.moves.Move;
import chess.board.representations.fen.FENDecoder;
import chess.uci.engine.Engine;
import chess.uci.engine.EngineZonda;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private EngineClient white;
    private EngineClient black;

    private Game game;

    public static void main(String[] args) {
        new Main().compete();
    }

    public Main(){
        Engine engine1 = new EngineZonda();
        white = new EngineClientImp(engine1);

        Engine engine2 = new EngineZonda();
        black = new EngineClientImp(engine2);

        game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
    }

    public void compete(){
        startEngines();
        startNewGame();

        game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        List<String> executedMoves = new ArrayList<>();
        EngineClient currentTurn = white;

        while( !GameState.GameStatus.MATE.equals(game.getGameStatus()) && !GameState.GameStatus.DRAW.equals(game.getGameStatus()) && executedMoves.size() < 100){
            String moveStr = askForBestNove(currentTurn, executedMoves);

            Move move = findMove(moveStr);
            game.executeMove(move);

            executedMoves.add(moveStr);

            currentTurn = (currentTurn == white ? black : white);
        }


        System.out.println("El juego termino: \n" + game.toString());

        quit();
    }

    private String askForBestNove(EngineClient currentTurn, List<String> moves) {
        currentTurn.send_CmdPosition(new CmdPosition(moves));

        RspBestMove bestMove = currentTurn.send_CmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));

        return bestMove.getBestMove();
    }

    private Move findMove(String bestMove) {
        UCIEncoder uciEncoder = new UCIEncoder();
        for (Move move : game.getPossibleMoves()) {
            String encodedMoveStr = uciEncoder.encode(move);
            if (encodedMoveStr.equals(bestMove)) {
                return move;
            }
        }
        throw new RuntimeException("No move found " + bestMove);
    }


    private void startEngines() {
        white.send_CmdUci();
        white.send_CmdIsReady();

        black.send_CmdUci();
        black.send_CmdIsReady();
    }

    private void startNewGame() {
        white.send_CmdUciNewGame();
        white.send_CmdIsReady();

        black.send_CmdUciNewGame();
        black.send_CmdIsReady();
    }

    private void quit() {
        white.send_CmdQuit();
        black.send_CmdQuit();
    }


}
