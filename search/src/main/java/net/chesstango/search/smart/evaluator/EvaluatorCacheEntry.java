package net.chesstango.search.smart.evaluator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
@AllArgsConstructor
public class EvaluatorCacheEntry {
    long hash;
    int evaluation;
    int age;

    public EvaluatorCacheEntry() {
        hash = 0;
        evaluation = 0;
        age = Integer.MIN_VALUE;
    }
}
