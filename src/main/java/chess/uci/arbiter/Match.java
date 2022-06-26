package chess.uci.arbiter;

import chess.ai.imp.smart.IterativeDeeping;
import chess.ai.imp.smart.MinMaxPruning;
import chess.ai.imp.smart.evaluation.GameEvaluator;
import chess.board.Color;
import chess.board.Game;
import chess.board.GameState;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.representations.PGNEncoder;
import chess.board.representations.fen.FENDecoder;
import chess.board.representations.fen.FENEncoder;
import chess.uci.arbiter.imp.EngineControllerImp;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;

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
        EngineController engine1 = new EngineControllerImp(new EngineZonda(new IterativeDeeping(new MinMaxPruning(new GameEvaluator()))).disableAsync());
        EngineController engine2 = new EngineControllerImp(new EngineProxy());
        //EngineControllerImp engine2 = new EngineControllerImp(new EngineZonda(new Dummy()));

        Instant start = Instant.now();

        Match match = new Match(engine1, engine2, 1);
        match.startEngines();

        match.play(Arrays.asList(FENDecoder.INITIAL_FEN, "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7", "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));

        match.quitEngines();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: " + timeElapsed.toMillis() + " ms");
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
        boolean fiftyMoveRule = false;
        while (game.getStatus().isInProgress() && !repetition && !fiftyMoveRule) {
            String moveStr = askForBestMove(currentTurn, fen, executedMovesStr);

            Move move = findMove(fen, game, moveStr);
            game.executeMove(move);

            executedMovesStr.add(moveStr);

            repetition = repeatedPosition(game, pastPositions);
            fiftyMoveRule = game.getChessPosition().getHalfMoveClock() < 50 ? false : true;
            currentTurn = (currentTurn == engine1 ? engine2 : engine1);
        }

        MathResult result = null;
        if (repetition || fiftyMoveRule || GameState.Status.DRAW.equals(game.getStatus())) {
            game.getGameState().setStatus(GameState.Status.DRAW);
            result = new MathResult(game, 1, 1);

        } else if (GameState.Status.MATE.equals(game.getStatus())) {
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                result = new MathResult(game, 0, 2);

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                result = new MathResult(game, 2, 0);
            }

        } else {
            throw new RuntimeException("Inconsistent game status");
        }

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            result.setEngineWhite(currentTurn == engine1 ? engine1 : engine2);
            result.setEngineBlack(currentTurn == engine1 ? engine2 : engine1);

        } else {
            result.setEngineBlack(currentTurn == engine1 ? engine1 : engine2);
            result.setEngineWhite(currentTurn == engine1 ? engine2 : engine1);

        }


        if (repetition) {
            System.out.println("DRAW (por repeticion)");
        } else if (fiftyMoveRule) {
            System.out.println("DRAW (por fiftyMoveRule)");
        } else if (GameState.Status.DRAW.equals(game.getStatus())) {
            System.out.println("DRAW");
        } else if (GameState.Status.MATE.equals(game.getStatus()) && Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            System.out.println("MATE " + result.getEngineWhite().getEngineName());
        } else if (GameState.Status.MATE.equals(game.getStatus()) && Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
            System.out.println("MATE " + result.getEngineBlack().getEngineName());
        }

        //printPGN(fen, game);

        //printMoveExecution(fen, game);

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


    private void printPGN(String fen, Game game) {
        System.out.println(game.toString());

        PGNEncoder encoder = new PGNEncoder();
        PGNEncoder.PGNHeader pgnHeader = new PGNEncoder.PGNHeader();

        pgnHeader.setEvent("Computer chess game");
        pgnHeader.setWhite(engine1.getEngineName());
        pgnHeader.setBlack(engine2.getEngineName());
        pgnHeader.setFen(fen);

        System.out.println(encoder.encode(pgnHeader, game));
        //System.out.println("--------------------------------------------------------------------------------");
    }

    private void printMoveExecution(String fen, Game game) {
        Game theGame = FENDecoder.loadGame(fen);

        int counter = 0;
        System.out.println("Game game =  getDefaultGame();");
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
                System.out.println(" // " + (counter / 2 + 1) + " " + fenEncoder.getResult());
            } else {
                System.out.println(" // " + fenEncoder.getResult());
            }

            counter++;
        }
    }

    public static class MathResult {
        private final Game game;

        private final int whitePoints;
        private final int blackPoints;

        private EngineController engineWhite;

        private EngineController engineBlack;


        public MathResult(Game game, int whitePoints, int blackPoints) {
            this.game = game;
            this.whitePoints = whitePoints;
            this.blackPoints = blackPoints;
        }

        public int getWhitePoints() {
            return whitePoints;
        }

        public int getBlackPoints() {
            return blackPoints;
        }


        public EngineController getEngineWhite() {
            return engineWhite;
        }

        public void setEngineWhite(EngineController engineWhite) {
            this.engineWhite = engineWhite;
        }

        public EngineController getEngineBlack() {
            return engineBlack;
        }

        public void setEngineBlack(EngineController engineBlack) {
            this.engineBlack = engineBlack;
        }
    }


}
