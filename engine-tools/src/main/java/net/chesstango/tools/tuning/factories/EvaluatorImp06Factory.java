package net.chesstango.tools.tuning.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
@Getter
public class EvaluatorImp06Factory implements GameEvaluatorFactory {
    public static final int CONSTRAINT_MAX_VALUE = 1000;

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorImp06Factory.class);

    private final String key;

    private final int[] weighs;

    private final int[] mgPawnTbl;
    private final int[] mgKnightTbl;
    private final int[] mgBishopTbl;
    private final int[] mgRookTbl;
    private final int[] mgQueenTbl;
    private final int[] mgKingTbl;

    private final int[] egPawnTbl;
    private final int[] egKnightTbl;
    private final int[] egBishopTbl;
    private final int[] egRookTbl;
    private final int[] egQueenTbl;
    private final int[] egKingTbl;

    public EvaluatorImp06Factory(int[] weighs,
                                 int[] mgPawnTbl, int[] mgKnightTbl, int[] mgBishopTbl, int[] mgRookTbl, int[] mgQueenTbl, int[] mgKingTbl,
                                 int[] egPawnTbl, int[] egKnightTbl, int[] egBishopTbl, int[] egRookTbl, int[] egQueenTbl, int[] egKingTbl) {

        this.weighs = weighs;

        this.mgPawnTbl = mgPawnTbl;
        this.mgKnightTbl = mgKnightTbl;
        this.mgBishopTbl = mgBishopTbl;
        this.mgRookTbl = mgRookTbl;
        this.mgQueenTbl = mgQueenTbl;
        this.mgKingTbl = mgKingTbl;

        this.egPawnTbl = egPawnTbl;
        this.egKnightTbl = egKnightTbl;
        this.egBishopTbl = egBishopTbl;
        this.egRookTbl = egRookTbl;
        this.egQueenTbl = egQueenTbl;
        this.egKingTbl = egKingTbl;

        int computedKey = Arrays.hashCode(weighs);
        computedKey ^= Arrays.hashCode(mgPawnTbl);
        computedKey ^= Arrays.hashCode(mgKnightTbl);
        computedKey ^= Arrays.hashCode(mgBishopTbl);
        computedKey ^= Arrays.hashCode(mgRookTbl);
        computedKey ^= Arrays.hashCode(mgQueenTbl);
        computedKey ^= Arrays.hashCode(mgKingTbl);
        computedKey ^= Arrays.hashCode(egPawnTbl);
        computedKey ^= Arrays.hashCode(egKnightTbl);
        computedKey ^= Arrays.hashCode(egBishopTbl);
        computedKey ^= Arrays.hashCode(egRookTbl);
        computedKey ^= Arrays.hashCode(egQueenTbl);
        computedKey ^= Arrays.hashCode(egKingTbl);


        //this.key = String.format("%s", Long.toUnsignedString(computedKey, 16));
        this.key = String.format("%08x", computedKey);
    }

    @Override
    public Evaluator createGameEvaluator() {
        return new EvaluatorImp06(weighs,
                mgPawnTbl, mgKnightTbl, mgBishopTbl, mgRookTbl, mgQueenTbl, mgKingTbl,
                egPawnTbl, egKnightTbl, egBishopTbl, egRookTbl, egQueenTbl, egKingTbl);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getRepresentation() {
        EvaluatorImp06.Tables obj = new EvaluatorImp06.Tables(key, weighs,
                mgPawnTbl, mgKnightTbl, mgBishopTbl, mgRookTbl, mgQueenTbl, mgKingTbl,
                egPawnTbl, egKnightTbl, egBishopTbl, egRookTbl, egQueenTbl, egKingTbl);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
