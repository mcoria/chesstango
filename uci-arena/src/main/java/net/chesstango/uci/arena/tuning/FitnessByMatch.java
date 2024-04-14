package net.chesstango.uci.arena.tuning;

import net.chesstango.board.*;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.Transcoding;
import net.chesstango.board.representations.pgn.PGNGame;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.tools.TuningMain;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessFunction;
import net.chesstango.uci.arena.Match;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.gui.ProxyConfigLoader;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.matchtypes.MatchType;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.proxy.UciProxy;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByMatch implements FitnessFunction {
    public static final int WINNER_POINTS = 1000;
    private static final MatchType matchType = new MatchByDepth(1);

    private final List<String> fenList;

    private ObjectPool<EngineController> pool;

    public FitnessByMatch() {
        this.fenList = getFenList();
    }

    @Override
    public void start() {
        pool = new GenericObjectPool<>(new EngineControllerPoolFactory(() -> new EngineControllerImp(new UciProxy(ProxyConfigLoader.loadEngineConfig("Spike")))));
    }

    @Override
    public void stop() {
        pool.close();
    }


    @Override
    public long fitness(Supplier<GameEvaluator> gameEvaluatorSupplier) {
        EngineController engineTango = createTango(gameEvaluatorSupplier.get());

        List<MatchResult> matchResult = fitnessEval(engineTango);

        quitTango(engineTango);

        long pointsAsWhite = matchResult.stream()
                .filter(result -> result.getEngineWhite() == engineTango)
                .map(MatchResult::getPgnGame)
                .mapToInt(FitnessByMatch::getPoints).sum();

        long pointsAsBlack = matchResult.stream()
                .filter(result -> result.getEngineBlack() == engineTango)
                .map(MatchResult::getPgnGame)
                .mapToInt(FitnessByMatch::getPoints)
                .sum();

        return pointsAsWhite + (-1) * pointsAsBlack;
    }

    public EngineController createTango(GameEvaluator gameEvaluator) {
        DefaultSearchMove search = new DefaultSearchMove(gameEvaluator);

        EngineController tango = new EngineControllerImp(new UciTango(new Tango(search)));

        tango.startEngine();

        return tango;
    }


    private List<MatchResult> fitnessEval(EngineController engineTango) {
        List<MatchResult> matchResult = null;
        try {

            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineTango, matchType);

            matchResult = match.play(fenList);

            pool.returnObject(engineProxy);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return matchResult;
    }

    private void quitTango(EngineController tango) {
        tango.send_CmdQuit();
    }


    private static List<String> getFenList() {
        return new Transcoding().pgnFileToFenPositions(TuningMain.class.getClassLoader().getResourceAsStream("Balsa_Top25.pgn"));
    }


    protected static int getPoints(PGNGame pgnGame) {
        Game game = pgnGame.buildGame();

        int matchPoints = 0;

        if (GameStatus.DRAW_BY_FOLD_REPETITION.equals(game.getStatus())) {
            matchPoints = material(game, true);

        } else if (GameStatus.DRAW_BY_FIFTY_RULE.equals(game.getStatus())) {
            matchPoints = material(game, true);

        } else if (GameStatus.STALEMATE.equals(game.getStatus())) {
            matchPoints = material(game, true);

        } else if (GameStatus.MATE.equals(game.getStatus())) {
            if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
                matchPoints = -1 * (WINNER_POINTS + material(game, false));

            } else if (Color.BLACK.equals(game.getChessPosition().getCurrentTurn())) {
                matchPoints = (WINNER_POINTS + material(game, false));

            }
        }

        return matchPoints;
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
