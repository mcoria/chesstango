package net.chesstango.search.alphabeta.pv.sorters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class PrincipalVariationPredicateTest {

    private PrincipalVariationPredicate principalVariationPredicate;

    @BeforeEach
    public void setUp() {
        principalVariationPredicate = new PrincipalVariationPredicate();
    }

}
