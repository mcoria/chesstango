package net.chesstango.ai.imp.smart.evaluation.tunning;

import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import io.jenetics.util.Seq;
import org.junit.Test;
import io.jenetics.util.Factory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;


public class MyJeneticsTest {

    private static int eval(Genotype<BitGene> gt) {
        return gt.chromosome()
                .as(BitChromosome.class)
                .bitCount();
    }

    @Test
    public void myTest(){
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<BitGene>> gtf =
                Genotype.of(BitChromosome.of(8, 0.5));

        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(MyJeneticsTest::eval, gtf)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(2000)
                .collect(EvolutionResult.toBestGenotype() );

        System.out.println("Hello World:\n" + result);
    }

    private static int evalNumber01(Genotype<IntegerGene> gt) {
        Chromosome<IntegerGene> chromosome = gt.chromosome();

        IntegerGene chrm0 = chromosome.get(0);

        int value = chrm0.intValue();

        return - (value-4) * (value-8);
    }

    @Test
    public void test_optimizeCuadratic_max(){

        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(-1000, +1000));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine
                .builder(MyJeneticsTest::evalNumber01, gtf)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<IntegerGene> result = engine.stream()
                .limit(50)
                .collect(EvolutionResult.toBestGenotype() );

        System.out.println("Hello World:\n" + result);
    }

    private static int evalNumber02(Genotype<IntegerGene> gt) {
        //System.out.println("Gene count:" + gt.geneCount());

        Chromosome<IntegerGene> chromo1 = gt.get(0);
        //System.out.println("chromo1 length:" + chromo1.length());
        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();
        //System.out.println("gene1:" + gene1Value);


        Chromosome<IntegerGene> chromo2 = gt.get(1);
        //System.out.println("chromo2 length:" + chromo2.length());
        IntegerGene gene2 = chromo2.get(0);
        int gene2Value = gene2.intValue();
        //System.out.println("gene2:" + gene2Value);

        int eval =  Math.abs( (33 - gene1Value) * (88 - gene2Value)  );

        //System.out.println("eval = " + eval);

        return  eval;
    }

    @Test
    public void test_optimizeCuadratic1(){

        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(-1000, +1000),
                            IntegerChromosome.of(-1000, +1000));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine
                .builder(MyJeneticsTest::evalNumber02, gtf)
                .minimizing()
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<IntegerGene> result = engine.stream()
                .limit(20)
                .peek(statistics)
                .collect( EvolutionResult.toBestGenotype() );

        System.out.println("IntegerGene:\n" + result);
        System.out.println(statistics);
    }

    private Integer counter = 0;

    int individuoNro = 1;
    @Test
    public void test_evolucionar(){
        // 1.) Define the genotype (factory) suitable for the problem.
        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(IntRange.of(-1000, 1000), 2));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine.builder(this::expresar_genotipo, gtf)
                .minimizing()
                .executor ( (Executor) Runnable::run )
                .build();

        // 4.) Start the execution (evolution) and collect the result.
        Phenotype<IntegerGene, Integer> result = engine.stream().limit(1000).collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado= " + result.fitness() );
        System.out.println("Y su genotipo= " + result.genotype() );
    }
    private int expresar_genotipo(Genotype<IntegerGene> gt) {
        Chromosome<IntegerGene> chromo1 = gt.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        synchronized(this){
            while(counter > 0){
                try {
                    System.out.println("Other thread is working");
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            counter++;
        }



        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int eval = Math.abs( (33 - gene1Value) * (88 - gene2Value) );
        System.out.println("Individuo nro " + individuoNro++ +  " con gene1 = " + gene1Value + " y gene2 = " + gene2Value + "; fenotipo = " + eval);


        synchronized(this){
            counter--;
            notify();
        }

        return eval;
    }


    public void test_evolucionar_en_batch(){
        // 1.) Define the genotype (factory) suitable for the problem.
        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(IntRange.of(-1000, 1000), 2));

        final Function<Genotype<IntegerGene>,Integer> fitness= null;

        final Evaluator<IntegerGene, Integer> evaluator =  new Evaluator<IntegerGene, Integer>() {
            @Override
            public ISeq<Phenotype<IntegerGene, Integer>> eval(Seq<Phenotype<IntegerGene, Integer>> population) {

                return null;
            }
        };

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = new Engine.Builder<>(evaluator, gtf).minimizing().build();


        // 4.) Start the execution (evolution) and collect the result.
        Phenotype<IntegerGene, Integer> result = engine
                .stream()
                .limit(1000)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado= " + result.fitness() );
        System.out.println("Y su genotipo= " + result.genotype() );
    }

    private int expresar_genotipo_batch(Genotype<IntegerGene> gt) {
        Chromosome<IntegerGene> chromo1 = gt.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        synchronized(this){
            while(counter > 0){
                try {
                    System.out.println("Other thread is working");
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            counter++;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int eval = Math.abs( (33 - gene1Value) * (88 - gene2Value) );
        System.out.println("Individuo nro " + individuoNro++ +  " con gene1 = " + gene1Value + " y gene2 = " + gene2Value + "; fenotipo = " + eval);


        synchronized(this){
            counter--;
            notify();
        }

        return eval;
    }



    @Test
    public void test_optimizeCuadratic01(){

        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(-1000, +1000),
                        IntegerChromosome.of(-1000, +1000));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine
                .builder(MyJeneticsTest::evalNumber02, gtf)
                .minimizing()
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<IntegerGene> result = engine.stream()
                .limit(1000)
                .peek(statistics)
                .collect( EvolutionResult.toBestGenotype() );

        System.out.println("IntegerGene:\n" + result);
        System.out.println(statistics);
    }

    private int contador = 1;

    @Test
    public void test_evolucionar01(){
        // 1.) Define the genotype (factory) suitable for the problem.
        Factory<Genotype<IntegerGene>> gtf =
                Genotype.of(IntegerChromosome.of(IntRange.of(-1000, 1000), 2));

        Constraint<IntegerGene, Integer> myConstraint = new Constraint<IntegerGene, Integer>() {
            @Override
            public boolean test(Phenotype<IntegerGene, Integer> phenotype) {
                Genotype<IntegerGene> genotype = phenotype.genotype();
                Chromosome<IntegerGene> chromo1 = genotype.get(0);

                IntegerGene gene1 = chromo1.get(0);
                int gene1Value = gene1.intValue();

                IntegerGene gene2 = chromo1.get(1);
                int gene2Value = gene2.intValue();

                return (gene1Value +  gene2Value) % 114 == 0 ;
            }

            @Override
            public Phenotype<IntegerGene, Integer> repair(Phenotype<IntegerGene, Integer> phenotype, long generation) {

                IntRange geneRange = IntRange.of(0, 1000);

                Phenotype<IntegerGene, Integer> newPhenotype = Phenotype.of(Genotype.of(IntegerChromosome.of(
                        IntegerGene.of(64, geneRange ),
                        IntegerGene.of(contador++, geneRange )
                )), generation);


                return newPhenotype;
            }
        };

        //myConstraint.

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine.builder(this::expresar_genotipo01, gtf)
                .selector(new EliteSelector<>(2))
                .constraint(myConstraint)
                .populationSize(2)
                .minimizing()
                .executor ( (Executor) Runnable::run )
                .build();

        // 4.) Start the execution (evolution) and collect the result.
        Phenotype<IntegerGene, Integer> result = engine.stream()
                .limit(1000)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("El mejor fenotipo encontrado= " + result.fitness() );
        System.out.println("Y su genotipo= " + result.genotype() );
    }


    private Set<String> keys = new HashSet<>();

    private int expresar_genotipo01(Genotype<IntegerGene> gt) {
        Chromosome<IntegerGene> chromo1 = gt.get(0);

        IntegerGene gene1 = chromo1.get(0);
        int gene1Value = gene1.intValue();

        IntegerGene gene2 = chromo1.get(1);
        int gene2Value = gene2.intValue();

        String keyStr = Integer.toString(gene1Value) + ":" + Integer.toString(gene2Value);

        if( ! keys.add(keyStr)){
            //System.out.println("Se genero un genotipo repetido " + keyStr);
        }

        int eval = Math.abs( (33 - gene1Value) * (88 - gene2Value) ) + Math.abs( 33 - gene1Value) + Math.abs( 88 - gene2Value ) ;

        //System.out.println("Individuo nro " + individuoNro++ +  " con gene1 = " + gene1Value + " y gene2 = " + gene2Value + "; fenotipo = " + eval);


        return eval;
    }

}
