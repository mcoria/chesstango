package chess.uci.ui;

import chess.board.Game;
import chess.board.GameState;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.representations.PGNEncoder;
import chess.board.representations.fen.FENDecoder;
import chess.board.representations.fen.FENEncoder;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import chess.uci.protocol.UCIEncoder;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.ui.imp.EngineControllerImp;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author Mauricio Coria
 *
 */
public class Match {
    private EngineController white;
    private EngineController black;

    private Game game;

    private String fen;

    public static void main(String[] args) {
        EngineControllerImp engine1 = new EngineControllerImp(new EngineZonda());
        EngineControllerImp engine2 = new EngineControllerImp(new EngineProxy());

        Instant start = Instant.now();

        Match match = new Match(engine1, engine2);
        match.startEngines();

        match.compete(FENDecoder.INITIAL_FEN);
        match.switchChairs();
        match.compete(FENDecoder.INITIAL_FEN);

        match.quitEngines();


        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: "+ timeElapsed.toMillis() +" ms");
    }

    public Match(EngineController white, EngineController black){
        this.white = white;
        this.black = black;
    }

    public void switchChairs() {
        EngineController tmpController = white;
        white = black;
        black = tmpController;
    }

    public void startEngines() {
        white.send_CmdUci();
        white.send_CmdIsReady();

        black.send_CmdUci();
        black.send_CmdIsReady();
    }

    public void quitEngines() {
        white.send_CmdQuit();
        black.send_CmdQuit();
    }

    public MathResult compete(String fen){
        startNewGame();

        this.fen = fen;
        this.game = FENDecoder.loadGame(this.fen);

        List<String> executedMovesStr = new ArrayList<>();
        Map<String, Integer> pastPositions = new HashMap<>();
        EngineController currentTurn = white;

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

        MathResult result = null;
        if(repetition || fiftyMoveRule){
            game.getGameState().setStatus(GameState.Status.DRAW);
            result = new MathResult(1, 1);
        } else if(GameState.Status.DRAW.equals(game.getStatus())) {
            result = new MathResult(1, 1);
        } else if(GameState.Status.MATE.equals(game.getStatus()) && currentTurn == white) {
            result = new MathResult(2, 0);
        } else if(GameState.Status.MATE.equals(game.getStatus()) && currentTurn == black) {
            result = new MathResult(0, 2);
        } else {
            throw new RuntimeException("Inconsistent game status");
        }

        if(repetition){
            System.out.println("El juego termino por repeticion:");
        } else if (fiftyMoveRule) {
            System.out.println("El juego termino por fiftyMoveRule:");
        } else {
            System.out.println("El juego termino por mate o draw");
        }
        System.out.println(game.toString());

        printPGN();
        //printMoveExecution();

        return result;
    }

    protected void startNewGame() {
        white.send_CmdUciNewGame();
        white.send_CmdIsReady();

        black.send_CmdUciNewGame();
        black.send_CmdIsReady();
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

    private String askForBestMove(EngineController currentTurn, List<String> moves) {
        if(FENDecoder.INITIAL_FEN.equals(fen)) {
            currentTurn.send_CmdPosition(new CmdPosition(moves));
        } else {
            currentTurn.send_CmdPosition(new CmdPosition(fen, moves));
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


    private void printPGN() {
        PGNEncoder encoder = new PGNEncoder();
        PGNEncoder.PGNHeader pgnHeader = new PGNEncoder.PGNHeader();

        pgnHeader.setEvent("Computer chess game");
        pgnHeader.setWhite(white.getEngineName());
        pgnHeader.setBlack(black.getEngineName());
        pgnHeader.setFen(fen);

        System.out.println(encoder.encode(pgnHeader, game));
    }

    private void printMoveExecution() {
        Game theGame =  FENDecoder.loadGame(fen);

        int counter = 0;
        System.out.println("Game game =  getDefaultGame();");
        System.out.println("game");
        Iterator<GameState.GameStateData> gameStateIterator = game.getGameState().iterateGameStates();
        while (gameStateIterator.hasNext()){
            GameState.GameStateData gameStateData = gameStateIterator.next();
            Move move = gameStateData.selectedMove;
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

    public static class MathResult {
        private final int whitePoints;
        private final int blackPoints;


        public MathResult(int whitePoints, int blackPoints) {
            this.whitePoints = whitePoints;
            this.blackPoints = blackPoints;
        }

        public int getWhitePoints() {
            return whitePoints;
        }

        public int getBlackPoints() {
            return blackPoints;
        }
    }

}
