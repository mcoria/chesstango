package net.chesstango.tools.tuning.geneticproviders;

import io.jenetics.*;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.tools.tuning.factories.EvaluatorImp06Factory;
import net.chesstango.tools.tuning.factories.GameEvaluatorFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GPEvaluatorImp06 implements GeneticProvider {
    private static final int CONSTRAINT_MAX_VALUE = 1000;

    private static final IntRange weighRange = IntRange.of(0, CONSTRAINT_MAX_VALUE);
    private static final IntRange tableRange = IntRange.of(-CONSTRAINT_MAX_VALUE, CONSTRAINT_MAX_VALUE);

    @Override
    public Factory<Genotype<IntegerGene>> getGenotypeFactory() {
        return Genotype.of(
                IntegerChromosome.of(weighRange, 3),  // weighs
                IntegerChromosome.of(tableRange, 64), // mgPawnTbl
                IntegerChromosome.of(tableRange, 64), // mgKnightTbl
                IntegerChromosome.of(tableRange, 64), // mgBishopTbl
                IntegerChromosome.of(tableRange, 64), // mgRookTbl
                IntegerChromosome.of(tableRange, 64), // mgQueenTbl
                IntegerChromosome.of(tableRange, 64), // mgKingTbl
                IntegerChromosome.of(tableRange, 64), // egPawnTbl
                IntegerChromosome.of(tableRange, 64), // egKnightTbl
                IntegerChromosome.of(tableRange, 64), // egBishopTbl
                IntegerChromosome.of(tableRange, 64), // egRookTbl
                IntegerChromosome.of(tableRange, 64), // egQueenTbl
                IntegerChromosome.of(tableRange, 64)  // egKingTbl
        );
    }

    @Override
    public EvolutionStart<IntegerGene, Long> getEvolutionStart(int populationSize) {

        List<Phenotype<IntegerGene, Long>> phenoList = List.of(
                createPhenotype("{\"id\":\"1c173b34\",\"weighs\":[902,45,43],\"mgPawnTbl\":[0,0,0,0,0,0,0,0,-35,-1,-20,-23,-15,24,38,-22,-26,-4,-4,-10,3,3,33,-12,-27,-2,-5,12,17,6,10,-25,-14,13,6,21,23,12,17,-23,-6,7,26,31,65,56,25,-20,98,134,61,95,68,126,34,-11,0,0,0,0,0,0,0,0],\"mgKnightTbl\":[-105,-21,-58,-33,-17,-28,-19,-23,-29,-53,-12,-3,-1,18,-14,-19,-23,-9,12,10,19,17,25,-16,-13,4,16,13,28,19,21,-8,-9,17,19,53,37,69,18,22,-47,60,37,65,84,129,73,44,-73,-41,72,36,23,62,7,-17,-167,-89,-34,-49,61,-97,-15,-107],\"mgBishopTbl\":[-33,-3,-14,-21,-13,-12,-39,-21,4,15,16,0,7,21,33,1,0,15,15,15,14,27,18,10,-6,13,13,26,34,12,10,4,-4,5,19,50,37,37,7,-2,-16,37,43,40,35,50,37,-2,-26,16,-18,-13,30,59,18,-47,-29,4,-82,-37,-25,-42,7,-8],\"mgRookTbl\":[-19,-13,1,17,16,7,-37,-26,-44,-16,-20,-9,-1,11,-6,-71,-45,-25,-16,-17,3,0,-5,-33,-36,-26,-12,-1,9,-7,6,-23,-24,-11,7,26,24,35,-8,-20,-5,19,26,36,17,45,61,16,27,32,58,62,80,67,26,44,32,42,32,51,63,9,31,43],\"mgQueenTbl\":[-1,-18,-9,10,-15,-25,-31,-50,-35,-8,11,2,8,15,-3,1,-14,2,-11,-2,-5,2,14,5,-9,-26,-9,-10,-2,-4,3,-3,-27,-27,-16,-16,-1,17,-2,1,-13,-17,7,8,29,56,47,57,-24,-39,-5,1,-16,57,28,54,-28,0,29,12,59,44,43,45],\"mgKingTbl\":[-15,36,12,-54,8,-28,24,14,1,7,-8,-64,-43,-16,9,8,-14,-14,-22,-46,-44,-30,-15,-27,-49,-1,-27,-39,-46,-44,-33,-51,-17,-20,-12,-27,-30,-25,-14,-36,-9,24,2,-16,-20,6,22,-22,29,-1,-20,-7,-8,-4,-38,-29,-65,23,16,-15,-56,-34,2,13],\"egPawnTbl\":[0,0,0,0,0,0,0,0,13,8,8,10,13,0,2,-7,4,7,-6,1,0,-5,-1,-8,13,9,-3,-7,-7,-8,3,-1,32,24,13,5,-2,4,17,17,94,100,85,67,56,53,82,84,178,173,158,134,147,132,165,187,0,0,0,0,0,0,0,0],\"egKnightTbl\":[-29,-51,-23,-15,-22,-18,-50,-64,-42,-20,-10,-5,-2,-20,-23,-44,-23,-3,-1,15,10,-3,-20,-22,-18,-6,16,25,16,17,4,-18,-17,3,22,22,22,11,8,-18,-24,-20,10,9,-1,-9,-19,-41,-25,-8,-25,-2,-9,-25,-24,-52,-58,-38,-13,-28,-31,-27,-63,-99],\"egBishopTbl\":[-23,-9,-23,-5,-9,-16,-5,-17,-14,-18,-7,-1,4,-9,-15,-27,-12,-3,8,10,13,3,-7,-15,-6,3,13,19,7,10,-3,-9,-3,9,12,9,14,10,3,2,2,-8,0,-1,-2,6,0,4,-8,-4,7,-12,-3,-13,-4,-14,-14,-21,-11,-8,-7,-9,-17,-24],\"egRookTbl\":[-9,2,3,-1,-5,-13,4,-20,-6,-6,0,2,-9,-9,-11,-3,-4,0,-5,-1,-7,-12,-8,-16,3,5,8,4,-5,-6,-8,-11,4,3,13,1,2,1,-1,2,7,7,7,5,4,-3,-5,-3,11,13,13,11,-3,3,8,3,13,10,18,15,12,12,8,5],\"egQueenTbl\":[-33,-28,-22,-43,-5,-32,-20,-41,-22,-23,-30,-16,-16,-23,-36,-32,-16,-27,15,6,9,17,10,5,-18,28,19,47,31,34,39,23,3,22,24,45,57,40,57,36,-20,6,9,49,47,35,19,9,-17,20,32,41,58,25,30,0,-9,22,22,27,27,19,10,20],\"egKingTbl\":[-53,-34,-21,-11,-28,-14,-24,-43,-27,-11,4,13,14,4,-5,-17,-19,-3,11,21,23,16,7,-9,-18,-4,21,24,27,23,9,-11,-8,22,24,27,26,33,26,3,10,17,23,15,20,45,44,13,-12,17,14,17,17,38,23,11,-74,-35,-18,-18,-11,15,4,-17]}"),
                createPhenotype("{\"id\":\"81c517b4\",\"weighs\":[647,760,222],\"mgPawnTbl\":[734,-146,-265,-853,816,257,-209,809,203,761,895,-327,675,936,508,335,-45,-272,304,306,-739,-527,-727,775,431,-162,-557,-179,138,-263,-929,12,727,-468,-1000,-377,-904,494,-361,-948,-593,-858,947,964,-199,809,-418,595,-337,238,-841,-214,-613,-679,176,-637,13,2,-921,110,91,928,-317,157],\"mgKnightTbl\":[-838,404,266,566,413,853,905,152,829,913,578,-379,-216,83,-739,534,173,-834,591,-358,-24,-475,-545,-903,-430,516,409,266,351,-798,-967,-445,526,-150,283,789,-859,232,-931,53,-916,-485,-977,-420,-322,736,-976,422,-102,319,-773,222,768,-979,-73,162,246,-154,-110,214,-695,957,-646,965],\"mgBishopTbl\":[-170,-533,426,861,-673,720,-520,575,119,539,-339,900,683,525,798,748,750,500,-894,-461,-42,-13,950,606,960,75,-261,-334,-377,-452,-780,983,418,-98,-571,370,-96,-179,612,-121,-927,555,139,-742,218,-145,-866,-240,924,-87,-343,-320,3,423,600,57,-217,267,300,977,42,633,418,-543],\"mgRookTbl\":[-420,-728,142,920,969,227,-554,164,476,157,-721,-611,-652,726,187,538,-814,351,255,-529,283,-521,573,-704,-605,860,288,-347,-114,28,767,440,-995,-752,-836,757,747,955,789,34,323,-53,807,-304,789,500,-870,636,231,54,-606,992,491,204,956,-863,-948,625,-8,-31,-3,995,132,-657],\"mgQueenTbl\":[-643,-468,717,-765,-894,-20,245,151,993,-218,-343,148,-355,938,334,709,136,-353,-780,868,898,550,22,344,-854,-324,-803,537,876,-335,-549,-230,367,484,-224,918,-784,514,124,226,475,-611,-225,-793,509,102,877,-116,-573,-571,-712,281,402,896,-537,-8,583,-253,-534,278,-284,-87,458,-76],\"mgKingTbl\":[-124,-613,-843,546,436,-816,-777,925,742,114,448,502,398,-625,-264,-504,-883,-128,233,901,-661,-441,-100,442,906,876,798,836,575,807,-305,763,-574,66,-971,-365,-415,546,-938,351,-108,-950,665,-708,-830,-454,-419,-127,-826,-288,-526,-821,-47,452,225,152,714,174,-856,333,-607,-598,-195,-423],\"egPawnTbl\":[-598,938,-312,-269,-53,272,-669,-634,-114,8,-644,115,-574,597,159,372,902,-824,398,-526,160,-965,245,-596,-884,487,-997,-216,8,-393,188,-485,609,-477,-432,-433,-82,396,-26,-421,972,-562,-920,-509,-611,-301,990,70,-528,-75,115,-95,-852,-966,611,132,403,-369,-389,-65,931,269,787,355],\"egKnightTbl\":[-324,59,967,980,997,-337,859,406,-335,75,-117,174,-542,-385,-555,-693,335,-494,-803,-186,798,-140,-306,-765,241,912,-992,993,-7,88,264,-925,-421,627,-685,-766,-999,675,-938,156,322,402,860,-160,-744,629,968,49,-376,505,764,-204,-872,501,63,-693,74,911,186,458,-578,-146,727,394],\"egBishopTbl\":[-914,419,-526,-662,-495,383,-168,-165,256,-765,967,437,-703,-528,-107,-845,-382,51,988,680,477,-307,-167,812,-437,857,-9,-71,751,371,290,539,788,-159,632,-957,-853,-712,-373,353,57,67,-506,275,-956,-94,8,309,-737,533,881,418,-405,960,-966,598,-171,826,899,125,-774,297,35,166],\"egRookTbl\":[-2,997,-741,30,-177,-169,-697,-761,589,302,-467,810,729,-238,-890,-719,769,648,892,676,-71,-969,-467,-466,506,606,-543,856,-948,-467,-413,329,551,-470,160,-15,328,-510,363,-812,925,189,-520,817,-189,-264,4,918,125,-432,-672,-872,959,817,-727,-636,-968,-275,-554,-22,-880,-932,115,970],\"egQueenTbl\":[823,529,-460,-755,79,-862,-943,-569,-220,-185,-578,-77,-803,-945,20,394,257,-157,419,-943,-416,-292,306,-628,-142,511,495,930,874,-604,459,-169,376,932,417,607,842,-547,-132,-903,-818,-709,65,367,503,648,-619,774,-393,-805,-156,-322,626,900,690,-885,-453,298,263,170,622,-85,564,-393],\"egKingTbl\":[739,91,823,-804,-359,61,895,985,849,-104,-69,-556,-323,329,-244,824,-94,436,-581,758,772,-517,977,-254,-851,810,-988,166,265,32,354,642,968,-900,-70,748,654,-442,963,-227,-99,-234,-516,235,45,-360,56,-768,-962,-986,-272,34,965,-407,-84,754,696,-553,-198,-506,458,-475,-37,588]}")
        );

        ISeq<Phenotype<IntegerGene, Long>> population = ISeq.of(phenoList);

        return EvolutionStart.of(population, 1);
    }


    @Override
    public GameEvaluatorFactory createGameEvaluatorFactors(Genotype<IntegerGene> genotype) {
        int[] weighs = chromosomeToArray(genotype.get(0));

        int[] mgPawnTbl = chromosomeToArray(genotype.get(1));
        int[] mgKnightTbl = chromosomeToArray(genotype.get(2));
        int[] mgBishopTbl = chromosomeToArray(genotype.get(3));
        int[] mgRookTbl = chromosomeToArray(genotype.get(4));
        int[] mgQueenTbl = chromosomeToArray(genotype.get(5));
        int[] mgKingTbl = chromosomeToArray(genotype.get(6));

        int[] egPawnTbl = chromosomeToArray(genotype.get(7));
        int[] egKnightTbl = chromosomeToArray(genotype.get(8));
        int[] egBishopTbl = chromosomeToArray(genotype.get(9));
        int[] egRookTbl = chromosomeToArray(genotype.get(10));
        int[] egQueenTbl = chromosomeToArray(genotype.get(11));
        int[] egKingTbl = chromosomeToArray(genotype.get(12));

        return new EvaluatorImp06Factory(weighs,
                mgPawnTbl, mgKnightTbl, mgBishopTbl, mgRookTbl, mgQueenTbl, mgKingTbl,
                egPawnTbl, egKnightTbl, egBishopTbl, egRookTbl, egQueenTbl, egKingTbl);
    }

    private int[] chromosomeToArray(Chromosome<IntegerGene> chromosome) {
        IntegerChromosome intChromosome = chromosome.as(IntegerChromosome.class);
        return intChromosome.toArray();
    }

    /**
     * Este es el proceso inverso para cargar una poblacion inicial dado los factores
     *
     * @return
     */
    private Phenotype<IntegerGene, Long> createPhenotype(String dump) {
        EvaluatorImp06.Tables tables = EvaluatorImp06.readValues(dump);

        List<IntegerGene> weighs = Arrays.stream(tables.weighs()).mapToObj(val -> IntegerGene.of(val, weighRange)).toList();
        List<IntegerGene> mgPawnTbl = Arrays.stream(tables.mgPawnTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgKnightTbl = Arrays.stream(tables.mgKnightTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgBishopTbl = Arrays.stream(tables.mgBishopTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgRookTbl = Arrays.stream(tables.mgRookTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgQueenTbl = Arrays.stream(tables.mgQueenTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> mgKingTbl = Arrays.stream(tables.mgKingTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egPawnTbl = Arrays.stream(tables.egPawnTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egKnightTbl = Arrays.stream(tables.egKnightTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egBishopTbl = Arrays.stream(tables.egBishopTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egRookTbl = Arrays.stream(tables.egRookTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egQueenTbl = Arrays.stream(tables.egQueenTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();
        List<IntegerGene> egKingTbl = Arrays.stream(tables.egKingTbl()).mapToObj(val -> IntegerGene.of(val, tableRange)).toList();

        return Phenotype.of(
                Genotype.of(
                        IntegerChromosome.of(weighs),
                        IntegerChromosome.of(mgPawnTbl),
                        IntegerChromosome.of(mgKnightTbl),
                        IntegerChromosome.of(mgBishopTbl),
                        IntegerChromosome.of(mgRookTbl),
                        IntegerChromosome.of(mgQueenTbl),
                        IntegerChromosome.of(mgKingTbl),
                        IntegerChromosome.of(egPawnTbl),
                        IntegerChromosome.of(egKnightTbl),
                        IntegerChromosome.of(egBishopTbl),
                        IntegerChromosome.of(egRookTbl),
                        IntegerChromosome.of(egQueenTbl),
                        IntegerChromosome.of(egKingTbl)
                ), 1);
    }


}