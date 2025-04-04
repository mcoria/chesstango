package net.chesstango.board;

import net.chesstango.board.iterators.bysquare.*;
import net.chesstango.board.iterators.bysquare.bypiece.KingSquareIteratorTest;
import net.chesstango.board.iterators.bysquare.bypiece.KnightSquareIteratorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ TopDownSquareIteratorTest.class, BitSquareIteratorTest.class, TopDownSquareIteratorTest.class,
		BottomUpSquareIteratorTest.class, CardinalSquareIterator01Test.class, CardinalSquareIterator02Test.class,
		KingSquareIteratorTest.class, KnightSquareIteratorTest.class})
public class IteratorsTestSuite {

}
