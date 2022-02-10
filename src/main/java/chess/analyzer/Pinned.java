/**
 * 
 */
package chess.analyzer;

import chess.ChessPositionReader;
import chess.Color;
import chess.Piece;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.square.CardinalSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class Pinned {	
	private final static Cardinal[] cardinalesBishop = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
	private final static Cardinal[] cardinalesRook = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

	private final  PinnedImp white = new PinnedImp(Color.WHITE);
	private final  PinnedImp black = new PinnedImp(Color.BLACK);
	
	private final ChessPositionReader positionReader;
	

	public Pinned(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}

	public long getPinnedSquare(Color color) {
		return Color.WHITE.equals(color) ? white.getPinnedSquare(positionReader.getKingSquare(color)) : black.getPinnedSquare(positionReader.getKingSquare(color));
	}


	private class PinnedImp {
		
		private final Color color;
		
		private final Piece reina;
		private final Piece torre;
		private final Piece alfil;	
		
		public PinnedImp(Color color) {
			this.color = color;
			this.reina = Piece.getQueen(color.opositeColor());
			this.torre = Piece.getRook(color.opositeColor());
			this.alfil = Piece.getBishop(color.opositeColor());
		}

		public long getPinnedSquare(Square kingSquare) {
			long pinnedCollection = 0;
			
			pinnedCollection |= getPinnedCardinales(kingSquare, alfil, reina, cardinalesBishop);
			pinnedCollection |= getPinnedCardinales(kingSquare, torre, reina, cardinalesRook);

			return pinnedCollection;
		}
		
		protected long getPinnedCardinales(Square kingSquare, Piece torreOBishop, Piece reina,
				Cardinal[] direcciones) {
			long pinnedCollection = 0;
			for (Cardinal cardinal : direcciones) {
				Square pinned = getPinned(kingSquare, torreOBishop, reina, cardinal);
				if (pinned != null) {
					pinnedCollection |= pinned.getPosicion();
				}
			}
			return pinnedCollection;
		}
		
		
		protected Square getPinned(Square kingSquare, Piece torreOBishop, Piece reina, Cardinal cardinal) {
			Square pinned = null;
			CardinalSquareIterator iterator = new CardinalSquareIterator(kingSquare, cardinal);
			while (iterator.hasNext()) {
				Square destino = iterator.next();
				Color colorDestino = positionReader.getColor(destino);
				if (colorDestino == null) {
					continue;
				}
				if (pinned == null) {
					if (color.equals(colorDestino)) {
						pinned = destino;
					} else { // if (color.opositeColor().equals(colorDestino))
						return null;
					}
				} else {
					if (color.equals(colorDestino)) {
						return null;
					} else { //// if (color.opositeColor().equals(colorDestino))
						Piece piece = positionReader.getPieza(destino);
						if (torreOBishop.equals(piece) || reina.equals(piece)) {
							return pinned;
						} else {
							return null;
						}
					}
				}

			}
			return null;
		}		
		
	}
}
