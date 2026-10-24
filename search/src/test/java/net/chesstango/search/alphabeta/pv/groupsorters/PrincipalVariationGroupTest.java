package net.chesstango.search.alphabeta.pv.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.PVMove;
import net.chesstango.search.sorters.GroupOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

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
