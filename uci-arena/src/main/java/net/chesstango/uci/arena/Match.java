package net.chesstango.uci.arena;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.PGNEncoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.uci.arena.imp.EngineControllerImp;
import net.chesstango.uci.engine.EngineProxy;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author Mauricio Coria
 */
public class Match {
    private EngineController engine1;
    private EngineController engine2;

    private final int depth;

    public static void main(String[] args) {
        EngineController engineTango = new EngineControllerImp(new EngineTango().disableAsync());
        EngineController engineOponente = new EngineControllerImp(new EngineProxy());
        //EngineControllerImp engineOponente = new EngineControllerImp(new EngineTango(new Dummy()));

        Instant start = Instant.now();

        Match match = new Match(engineTango, engineOponente, 1);
        match.startEngines();

        List<MathResult> matchResult = match.play(Arrays.asList(
                FENDecoder.INITIAL_FEN,
                "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20",
                "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7",
                "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"
                ));

        match.quitEngines();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: " + timeElapsed.toMillis() + " ms");

        long puntosAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engineTango).mapToLong(result -> result.getPoints()).sum();
        long puntosAsBlack = (-1) * matchResult.stream().filter(result -> result.getEngineBlack() == engineTango).mapToLong(result -> result.getPoints()).sum();
        long puntosTotal = puntosAsWhite + puntosAsBlack;

