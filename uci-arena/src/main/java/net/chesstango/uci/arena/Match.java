package net.chesstango.uci.arena;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.GameDebugEncoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.board.representations.pgn.PGNGame;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Match {
    public static final int WINNER_POINTS = 1000;
    private final EngineController controller1;
    private final EngineController controller2;
    private final int depth;
    private EngineController white;
    private EngineController black;
    private String fen;
    private Game game;
    private boolean debugEnabled;
    private boolean switchChairs;
    private MatchListener matchListener;


    public Match(EngineController controller1, EngineController controller2, int depth) {
        this.controller1 = controller1;
        this.controller2 = controller2;
        this.depth = depth;
        this.switchChairs = true;
    }

    public Match setMatchListener(MatchListener matchListener) {
        this.matchListener = matchListener;
        return this;
    }

    public List<GameResult> play(List<String> fenList) {
        List<GameResult> result = new ArrayList<>();

        fenList.stream().forEach(fen -> {
            result.addAll(play(fen));
        });

        return result;
    }

    public List<GameResult> play(String fen) {
        List<GameResult> result = new ArrayList<>();

        try {
            setFen(fen);

            result.add(play(controller1, controller2));

            if (switchChairs) {
                result.add(play(controller2, controller1));
            }

        } catch (RuntimeException e) {
            System.err.println("Error playing fen:" + fen);

            printPGN(System.err);

            throw e;
        }

        return result;
    }

    protected GameResult play(EngineController white, EngineController black) {
        setChairs(white, black);

        startNewGame();

        compete();

        GameResult gameResult = createResult();

        if (matchListener != null) {
            matchListener.notifyEndGame(game, gameResult);
        }

        return gameResult;
    }


    protected void compete() {
        this.game = FENDecoder.loadGame(fen);
        this.game.detectRepetitions(true);

        final List<String> executedMovesStr = new ArrayList<>();

        EngineController currentTurn;

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            currentTurn = white;
        } else {
            currentTurn = black;
        }

        if (matchListener != null) {
            matchListener.notifyNewGame(game, white, black);
        }

        while (game.getStatus().isInProgress()) {

            String moveStr = retrieveBestMoveFromController(currentTurn, executedMovesStr);

            Move move = UCIEncoder.selectMove(game.getPossibleMoves(), moveStr);

            if (move == null) {
                printDebug(System.err);
                throw new RuntimeException(String.format("No move found %s", moveStr));
            }

            game.executeMove(move);

            executedMovesStr.add(moveStr);

            currentTurn = (currentTurn == white ? black : white);

            if (matchListener != null) {
                matchListener.notifyMove(game, move);
            }
        }
    }

    public Match setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    public Match switchChairs(boolean switchChairs) {
        this.switchChairs = switchChairs;
        return this;
    }

    protected void setFen(String fen) {
        this.fen = fen;
    }

    protected void setChairs(EngineController white, EngineController black) {
        if (white != this.controller1 && white != this.controller2 || black != this.controller1 && black != this.controller2) {
            throw new RuntimeException("Invalid opponents");
        }
        this.white = white;
        this.black = black;
    }

    protected void setGame(Game game) {
        this.game = game;
    }

    //TODO: cambiar el metodo para evaluar los puntos, son demasiados los puntos en caso de ganar
    protected GameResult createResult() {
        int matchPoints = 0;
        EngineController winner = null;

        if (GameStatus.DRAW_BY_FOLD_REPETITION.equals(game.getStatus())) {
            System.out.println("DRAW (por fold repetition)");
            matchPoints = material(game, true);

        } else if (GameStatus.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            System.out.println("DRAW (por fiftyMoveRule)");
            matchPoints = material(game, true);

        } else if (GameStatus.DRAW.equals(game.getStatus())) {
            System.out.println("DRAW");
            matchPoints = material(game, true);

        } else if (GameStatus.MATE.equals(game.getStatus())) {
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("BLACK WON " + black.getEngineName());
                matchPoints = -1 * (WINNER_POINTS + material(game, false));
                winner = black;

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                System.out.println("WHITE WON " + white.getEngineName());
                matchPoints = (WINNER_POINTS + material(game, false));
                winner = white;

            }
        } else {
            printDebug(System.err);
            throw new RuntimeException("Game is still in progress.");
        }

        if (debugEnabled) {
            printDebug(System.out);
        }

        PGNGame pgnGame = PGNGame.createFromGame(game);
        pgnGame.setEvent(String.format("%s vs %s - Match", white.getEngineName(), black.getEngineName()));
        pgnGame.setWhite(white.getEngineName());
        pgnGame.setBlack(black.getEngineName());

        return new GameResult(pgnGame, white, black, winner, matchPoints);
    }

    private void startNewGame() {
        controller1.startNewGame();
        controller2.startNewGame();
    }

    private String retrieveBestMoveFromController(EngineController currentTurn, List<String> moves) {
        if (FENDecoder.INITIAL_FEN.equals(fen)) {
            currentTurn.send_CmdPosition(new CmdPosition(moves));
        } else {
            currentTurn.send_CmdPosition(new CmdPosition(fen, moves));
        }

        RspBestMove bestMove = currentTurn.send_CmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(depth));

        return bestMove.getBestMove();
    }

    private void printDebug(PrintStream printStream) {
        printStream.println(game.toString());

        printStream.println();

        printPGN(printStream);

        printStream.println();

        printMoveExecution();

        printStream.println("--------------------------------------------------------------------------------");
    }

    private void printPGN(PrintStream printStream) {
        PGNEncoder encoder = new PGNEncoder();
        PGNGame pgnGame = PGNGame.createFromGame(game);

        pgnGame.setEvent("Computer chess game");
        pgnGame.setWhite(white.getEngineName());
        pgnGame.setBlack(black.getEngineName());
        pgnGame.setFen(fen);

        printStream.println(encoder.encode(pgnGame));
    }

    private void printMoveExecution() {
        GameDebugEncoder encoder = new GameDebugEncoder();

        System.out.println(encoder.encode(game));
    }

    static public int material(Game game, boolean difference) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            evaluation += difference ? getPieceValue(piece) : Math.abs(getPieceValue(piece));
        }
        return evaluation;
    }

    static public int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 1;
            case PAWN_BLACK -> -1;
            case KNIGHT_WHITE -> 3;
            case KNIGHT_BLACK -> -3;
            case BISHOP_WHITE -> 3;
            case BISHOP_BLACK -> -3;
            case ROOK_WHITE -> 5;
            case ROOK_BLACK -> -5;
            case QUEEN_WHITE -> 9;
            case QUEEN_BLACK -> -9;
            case KING_WHITE -> 10;
            case KING_BLACK -> -10;
        };
    }
}
