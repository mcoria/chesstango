package net.chesstango.uci.arena;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.GameDebugEncoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SimpleMoveDecoder;
import net.chesstango.board.representations.pgn.PGNGame;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.listeners.MatchListener;
import net.chesstango.uci.arena.matchtypes.MatchType;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Mauricio Coria
 */
public class Match {
    private static final Logger logger = LoggerFactory.getLogger(Match.class);
    private final EngineController controller1;
    private final EngineController controller2;
    private final MatchType matchType;
    private final SimpleMoveDecoder simpleMoveDecoder = new SimpleMoveDecoder();
    private EngineController white;
    private EngineController black;
    private Game game;
    private String mathId;
    private MatchResult matchResult;

    @Setter
    @Accessors(chain = true)
    private String fen;

    @Setter
    @Accessors(chain = true)
    private boolean debugEnabled;

    @Setter
    @Accessors(chain = true)
    private boolean switchChairs;

    @Setter
    @Accessors(chain = true)
    private MatchListener matchListener;


    public Match(EngineController controller1, EngineController controller2, MatchType matchType) {
        this.controller1 = controller1;
        this.controller2 = controller2;
        this.matchType = matchType;
        this.switchChairs = true;
    }

    public List<MatchResult> play(List<String> fenList) {
        List<MatchResult> result = new ArrayList<>();

        fenList.forEach(fen -> result.addAll(play(fen)));

        return result;
    }

    public List<MatchResult> play(String fen) {
        List<MatchResult> result = new ArrayList<>(2);

        try {
            setFen(fen);

            result.add(play(controller1, controller2));

            if (switchChairs) {
                result.add(play(controller2, controller1));
            }

        } catch (RuntimeException e) {
            logger.error("Error playing fen: {}", fen);

            logger.error("PGN: {}", createPGN());

            throw e;
        }

        return result;
    }

    protected MatchResult play(EngineController white, EngineController black) {
        mathId = UUID.randomUUID().toString();

        setChairs(white, black);

        startNewGame();

        compete();

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

            Move move = simpleMoveDecoder.decode(game.getPossibleMoves(), moveStr);

            if (move == null) {
                printGameForDebug(System.err);
                throw new RuntimeException(String.format("No move found %s", moveStr));
            }

            game.executeMove(move);

            executedMovesStr.add(moveStr);

            currentTurn = (currentTurn == white ? black : white);

            if (matchListener != null) {
                matchListener.notifyMove(game, move);
            }
        }

        matchResult = createResult();

        if (matchListener != null) {
            matchListener.notifyEndGame(game, matchResult);
        }
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


    protected MatchResult createResult() {
        EngineController winner = null;

        if (GameStatus.DRAW_BY_FOLD_REPETITION.equals(game.getStatus())) {
            logger.info("[{}] DRAW (por fold repetition)", mathId);

        } else if (GameStatus.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            logger.info("[{}] DRAW (por fold fiftyMoveRule)", mathId);

        } else if (GameStatus.STALEMATE.equals(game.getStatus())) {
            logger.info("[{}] DRAW", mathId);

        } else if (GameStatus.MATE.equals(game.getStatus())) {
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                logger.info("[{}] BLACK WON {}", mathId, black.getEngineName());
                winner = black;

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                logger.info("[{}] WHITE WON {}", mathId, white.getEngineName());
                winner = white;

            }
        } else {
            printGameForDebug(System.err);
            throw new RuntimeException("Game is still in progress.");
        }

        if (debugEnabled) {
            printGameForDebug(System.out);
        }

        return new MatchResult(mathId, createPGN(), white, black, winner);
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

    private void printGameForDebug(PrintStream printStream) {
        printStream.println(createPGN());

        printStream.println();

        printMoveExecution(printStream);

        printStream.println("--------------------------------------------------------------------------------");
    }

    private void printMoveExecution(PrintStream printStream) {
        GameDebugEncoder encoder = new GameDebugEncoder();

        printStream.println(encoder.encode(game));
    }

    private PGNGame createPGN() {
        PGNGame pgnGame = PGNGame.createFromGame(game);
        pgnGame.setEvent(mathId);
        pgnGame.setWhite(white.getEngineName());
        pgnGame.setBlack(black.getEngineName());
        return pgnGame;
    }
}
