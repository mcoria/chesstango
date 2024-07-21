package net.chesstango.tools.tuning.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

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

        this.key = String.format("%s-eval", UUID.randomUUID());

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
    public void dump() {
        EvaluatorImp06.Tables obj = new EvaluatorImp06.Tables(weighs,
                mgPawnTbl, mgKnightTbl, mgBishopTbl, mgRookTbl, mgQueenTbl, mgKingTbl,
                egPawnTbl, egKnightTbl, egBishopTbl, egRookTbl, egQueenTbl, egKingTbl);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(obj);
            logger.info("Tables {} - {}", key, jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
