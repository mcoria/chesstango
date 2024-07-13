package net.chesstango.uci.arena;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.GameDebugEncoder;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SimpleMoveDecoder;
import net.chesstango.board.representations.pgn.PGN;
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
class Match {
    private static final Logger logger = LoggerFactory.getLogger(Match.class);
    private final EngineController white;
    private final EngineController black;
    private final MatchType matchType;
    private final String mathId;
    private final SimpleMoveDecoder simpleMoveDecoder = new SimpleMoveDecoder();

    private Game game;
    private MatchResult matchResult;

    @Setter
    @Accessors(chain = true)
    private FEN fen;

    @Setter
    @Accessors(chain = true)
    private boolean debugEnabled;

    @Setter
    @Accessors(chain = true)
    private MatchListener matchListener;


    public Match(EngineController white, EngineController black, MatchType matchType) {
        this.white = white;
        this.black = black;
        this.matchType = matchType;
        this.mathId = UUID.randomUUID().toString();
    }

    public MatchResult play(FEN fen) {
        try {
            setFen(fen);

            startNewGame();

            compete();

            return matchResult;

        } catch (RuntimeException e) {
            logger.error("Error playing fen: {}", fen);

            logger.error("PGN: {}", createPGN());

            throw e;
        }
    }


    protected void compete() {
        setGame(FENDecoder.loadGame(fen));

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
        white.startNewGame();
        black.startNewGame();
    }

    private String retrieveBestMoveFromController(EngineController currentTurn, List<String> moves) {
        if (FEN.of(FENDecoder.INITIAL_FEN).equals(fen)) {
            currentTurn.send_CmdPosition(new CmdPosition(moves));
        } else {
            currentTurn.send_CmdPosition(new CmdPosition(fen.toString(), moves));
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

    private PGN createPGN() {
        PGN pgn = PGN.of(game);
        pgn.setEvent(mathId);
        pgn.setWhite(white.getEngineName());
        pgn.setBlack(black.getEngineName());
        return pgn;
    }
}
