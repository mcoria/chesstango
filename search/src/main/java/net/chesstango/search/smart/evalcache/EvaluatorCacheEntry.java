package net.chesstango.search.smart.evalcache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
@AllArgsConstructor
@Accessors(chain = true)
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
