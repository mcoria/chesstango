package net.chesstango.uci.arena;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.GameDebugEncoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.board.representations.pgn.PGNGame;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.mathtypes.MatchType;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Mauricio Coria
 */
public class Match {
    private static final Logger logger = LoggerFactory.getLogger(Match.class);
    public static final int WINNER_POINTS = 1000;
    private final EngineController controller1;
    private final EngineController controller2;
    private final MatchType matchType;
    private EngineController white;
    private EngineController black;
    private String fen;
    private Game game;
    private boolean debugEnabled;
    private boolean switchChairs;
    private MatchListener matchListener;
    private String mathId;


    public Match(EngineController controller1, EngineController controller2, MatchType matchType) {
        this.controller1 = controller1;
        this.controller2 = controller2;
        this.matchType = matchType;
        this.switchChairs = true;
    }

    public Match setMatchListener(MatchListener matchListener) {
        this.matchListener = matchListener;
        return this;
    }

    public List<MatchResult> play(List<String> fenList) {
        List<MatchResult> result = new ArrayList<>();

        fenList.forEach(fen -> {
            result.addAll(play(fen));
        });

        return result;
    }

    public List<MatchResult> play(String fen) {
        List<MatchResult> result = new ArrayList<>();

        try {
            setFen(fen);

            result.add(play(controller1, controller2));

            if (switchChairs) {
                result.add(play(controller2, controller1));
            }

        } catch (RuntimeException e) {
            logger.error("Error playing fen: {}", fen);

            logger.error("PGN: {}", generatePGN());

            throw e;
        }

        return result;
    }

    protected MatchResult play(EngineController white, EngineController black) {
        mathId = UUID.randomUUID().toString();

        setChairs(white, black);

        startNewGame();

        compete();

        MatchResult matchResult = createResult();

        if (matchListener != null) {
            matchListener.notifyEndGame(game, matchResult);
        }

        return matchResult;
    }


    protected void compete() {
        this.game = FENDecoder.loadGame(fen);

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
    protected MatchResult createResult() {
        int matchPoints = 0;
        EngineController winner = null;

        if (GameStatus.DRAW_BY_FOLD_REPETITION.equals(game.getStatus())) {
            logger.info("[{}] DRAW (por fold repetition)", mathId);
            matchPoints = material(game, true);

        } else if (GameStatus.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            logger.info("[{}] DRAW (por fold fiftyMoveRule)", mathId);
            matchPoints = material(game, true);

        } else if (GameStatus.DRAW.equals(game.getStatus())) {
            logger.info("[{}] DRAW", mathId);
            matchPoints = material(game, true);

        } else if (GameStatus.MATE.equals(game.getStatus())) {
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                logger.info("[{}] BLACK WON {}", mathId, black.getEngineName());
                matchPoints = -1 * (WINNER_POINTS + material(game, false));
                winner = black;

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                logger.info("[{}] WHITE WON {}", mathId, white.getEngineName());
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

        return new MatchResult(mathId, pgnGame, white, black, winner, matchPoints);
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

        RspBestMove bestMove = matchType.retrieveBestMoveFromController(currentTurn, currentTurn == white);

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
        printStream.println(generatePGN());
    }

    private String generatePGN() {
        PGNEncoder encoder = new PGNEncoder();
        PGNGame pgnGame = PGNGame.createFromGame(game);

        pgnGame.setEvent(String.format("%s", mathId));
        pgnGame.setWhite(white.getEngineName());
        pgnGame.setBlack(black.getEngineName());
        pgnGame.setFen(fen);

        return encoder.encode(pgnGame);
    }

    private void printMoveExecution() {
        GameDebugEncoder encoder = new GameDebugEncoder();

        System.out.println(encoder.encode(game));
    }

    protected static int material(Game game, boolean difference) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            evaluation += difference ? getPieceValue(piece) : Math.abs(getPieceValue(piece));
        }
        return evaluation;
    }

    protected static int getPieceValue(Piece piece) {
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
