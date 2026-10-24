package net.chesstango.search.alphabeta.pv.groupsorters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class PrincipalVariationGroupTest {

    private PrincipalVariationGroup principalVariationGroup;

    @BeforeEach
    public void setUp() {
        principalVariationGroup = new PrincipalVariationGroup();
    }

}
