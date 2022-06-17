package chess.uci.ui;

import chess.board.Game;
import chess.board.GameState;
import chess.board.moves.Move;
import chess.board.representations.fen.FENDecoder;
import chess.uci.engine.Engine;
import chess.uci.engine.EngineProxy;
import chess.uci.engine.EngineZonda;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final EngineClient white;
    private final EngineClient black;

    private final Engine engine1;
    private final Engine engine2;

    private Game game;

    public static void main(String[] args) {
        new Main().compete();
    }

    public Main(){
        engine1 = new EngineZonda(executorService);
        white = new EngineClientImp(engine1);

        //engine2 = new EngineZonda(executorService);
        engine2 = new EngineProxy();
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
            String moveStr = askForBestMove(currentTurn, executedMoves);

            Move move = findMove(moveStr);
            game.executeMove(move);

            executedMoves.add(moveStr);

            currentTurn = (currentTurn == white ? black : white);
        }


        System.out.println("El juego termino: \n" + game.toString());

        printPGN();

        quit();
    }

    private void printPGN() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Event \"Arena match\"]\n" +
                "[Site \"Kano's computer\"]\n" +
                "[Date \"2022.16.06\"]\n" +
                "[Round \"29\"]\n" +
                "[White \"" + white.getEngineAuthor() + "\"]\n" +
                "[Black \"" + black.getEngineAuthor() + "\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n");

        sb.append(
                "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 {This opening is called the Ruy Lopez.}\n" +
                "4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 O-O 9. h3 Nb8 10. d4 Nbd7\n" +
                "11. c4 c6 12. cxb5 axb5 13. Nc3 Bb7 14. Bg5 b4 15. Nb1 h6 16. Bh4 c5 17. dxe5\n" +
                "Nxe4 18. Bxe7 Qxe7 19. exd6 Qf6 20. Nbd2 Nxd6 21. Nc4 Nxc4 22. Bxc4 Nb6\n" +
                "23. Ne5 Rae8 24. Bxf7+ Rxf7 25. Nxf7 Rxe1+ 26. Qxe1 Kxf7 27. Qe3 Qg5 28. Qxg5\n" +
                "hxg5 29. b3 Ke6 30. a3 Kd6 31. axb4 cxb4 32. Ra5 Nd5 33. f3 Bc8 34. Kf2 Bf5\n" +
                "35. Ra7 g6 36. Ra6+ Kc5 37. Ke1 Nf4 38. g3 Nxh3 39. Kd2 Kb5 40. Rd6 Kc5 41. Ra6\n" +
                "Nf2 42. g4 Bd3 43. Re6 1/2-1/2");

        System.out.println(sb.toString());
    }

    private void printMoves(List<String> executedMoves) {
        System.out.print("Moves:");
        executedMoves.stream().forEach(move -> System.out.print(" " + move));
        System.out.println("");
    }

    private String askForBestMove(EngineClient currentTurn, List<String> moves) {
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
        if(engine1 instanceof  Runnable){
            executorService.execute((Runnable) engine1);
        }
        white.send_CmdUci();
        white.send_CmdIsReady();

        if(engine2 instanceof  Runnable){
            executorService.execute((Runnable) engine2);
        }
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

        executorService.shutdown();
        try {
            boolean terminated = executorService.awaitTermination(2000, TimeUnit.MILLISECONDS);
            if(terminated == false) {
                throw new RuntimeException("El thread no termino");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