        System.out.println("Puntos withe = " + puntosAsWhite + ", puntos black = " + puntosAsBlack + ", total = " + puntosTotal);
    }

    public Match(EngineController engine1, EngineController engine2, int depth) {
        this.engine1 = engine1;
        this.engine2 = engine2;
        this.depth = depth;
    }

    public void switchChairs() {
        EngineController tmpController = engine1;
        engine1 = engine2;
        engine2 = tmpController;
    }

    public void startEngines() {
        engine1.send_CmdUci();
        engine1.send_CmdIsReady();

        engine2.send_CmdUci();
        engine2.send_CmdIsReady();
    }

    public void quitEngines() {
        engine1.send_CmdQuit();
        engine2.send_CmdQuit();
    }

    public List<MathResult> play(List<String> fenList) {
        List<MathResult> result = new ArrayList<>();

        fenList.stream().forEach(fen -> {
            result.addAll(play(fen));
        });

        return result;
    }

    public List<MathResult> play(String fen) {
        List<MathResult> result = new ArrayList<>();

        result.add(compete(fen));
        switchChairs();
        result.add(compete(fen));

        return result;
    }

    public List<MathResult> compete(List<String> fenList) {
        List<MathResult> result = new ArrayList<>();

        fenList.stream().forEach(fen -> {
            result.add(compete(fen));
        });

        return result;
    }

    public MathResult compete(String fen) {
        startNewGame();

        Game game = FENDecoder.loadGame(fen);

        List<String> executedMovesStr = new ArrayList<>();
        Map<String, Integer> pastPositions = new HashMap<>();
        EngineController currentTurn = engine1;

        boolean repetition = false;
        while (game.getStatus().isInProgress() && !repetition) {
            String moveStr = askForBestMove(currentTurn, fen, executedMovesStr);

            Move move = findMove(fen, game, moveStr);
            game.executeMove(move);

            executedMovesStr.add(moveStr);

            repetition = repeatedPosition(game, pastPositions);
            currentTurn = (currentTurn == engine1 ? engine2 : engine1);
        }

        MathResult result = null;
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            EngineController white = currentTurn == engine1 ? engine1 : engine2;
            EngineController black = white == engine1 ? engine2 : engine1;
            result = new MathResult(game, white, black);

        } else {
            EngineController black = currentTurn == engine1 ? engine1 : engine2;
            EngineController white = black == engine1 ? engine2 : engine1;
            result = new MathResult(game, white, black);
        }

        int materialPoints = GameEvaluator.evaluateByMaterial(game);
        if (repetition) {
            game.getGameState().setStatus(GameState.Status.DRAW);
            System.out.println("DRAW (por repeticion)");
            result.setPoints(materialPoints);

        } else if (GameState.Status.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            game.getGameState().setStatus(GameState.Status.DRAW);
            System.out.println("DRAW (por fiftyMoveRule)");
            result.setPoints(materialPoints);

        } else if (GameState.Status.DRAW.equals(game.getStatus())) {
            game.getGameState().setStatus(GameState.Status.DRAW);
            System.out.println("DRAW");
            result.setPoints(materialPoints);


        } else if (GameState.Status.MATE.equals(game.getStatus())) {
            if(Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("MATE WHITE " + result.getEngineWhite().getEngineName());
                result.setPoints(GameEvaluator.WHITE_LOST);
            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("MATE BLACK " + result.getEngineBlack().getEngineName());
                result.setPoints(GameEvaluator.BLACK_LOST);
            }
        } else {
            throw new RuntimeException("Inconsistent game status");
        }


        //printDebug(fen, game);

        return result;
    }


    protected void startNewGame() {
        engine1.send_CmdUciNewGame();
        engine1.send_CmdIsReady();

        engine2.send_CmdUciNewGame();
        engine2.send_CmdIsReady();
    }

    private boolean repeatedPosition(Game game, Map<String, Integer> pastPositions) {
        FENEncoder encoder = new FENEncoder();

        game.getChessPosition().constructBoardRepresentation(encoder);

        String fenWithoutClocks = encoder.getFENWithoutClocks();

        int positionCount = pastPositions.computeIfAbsent(fenWithoutClocks, key -> 0);

        positionCount++;

        pastPositions.put(fenWithoutClocks, positionCount);

        return positionCount > 2 ? true : false;
    }

    private String askForBestMove(EngineController currentTurn, String fen, List<String> moves) {
        if (FENDecoder.INITIAL_FEN.equals(fen)) {
            currentTurn.send_CmdPosition(new CmdPosition(moves));
        } else {
            currentTurn.send_CmdPosition(new CmdPosition(fen, moves));
        }

        RspBestMove bestMove = currentTurn.send_CmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(depth));

        return bestMove.getBestMove();
    }

    private Move findMove(String fen, Game game, String bestMove) {
        UCIEncoder uciEncoder = new UCIEncoder();
        for (Move move : game.getPossibleMoves()) {
            String encodedMoveStr = uciEncoder.encode(move);
            if (encodedMoveStr.equals(bestMove)) {
                return move;
            }
        }
        printPGN(fen, game);
        printMoveExecution(fen, game);
        throw new RuntimeException("No move found " + bestMove);
    }

    private void printDebug(String fen, Game game) {
        System.out.println(game.toString());

        System.out.println();

        printPGN(fen, game);

        System.out.println();

        printMoveExecution(fen, game);

        System.out.println("--------------------------------------------------------------------------------");
    }

    private void printPGN(String fen, Game game) {
        PGNEncoder encoder = new PGNEncoder();
        PGNEncoder.PGNHeader pgnHeader = new PGNEncoder.PGNHeader();

        pgnHeader.setEvent("Computer chess game");
        pgnHeader.setWhite(engine1.getEngineName());
        pgnHeader.setBlack(engine2.getEngineName());
        pgnHeader.setFen(fen);

        System.out.println(encoder.encode(pgnHeader, game));
    }

    private void printMoveExecution(String fen, Game game) {
        Game theGame = FENDecoder.loadGame(fen);

        int counter = 0;
        System.out.println("Game game = getDefaultGame();");
        System.out.println("game");
        Iterator<GameState.GameStateData> gameStateIterator = game.getGameState().iterateGameStates();
        while (gameStateIterator.hasNext()) {
            GameState.GameStateData gameStateData = gameStateIterator.next();
            Move move = gameStateData.selectedMove;
            theGame.executeMove(move);
            System.out.print(".executeMove(Square." + move.getFrom().getKey().toString() + ", Square." + move.getTo().getKey().toString() + ")");

            FENEncoder fenEncoder = new FENEncoder();
            ChessPositionReader theGamePositionReader = theGame.getChessPosition();
            theGamePositionReader.constructBoardRepresentation(fenEncoder);

            if (counter % 2 == 0) {
                System.out.println(" // " + (counter / 2 + 1) + " " + fenEncoder.getChessRepresentation());
            } else {
                System.out.println(" // " + fenEncoder.getChessRepresentation());
            }

            counter++;
        }
    }


}
