package chess.arena;

import chess.ai.imp.smart.IterativeDeeping;
import chess.ai.imp.smart.MinMaxPruning;
import chess.ai.imp.smart.evaluation.GameEvaluator;
import chess.board.representations.fen.FENDecoder;
import chess.uci.arbiter.EngineController;
import chess.uci.arbiter.Match;
import chess.uci.arbiter.imp.EngineControllerImp;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class ChessEvolutionTest {

    private EngineController engineProxy;

    @Before
    public void settup() {
        EngineProxy coreEngineProxy = new EngineProxy();
        //coreEngineProxy.setLogging(true);
        engineProxy = new EngineControllerImp(coreEngineProxy);
        engineProxy.send_CmdUci();
        engineProxy.send_CmdIsReady();
    }

    @After
    public void tearDown() {
        engineProxy.send_CmdQuit();
    }


    //TOOD: Deberiamos codificar los siguientes genes para una poblacion inicial
/*
    Para esta lista de juegos:
    (FENDecoder.INITIAL_FEN, "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7", "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));
    Evaluacion con gene1=[833] gene2=[352] gene3=[91] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[145] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[169] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[46] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[170] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[118] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[69] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[172] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[356] gene3=[91] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[129] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[355] gene3=[91] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[50] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[83] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[164] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[56] ; puntos = [9]
    Evaluacion con gene1=[838] gene2=[352] gene3=[66] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[102] ; puntos = [9]
    Evaluacion con gene1=[833] gene2=[352] gene3=[82] ; puntos = [9]
    Evaluacion con gene1=[825] gene2=[352] gene3=[91] ; puntos = [9]
    Evaluacion con gene1=[837] gene2=[352] gene3=[52] ; puntos = [9]
*/


    @Test
    public void evolveEvaluationFunction() {
        // 1.) Define the genotype (factory) suitable for the problem.

        IntRange geneRange = IntRange.of(0, 1000);

        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(geneRange, 3));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine.builder(this::expresar_genotipo01, gtf)
                .selector(new EliteSelector<>(3))
                .populationSize(15)
                .executor((Executor) Runnable::run)
                .build();

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

        // 4.) Start the execution (evolution) and collect the result.
        Phenotype<IntegerGene, Integer> result = engine
                .stream(initial)
                .limit(1000)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado= " + result.fitness());
        System.out.println("Y su genotipo= " + result.genotype());
    }


    private Map<String, Integer> gameMemory = new HashMap<>();

    private int expresar_genotipo01(Genotype<IntegerGene> gt) {
        Chromosome<IntegerGene> chromo1 = gt.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        IntegerGene gene3 = chromo1.get(2);
        int gene3Value = gene3.intValue();

        int points = 0;

        String keyGenes = gene1Value + "|" + gene2Value + "|" + gene3Value;
        Integer previousGamePoints = gameMemory.get(keyGenes);

        if (previousGamePoints == null) {

            EngineController engineZonda = createZonda(gene1Value, gene2Value, gene3Value);

            Match match = new Match(engineProxy, engineZonda, 1);

            List<Match.MathResult> matchResult = match.play(Arrays.asList(FENDecoder.INITIAL_FEN, "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20", "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7", "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));

            tearDownEngine(engineZonda);

            points += matchResult.stream().filter(result -> result.getEngineWhite() == engineZonda).mapToInt(result -> result.getWhitePoints()).sum();

            points += matchResult.stream().filter(result -> result.getEngineBlack() == engineZonda).mapToInt(result -> result.getBlackPoints()).sum();

            gameMemory.put(keyGenes, points);

            System.out.println("Evaluacion con gene1=[" + gene1Value + "] gene2=[" + gene2Value + "] gene3=[" + gene3Value + "] ; puntos = [" + points + "]");
        } else {
            points = previousGamePoints;
        }

        return points;
    }

    private EngineController createZonda(int gene1, int gene2, int gene3) {
        EngineController zonda = new EngineControllerImp(new EngineZonda(new IterativeDeeping(new MinMaxPruning(new GameEvaluator(gene1, gene2, gene3)))).disableAsync());
        zonda.send_CmdUci();
        zonda.send_CmdIsReady();
        return zonda;
    }

    public void tearDownEngine(EngineController engine) {
        engine.send_CmdQuit();
    }
}
