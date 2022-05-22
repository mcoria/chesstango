/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.pseudomovesgenerators.imp.MoveGeneratorEnPassantImpTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorEsteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorNorteEsteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorNorteOesteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorNorteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorOesteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorSurEsteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorSurOesteTest;
import chess.board.pseudomovesgenerators.strategies.AbstractCardinalMoveGeneratorSurTest;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.KingBlackMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.KingWhiteMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.KnightMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.PawnBlackMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.PawnWhiteMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.QueenMoveGeneratorTest;
import chess.board.pseudomovesgenerators.strategies.RookMoveGeneratorTest;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractCardinalMoveGeneratorEsteTest.class, AbstractCardinalMoveGeneratorNorteEsteTest.class,
		AbstractCardinalMoveGeneratorNorteOesteTest.class, AbstractCardinalMoveGeneratorNorteTest.class,
		AbstractCardinalMoveGeneratorOesteTest.class, AbstractCardinalMoveGeneratorSurEsteTest.class,
		AbstractCardinalMoveGeneratorSurOesteTest.class, AbstractCardinalMoveGeneratorSurTest.class,
		BishopMoveGeneratorTest.class, KingBlackMoveGeneratorTest.class, KingWhiteMoveGeneratorTest.class,
		KnightMoveGeneratorTest.class, PawnBlackMoveGeneratorTest.class, PawnWhiteMoveGeneratorTest.class,
		QueenMoveGeneratorTest.class, RookMoveGeneratorTest.class, MoveGeneratorEnPassantImpTest.class })
public class PseudoMoveGeneratorTestSuite {
}
