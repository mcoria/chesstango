package net.chesstango;

import net.chesstango.board.iterators.bysquare.*;
import net.chesstango.board.iterators.bysquare.bypiece.KingJumpSquareIteratorTest;
import net.chesstango.board.iterators.bysquare.bypiece.KnightJumpSquareIteratorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ TopDownSquareIteratorTest.class, BitSquareIteratorTest.class, TopDownSquareIteratorTest.class,
		BottomUpSquareIteratorTest.class, CardinalSquareIterator01Test.class, CardinalSquareIterator02Test.class,
		KingJumpSquareIteratorTest.class, KnightJumpSquareIteratorTest.class})
public class IteratorsTestSuite {

}
