package chess.uci.ui;

import chess.board.Game;
import chess.board.GameState;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.representations.PGNEncoder;
import chess.board.representations.fen.FENDecoder;
import chess.board.representations.fen.FENEncoder;
import chess.uci.engine.Engine;
import chess.uci.engine.EngineZonda;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
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

    private String gameFenSeed;

    public static void main(String[] args) {
        Instant start = Instant.now();
        new Main().compete();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: "+ timeElapsed.toMillis() +" ms");
    }

    public Main(){
        engine1 = new EngineZonda(executorService);
        //engine1 = new EngineProxy();
        white = new EngineClientImp(engine1);

        engine2 = new EngineZonda(executorService);
        //engine2 = new EngineProxy();
        black = new EngineClientImp(engine2);

        //gameFenSeed = "1k6/8/8/8/8/8/4K3/8 w - - 0 1";
        gameFenSeed = FENDecoder.INITIAL_FEN;
    }

    public void compete(){
        startEngines();
        startNewGame();

        game = FENDecoder.loadGame(gameFenSeed);
        List<String> executedMovesStr = new ArrayList<>();
        Map<String, Integer> pastPositions = new HashMap<>();
        EngineClient currentTurn = white;

        boolean repetition = false;
        boolean fiftyMoveRule = false;
        while( game.getStatus().isInProgress() && !repetition && !fiftyMoveRule){
            String moveStr = askForBestMove(currentTurn, executedMovesStr);

            Move move = findMove(moveStr);
            game.executeMove(move);

            executedMovesStr.add(moveStr);

            repetition = repeatedPosition(pastPositions);
            fiftyMoveRule = game.getChessPosition().getHalfMoveClock() < 50 ? false : true;
            currentTurn = (currentTurn == white ? black : white);
        }
        if(repetition || fiftyMoveRule){
            game.getGameState().setStatus(GameState.Status.DRAW);
        }

        if(repetition){
            System.out.println("El juego termino por repeticion:");
        } else if (fiftyMoveRule) {
            System.out.println("El juego termino por fiftyMoveRule:");
        } else {
            System.out.println("El juego termino");
        }
        System.out.println(game.toString());

        printPGN();
        //printMoveExecution();

        quit();
    }

    private boolean repeatedPosition(Map<String, Integer> pastPositions) {
        FENEncoder encoder = new FENEncoder();

        game.getChessPosition().constructBoardRepresentation(encoder);

        String fenWithoutClocks = encoder.getFENWithoutClocks();

        int positionCount = pastPositions.computeIfAbsent(fenWithoutClocks, key -> 0);

        positionCount++;

        pastPositions.put(fenWithoutClocks, positionCount);


        return positionCount > 2 ? true : false;
    }

    private void printPGN() {
        PGNEncoder encoder = new PGNEncoder();
        PGNEncoder.PGNHeader pgnHeader = new PGNEncoder.PGNHeader();

        pgnHeader.setEvent("Computer chess game");
        pgnHeader.setSite(getComputerName());
        pgnHeader.setDate(getToday());
        pgnHeader.setRound("?");
        pgnHeader.setWhite(white.getEngineName());
        pgnHeader.setBlack(black.getEngineName());
        pgnHeader.setFen(gameFenSeed);

        System.out.println(encoder.encode(pgnHeader, game));
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

    private void printMoveExecution() {
        Game theGame =  FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        int counter = 0;
        System.out.println("Game game =  getDefaultGame();");
        System.out.println("game");
        Iterator<GameState.GameStateNode> gameStateIterator = game.getGameState().iterateGameStates();
        while (gameStateIterator.hasNext()){
            GameState.GameStateNode gameStateNode = gameStateIterator.next();
            Move move = gameStateNode.selectedMove;
            theGame.executeMove(move);
            System.out.print(".executeMove(Square." + move.getFrom().getKey().toString() + ", Square." + move.getTo().getKey().toString() + ")");

            FENEncoder fenEncoder = new FENEncoder();
            ChessPositionReader theGamePositionReader = theGame.getChessPosition();
            theGamePositionReader.constructBoardRepresentation(fenEncoder);

            if(counter % 2 == 0){
                System.out.println(" // " + (counter / 2 + 1) + " " + fenEncoder.getResult());
            } else {
                System.out.println(" // " + fenEncoder.getResult());
            }

            counter++;
        }
    }

    private String askForBestMove(EngineClient currentTurn, List<String> moves) {
        if(FENDecoder.INITIAL_FEN.equals(gameFenSeed)) {
            currentTurn.send_CmdPosition(new CmdPosition(moves));
        } else {
            currentTurn.send_CmdPosition(new CmdPosition(gameFenSeed, moves));
        }

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

        //executorService.shutdown();
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
