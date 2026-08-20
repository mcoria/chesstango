package net.chesstango.search.sorters;

import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.smart.root.RootMoveEvaluationComparator;
import net.chesstango.search.sorters.RootMoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class RootMoveSorterTest {

    @Mock
    private List<RootMoveEvaluation> rootMoveEvaluationList;

    @Mock
    private RootMoveEvaluationComparator rootMoveEvaluationComparatorArgConstructor;

    @Mock
    private Comparator<RootMoveEvaluation> rootMoveEvaluationComparatorReversed;

    @Mock
    private Stream<RootMoveEvaluation> stream;


    private RootMoveSorter rootMoveSorter;

    @BeforeEach
    public void setUp() {

    }

    /**
     * RootMoveEvaluationComparator debe ser reverso para ordenar de mayor a menor
     */
    @Test
    public void testRootMoveEvaluationComparatorIsReversed() {
        when(rootMoveEvaluationComparatorArgConstructor.reversed()).thenReturn(rootMoveEvaluationComparatorReversed);

        rootMoveSorter = new RootMoveSorter(rootMoveEvaluationComparatorArgConstructor);

        verify(rootMoveEvaluationComparatorArgConstructor).reversed();

        assertEquals(rootMoveEvaluationComparatorReversed, rootMoveSorter.getRootMoveEvaluationComparator());
    }

    @Test
    public void testSortIsCalled() {
        when(rootMoveEvaluationComparatorArgConstructor.reversed()).thenReturn(rootMoveEvaluationComparatorReversed);
        when(rootMoveEvaluationList.stream()).thenReturn(stream);

        rootMoveSorter = new RootMoveSorter(rootMoveEvaluationComparatorArgConstructor);

        rootMoveSorter.setRootMoveEvaluationList(rootMoveEvaluationList);

        rootMoveSorter.getOrderedMoves(0);

        verify(stream).sorted(rootMoveEvaluationComparatorReversed);
    }
}
