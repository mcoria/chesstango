package net.chesstango.evaluation.tuning.fitnessfunctions;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import net.chesstango.board.representations.Transcoding;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.tuning.TuningMain;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.arena.EngineControllerPoolFactory;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.Match;
import net.chesstango.uci.arena.gui.ProxyConfigLoader;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.go.CmdGoByDepth;
import net.chesstango.uci.proxy.UciProxy;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.List;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class FitnessByMatch implements FitnessFunction {

    private static final CmdGo CMD_GO = new CmdGoByDepth().setDepth(2);

    private final List<String> fenList;

    private final Function<Genotype<IntegerGene>, GameEvaluator> gameEvaluatorSupplierFn;

    private ObjectPool<EngineController> pool;

    public FitnessByMatch(Function<Genotype<IntegerGene>, GameEvaluator> gameEvaluatorSupplierFn) {
        this.gameEvaluatorSupplierFn = gameEvaluatorSupplierFn;
        this.fenList = getFenList() ;
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
    public long fitness(Genotype<IntegerGene> genotype) {
        EngineController engineTango = createTango(genotype);

        List<MatchResult> matchResult = fitnessEval(engineTango);

        quitTango(engineTango);

        long pointsAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engineTango).mapToLong(MatchResult::getPoints).sum();

        long pointsAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engineTango).mapToLong(MatchResult::getPoints).sum();

        return pointsAsWhite + (-1) * pointsAsBlack;
    }

    public EngineController createTango(Genotype<IntegerGene> genotype) {
        DefaultSearchMove search = new DefaultSearchMove(gameEvaluatorSupplierFn.apply(genotype));

        EngineController tango = new EngineControllerImp(new UciTango( new Tango( search )));

        tango.startEngine();

        return tango;
    }


    private List<MatchResult> fitnessEval(EngineController engineTango) {
        List<MatchResult> matchResult = null;
        try {

            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineTango, CMD_GO);

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
}
