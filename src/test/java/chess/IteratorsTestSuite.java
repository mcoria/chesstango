/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.iterators.pieceplacement.BoardteratorTest;
import chess.board.iterators.square.BitSquareIteratorTest;
import chess.board.iterators.square.BottomUpSquareIteratorTest;
import chess.board.iterators.square.CardinalSquareIterator01Test;
import chess.board.iterators.square.CardinalSquareIterator02Test;
import chess.board.iterators.square.JumpSquareIterator01Test;
import chess.board.iterators.square.JumpSquareIterator02Test;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BoardteratorTest.class, BitSquareIteratorTest.class, BottomUpSquareIteratorTest.class,
		CardinalSquareIterator01Test.class, CardinalSquareIterator02Test.class,
		JumpSquareIterator01Test.class, JumpSquareIterator02Test.class})
public class IteratorsTestSuite {

}
