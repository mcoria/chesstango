package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.Constraint;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import net.chesstango.ai.imp.smart.IterativeDeeping;
import net.chesstango.ai.imp.smart.MinMaxPruning;
import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.arbiter.EngineController;
import net.chesstango.uci.arbiter.Match;
import net.chesstango.uci.arbiter.imp.EngineControllerImp;
import net.chesstango.uci.engine.imp.EngineProxy;
import net.chesstango.uci.engine.imp.EngineTango;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class ChessEvolutionTest {

    private ObjectPool<EngineController> pool;

    @Before
    public void settup() {
        pool = new GenericObjectPool<EngineController>(new EngineControllerProxyFactory());
    }

    @After
    public void tearDown() {
        pool.close();
    }


    private int CONSTRAINT_MAX_VALUE = 100;

    protected Constraint<IntegerGene, Long> myConstraint = new Constraint<IntegerGene, Long>() {
        @Override
        public boolean test(Phenotype<IntegerGene, Long> phenotype) {
            Genotype<IntegerGene> genotype = phenotype.genotype();
            Chromosome<IntegerGene> chromo1 = genotype.get(0);

            IntegerGene gene1 = chromo1.get(0);
            int gene1Value = gene1.intValue();

            IntegerGene gene2 = chromo1.get(1);
            int gene2Value = gene2.intValue();

            IntegerGene gene3 = chromo1.get(2);
            int gene3Value = gene3.intValue();

            return (gene1Value +  gene2Value + gene3Value) % CONSTRAINT_MAX_VALUE == 0 ;
        }

        @Override
        public Phenotype<IntegerGene, Long> repair(Phenotype<IntegerGene, Long> phenotype, long generation) {

            Genotype<IntegerGene> genotype = phenotype.genotype();

            Chromosome<IntegerGene> chromo1 = genotype.get(0);

            IntegerGene gene1 = chromo1.get(0);
            int gene1Value = gene1.intValue() % CONSTRAINT_MAX_VALUE;

            IntegerGene gene2 = chromo1.get(1);
            int gene2Value = gene2.intValue() % CONSTRAINT_MAX_VALUE;

            while(gene1Value + gene2Value > CONSTRAINT_MAX_VALUE) {
                gene2Value = (gene1Value + gene2Value) % CONSTRAINT_MAX_VALUE;
            }

            int gene3Value = CONSTRAINT_MAX_VALUE - gene2Value - gene1Value;


            IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);
            Phenotype<IntegerGene, Long> newPhenotype = Phenotype.of(Genotype.of(IntegerChromosome.of(
                    IntegerGene.of(gene1Value, geneRange ),
                    IntegerGene.of(gene2Value, geneRange ),
                    IntegerGene.of(gene3Value, geneRange )
            )), generation);


            return newPhenotype;
        }
    };

    @Test
    public void evolveChessFunction() {
        // 1.) Define the genotype (factory) suitable for the problem.

        IntRange geneRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);

        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(geneRange, 3));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Long> engine = Engine.builder(this::fitness, gtf)
                .selector(new EliteSelector<>(4))
                .constraint(myConstraint)
                .populationSize(10)
                //.executor((Executor) Runnable::run)
                .build();

        // 4.) Start the execution (evolution) and collect the result.
        Phenotype<IntegerGene, Long> result = engine
                .stream()
                .limit(100)
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


    private Map<String, Long> gameMemory = new HashMap<>();

    private long fitness(Genotype<IntegerGene> genotype){
        Chromosome<IntegerGene> chromo1 = genotype.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        IntegerGene gene3 = chromo1.get(2);
        int gene3Value = gene3.intValue();

        long points = 0;

        String keyGenes = gene1Value + "|" + gene2Value + "|" + gene3Value;
        Long previousGamePoints = gameMemory.get(keyGenes);

        if (previousGamePoints == null) {

            EngineController engineZonda = createTango(gene1Value, gene2Value, gene3Value);

            List<Match.MathResult> matchResult = fitnessEval(engineZonda);

            tearDownEngine(engineZonda);

            points += matchResult.stream().filter(result -> result.getEngineWhite() == engineZonda).mapToLong(Match.MathResult::getPoints).sum();

            points -= matchResult.stream().filter(result -> result.getEngineBlack() == engineZonda).mapToLong(Match.MathResult::getPoints).sum();

            gameMemory.put(keyGenes, points);

            System.out.println("Evaluacion con gene1=[" + gene1Value + "] gene2=[" + gene2Value + "] gene3=[" + gene3Value + "] ; puntos = [" + points + "]");

        } else {
            points = previousGamePoints;
        }

        return points;
    }

    private List<Match.MathResult> fitnessEval(EngineController engineZonda) {
        List<Match.MathResult> matchResult = null;

        try {
            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineZonda, 1);

            matchResult = match.play(Arrays.asList(FENDecoder.INITIAL_FEN, "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7", "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));

            pool.returnObject(engineProxy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return matchResult;
    }

    private EngineController createTango(int gene1, int gene2, int gene3) {
        EngineController zonda = new EngineControllerImp(new EngineTango(new IterativeDeeping(new MinMaxPruning(new GameEvaluatorImp(gene1, gene2, gene3)))).disableAsync());
        zonda.send_CmdUci();
        zonda.send_CmdIsReady();
        return zonda;
    }

    public void tearDownEngine(EngineController engine) {
        engine.send_CmdQuit();
    }

    private class EngineControllerProxyFactory extends BasePooledObjectFactory<EngineController> {

        @Override
        public EngineController create() throws Exception {
            EngineProxy coreEngineProxy = new EngineProxy();
            //coreEngineProxy.setLogging(true);
            EngineController engineProxy = new EngineControllerImp(coreEngineProxy);
            engineProxy.send_CmdUci();
            engineProxy.send_CmdIsReady();

            return engineProxy;
        }

        @Override
        public PooledObject<EngineController> wrap(EngineController engineController) {
            return new DefaultPooledObject<EngineController>(engineController);
        }

        @Override
        public void destroyObject(PooledObject<EngineController> pooledController) throws Exception {
            pooledController.getObject().send_CmdQuit();
        }
    }
}

        /*
        // Pheno = 6
        Phenotype<IntegerGene, Integer> phenotype0 = Phenotype.of(Genotype.of(IntegerChromosome.of(
                IntegerGene.of(1000, geneRange ),
                IntegerGene.of(42, geneRange ),
                IntegerGene.of(7, geneRange )
        )), 1);

        // Pheno = 6
        Phenotype<IntegerGene, Integer> phenotype1 = Phenotype.of(Genotype.of(IntegerChromosome.of(
                IntegerGene.of(55, geneRange ),
                IntegerGene.of(1, geneRange),
                IntegerGene.of(4, geneRange)
        )), 1);

        // Pheno = 5
        Phenotype<IntegerGene, Integer> phenotype2 = Phenotype.of(Genotype.of(IntegerChromosome.of(
                IntegerGene.of(99, geneRange ),
                IntegerGene.of(1, geneRange ),
                IntegerGene.of(76, geneRange )
        )), 1);

        // Pheno = 5
        Phenotype<IntegerGene, Integer> phenotype3 = Phenotype.of(Genotype.of(IntegerChromosome.of(
                IntegerGene.of(86, geneRange ),
                IntegerGene.of(1, geneRange),
                IntegerGene.of(39, geneRange )
        )), 1);

        // Pheno = 5
        Phenotype<IntegerGene, Integer> phenotype4 = Phenotype.of(Genotype.of(IntegerChromosome.of(
                IntegerGene.of(76, geneRange ),
                IntegerGene.of(1, geneRange ),
                IntegerGene.of(36, geneRange )
        )), 1);

        // Pheno = 5
        Phenotype<IntegerGene, Integer> phenotype5 = Phenotype.of(Genotype.of(IntegerChromosome.of(
                IntegerGene.of(64, geneRange ),
                IntegerGene.of(1, geneRange ),
                IntegerGene.of(30, geneRange )
        )), 1);



        EvolutionStart<IntegerGene, Integer> initial =
                EvolutionStart.of( ISeq.of(phenotype0, phenotype1, phenotype2, phenotype3, phenotype4, phenotype5), 1);

         */