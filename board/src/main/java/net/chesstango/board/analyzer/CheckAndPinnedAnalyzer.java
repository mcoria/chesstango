package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByKnight;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByPawn;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByPiece;
import net.chesstango.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Esta clase analiza si el jugador actual se encuentra en Jaque.
 * De no estar en jaque se identifican todas las posiciones pinned y el Cardinal de la amenaza.
 *
 * @author Mauricio Coria
 *
 */
public class CheckAndPinnedAnalyzer {
	private final ChessPositionReader positionReader;
	private final CheckAndPinnedAnalyzerByColor analyzerWhite;
	private final CheckAndPinnedAnalyzerByColor analyzerBlack;
	protected long pinnedPositions;
	protected List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals;
	protected boolean kingInCheck;
	
	public CheckAndPinnedAnalyzer(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
		this.analyzerWhite = new CheckAndPinnedAnalyzerByColor(Color.WHITE);
		this.analyzerBlack = new CheckAndPinnedAnalyzerByColor(Color.BLACK);
	}	

	public void analyze(AnalyzerResult result) {
		pinnedPositions = 0;
		pinnedPositionCardinals = new ArrayList<>(8);
		kingInCheck = false;
		
		Color currentTurn = positionReader.getCurrentTurn();
		
		if(Color.WHITE.equals(currentTurn)){
			analyzerWhite.analyze();
		} else {
			analyzerBlack.analyze();
		}

		result.setKingInCheck(kingInCheck);

		result.setPinnedSquares(pinnedPositions);

		result.setPinnedPositionCardinals(pinnedPositionCardinals);
	}


	private class CheckAndPinnedAnalyzerByColor {
		private final Color color;
		private final CheckAndPinnedAnalyzerCardinal rookCapturer;
		private final CheckAndPinnedAnalyzerCardinal bishopCapturer;
		private final CapturerByPiece knightCapturer;
		private final CapturerByPiece pawnCapturer;

		
		public CheckAndPinnedAnalyzerByColor(Color color) {
			this.color = color;
			this.rookCapturer =  new CheckAndPinnedAnalyzerRook(positionReader, color.oppositeColor());
			this.bishopCapturer = new CheckAndPinnedAnalyzerBishop(positionReader, color.oppositeColor());
			this.knightCapturer = new CapturerByKnight(positionReader, color.oppositeColor());
			this.pawnCapturer = new CapturerByPawn(positionReader, color.oppositeColor());
		}

		public void analyze() {
			Square squareKing = positionReader.getKingSquare(color);

			if( knightCapturer.positionCaptured(squareKing) ||
				pawnCapturer.positionCaptured(squareKing) ||
				rookCapturer.positionCaptured(squareKing, pinnedPositionCardinals) ||
				bishopCapturer.positionCaptured(squareKing, pinnedPositionCardinals)) {
				CheckAndPinnedAnalyzer.this.kingInCheck = true;
			}

			// Another king can not check our king NEVER
			// analyzeByKing(squareKing)

			pinnedPositions |= rookCapturer.pinnedPositions;
			pinnedPositions |= bishopCapturer.pinnedPositions;
		}
	}
}