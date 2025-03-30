package net.chesstango.board.analyzer;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;

import java.util.AbstractMap;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
@Setter
@Getter
public class AnalyzerResult {

	private boolean isKingInCheck;

	private long pinnedSquares;

	private long capturedPositions;

	private long safeKingPositions;

	private List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals;

}
