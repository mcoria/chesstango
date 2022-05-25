package chess.board.analyzer;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.byposition.bypiece.KnightBitIterator;
import chess.board.iterators.byposition.bypiece.PawnBlackBitIterator;
import chess.board.iterators.byposition.bypiece.PawnWhiteBitIterator;
import chess.board.iterators.bysquare.CardinalSquareIterator;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByKnight;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByPawn;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.SquareCapturerByPiece;
import chess.board.position.ChessPositionReader;
import chess.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import chess.board.movesgenerators.pseudo.strategies.RookMoveGenerator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


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
	
	private long pinnedPositions;
	private List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals;
	private boolean kingInCheck;
	
	public CheckAndPinnedAnalyzer(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
		this.analyzerWhite = new CheckAndPinnedAnalyzerByColor(Color.WHITE);
		this.analyzerBlack = new CheckAndPinnedAnalyzerByColor(Color.BLACK);
	}	

	public void analyze() {
		pinnedPositions = 0;
		pinnedPositionCardinals = new ArrayList<>(8);
		kingInCheck = false;
		
		Color currentTurn = positionReader.getCurrentTurn();
		
		if(Color.WHITE.equals(currentTurn)){
			analyzerWhite.analyze();
		} else {
			analyzerBlack.analyze();
		}
	}

	public long getPinnedPositions() {
		return pinnedPositions;
	}


	public boolean isKingInCheck() {
		return kingInCheck;
	}

	public List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> getPinnedPositionCardinals() {
		return pinnedPositionCardinals;
	}


	private class CheckAndPinnedAnalyzerByColor {
		private final Color color;
		private final Color opponentColor;
		private final Piece rook;
		private final Piece bishop;
		private final Piece queen;
		private final SquareCapturerByPiece knightCapturer;
		private final SquareCapturerByPiece pawnCapturer;

		
		public CheckAndPinnedAnalyzerByColor(Color color) {
			this.color = color;
			this.opponentColor = color.oppositeColor();
			this.rook =  Piece.getRook(opponentColor);
			this.bishop = Piece.getBishop(opponentColor);
			this.queen = Piece.getQueen(opponentColor);
			this.knightCapturer = new CapturerByKnight(positionReader, opponentColor);
			this.pawnCapturer = new CapturerByPawn(positionReader, opponentColor);
		}

		public void analyze() {
			Square squareKing = positionReader.getKingSquare(color);

			if( knightCapturer.positionCaptured(squareKing) ||
				analyzeByBishop(squareKing) ||
				analyzeByRook(squareKing) ||
				pawnCapturer.positionCaptured(squareKing) ) {
					CheckAndPinnedAnalyzer.this.kingInCheck = true;
			}
			
			// Another king can not check our king NEVER
			// analyzeByKing(squareKing)

		}

		private boolean analyzeByBishop(Square square) {
			return positionCapturedByDirections(square, BishopMoveGenerator.BISHOP_CARDINAL, bishop);
		}

		private boolean analyzeByRook(Square squareKingOpponent) {
			return positionCapturedByDirections(squareKingOpponent, RookMoveGenerator.ROOK_CARDINAL, rook);
		}

		private boolean positionCapturedByDirections(Square squareKingOpponent, Cardinal[] direcciones, Piece rookOrBishop) {
			for (Cardinal cardinal : direcciones) {
				if(positionCapturedByDirection(squareKingOpponent, cardinal, rookOrBishop)){
					return true;
				}
			}
			return false;
		}
		
		private boolean positionCapturedByDirection(Square squareKingOpponent, Cardinal cardinal, Piece rookOrBishop) {
			PiecePositioned possiblePinned = null;

			Iterator<PiecePositioned> iterator = positionReader.iterator(new CardinalSquareIterator(squareKingOpponent, cardinal));
			
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				Piece piece = destino.getValue();

				if (piece != null) {
					if (possiblePinned == null){
                        // La pieza es nuestra y de las que ponen en jaque al oponente
                        // La pieza es nuestra pero no pone en jaque al oponente
                        if(color.equals(piece.getColor())){
							// La pieza es del oponente, es posiblemente pinned
							possiblePinned = destino;
						} else {
							return this.queen.equals(piece) || rookOrBishop.equals(piece);
						}
					} else {
						
						// La pieza es nuestra y de las que ponen en jaque al oponente, tenemos pinned
						if (queen.equals(piece) || rookOrBishop.equals(piece)) {
							// Confirmado, tenemos pinned
							pinnedPositions |= possiblePinned.getKey().getPosicion();
							pinnedPositionCardinals.add(new AbstractMap.SimpleImmutableEntry<>(possiblePinned, cardinal));
						}
						// O ....
						
						// La pieza es del oponente, tiene 2 piezas seguidas en la misma direccion, no tenemos pinned
						
						// La pieza es nuestra pero no pone en jaque al oponente, no tenemos pinned						
						
						// Cortamos el loop siempre al encontrar la proxima pieza en la misma direccion .....
						return false;
					}
				}
			}
			
			return false;
		}

	}
}