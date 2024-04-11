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
public class AnalyzerResult {

	@Setter
	@Getter
	private boolean isKingInCheck;

	@Setter
	@Getter
	private long pinnedSquares;

	@Setter
	@Getter
	private long capturedPositions;

	@Setter
	@Getter
	private long safeKingPositions;

	@Setter
	@Getter
	private List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals;

}
