package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.*;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.MoveCacheBoard;


/**
 * Esta clase analiza si el jugador actual se encuentra en Jaque.
 * De no estar en jaque se identifican todas las posiciones pinned y el Cardinal de la amenaza.
 *
 * @author Mauricio Coria
 *
 */
public class CheckAnalyzer implements Analyzer {
	private final ChessPositionReader positionReader;
	private final CheckAnalyzerByColor analyzerWhite;
	private final CheckAnalyzerByColor analyzerBlack;
	private final MoveCacheBoard moveCacheBoard;
	
	public CheckAnalyzer(ChessPositionReader positionReader, MoveCacheBoard moveCacheBoard) {
		this.positionReader = positionReader;
		this.moveCacheBoard = moveCacheBoard;
		this.analyzerWhite = new CheckAnalyzerByColor(Color.WHITE);
		this.analyzerBlack = new CheckAnalyzerByColor(Color.BLACK);
	}

	@Override
	public void analyze(AnalyzerResult result) {
		Color currentTurn = positionReader.getCurrentTurn();
		
		if(Color.WHITE.equals(currentTurn)){
			analyzerWhite.analyze(result);
		} else {
			analyzerBlack.analyze(result);
		}
	}


	private class CheckAnalyzerByColor implements Analyzer{
		private final Color color;
		private final CapturerByPiece rookCapturer;
		private final CapturerByPiece bishopCapturer;
		private final CapturerByPiece knightCapturer;
		private final CapturerByPiece pawnCapturer;

		
		public CheckAnalyzerByColor(Color color) {
			this.color = color;
			this.rookCapturer =  new CapturerByRook(positionReader, positionReader, color.oppositeColor());
			this.bishopCapturer = new CapturerByBishop(positionReader, positionReader, color.oppositeColor());
			this.knightCapturer = new CapturerByKnight(positionReader, color.oppositeColor());
			this.pawnCapturer = new CapturerByPawn(positionReader, color.oppositeColor());
		}

		@Override
		public void analyze(AnalyzerResult result) {
			// King square under attack
			Square squareKing = positionReader.getKingSquare(color);

			long allPositions = positionReader.getAllPositions();

			long positionsInCache = moveCacheBoard.getPseudoMovesPositions();

			long positionsNotInCache = allPositions & ~positionsInCache;

			long possibleAttackers = positionsNotInCache & positionReader.getPositions(color.oppositeColor()) ;


			// Las posiciones que no estan en cache y son del color contrario pueden capturar al REY
			// Si el cache esta vacio entonces considerar que cualquier pieza puede capturar al REY

			if( knightCapturer.positionCaptured(squareKing, possibleAttackers) ||
				pawnCapturer.positionCaptured(squareKing, possibleAttackers) ||
				rookCapturer.positionCaptured(squareKing, possibleAttackers) ||
				bishopCapturer.positionCaptured(squareKing, possibleAttackers)) {
				result.setKingInCheck(true);
			} else {
				result.setKingInCheck(false);
			}
		}
	}
}