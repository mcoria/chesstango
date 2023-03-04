package net.chesstango.evaluation.tunning;

import io.jenetics.EliteSelector;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import net.chesstango.uci.arena.EngineControllerFactory;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.arena.MatchMain;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.arena.Match;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.proxy.EngineProxy;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class EvaluationMain{
    private static final int MATCH_DEPTH = 1;
    private static final int POPULATION_SIZE = 15;
    private static final int GENERATION_LIMIT = 100;
    private static ExecutorService executor;
    private static ObjectPool<EngineController> pool;
    private final GeneticProvider geneticProvider;
    private final List<String> fenList;
    private final Map<String, Long> gameMemory;

    public EvaluationMain(List<String> fenList, GeneticProvider geneticProvider) {
        this.fenList = fenList;
        this.geneticProvider = geneticProvider;
        this.gameMemory = new HashMap<>();
    }

    public static void main(String[] args) {
        executor = Executors.newFixedThreadPool(4);
        pool = new GenericObjectPool<>(new EngineControllerFactory(EngineProxy::new));
        EvaluationMain main = new EvaluationMain(MatchMain.GAMES_BALSA_TOP10, new GeneticProviderImp02());
        main.findGenotype();
        pool.close();
        executor.shutdown();
    }

    private void findGenotype() {
        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, geneticProvider.getGenotypeFactory())
                .selector(new EliteSelector<>(5))
                .constraint(geneticProvider.getPhenotypeConstraint())
                .populationSize(POPULATION_SIZE)
                .executor(executor)
                .build();

        EvolutionStart<IntegerGene, Long>  start = geneticProvider.getEvolutionStart(POPULATION_SIZE);

        Phenotype<IntegerGene, Long> result = engine
                .stream(start)
                //.stream()
                .limit(GENERATION_LIMIT)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado = " + result.fitness());
        System.out.println("Y su genotipo = " + result.genotype());

        Set<Map.Entry<String, Long>> entrySet = gameMemory.entrySet();
        List<Map.Entry<String, Long>> entryList = entrySet.stream().collect(Collectors.toList());
        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));
        entryList.stream().forEach( entry -> {
            System.out.println("key = [" + entry.getKey() + "]; value=[" + entry.getValue() + "]");
        });

    }

    public long fitness(Genotype<IntegerGene> genotype){
        long points = 0;

        String keyGenes =  geneticProvider.getKeyGenesString(genotype);

        Long previousGamePoints = gameMemory.get(keyGenes);

        if (previousGamePoints == null) {

            EngineController engineTango = createTango(genotype);

            List<GameResult> matchResult = fitnessEval(engineTango);

            quitTango(engineTango);

            points += matchResult.stream().filter(result -> result.getEngineWhite() == engineTango).mapToLong(GameResult::getPoints).sum();

            points -= matchResult.stream().filter(result -> result.getEngineBlack() == engineTango).mapToLong(GameResult::getPoints).sum();

            gameMemory.put(keyGenes, points);

            geneticProvider.printGeneAndPoints(genotype, points);

        } else {
            points = previousGamePoints;
        }

        return points;
    }

    public EngineController createTango(Genotype<IntegerGene> genotype) {
        SearchMove search = new DefaultSearchMove();

        search.setGameEvaluator(geneticProvider.createGameEvaluator(genotype));

        EngineController tango = new EngineControllerImp(new EngineTango( search ));

        tango.send_CmdUci();
        tango.send_CmdIsReady();

        return tango;
    }


    private List<GameResult> fitnessEval(EngineController engineTango) {
        List<GameResult> matchResult = null;
        try {

            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineTango, MATCH_DEPTH);

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


}
