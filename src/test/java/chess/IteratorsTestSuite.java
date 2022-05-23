/**
 * 
 */
package chess;

import chess.board.iterators.square.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TopDownSquareIteratorTest.class, BitSquareIteratorTest.class, TopDownSquareIteratorTest.class,
		BottomUpSquareIteratorTest.class, CardinalSquareIterator01Test.class, CardinalSquareIterator02Test.class,
		KingJumpSquareIteratorTest.class, KnightJumpSquareIteratorTest.class})
public class IteratorsTestSuite {

}
