package chess.board.analyzer;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.positions.bypiece.KnightBitIterator;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;

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
	
	private final CheckAndPinnedAnalyzerByColor analyzerWhite = new CheckAndPinnedAnalyzerByColor(Color.WHITE, PawnWhite_ARRAY_SALTOS);
	private final CheckAndPinnedAnalyzerByColor analyzerBlack = new CheckAndPinnedAnalyzerByColor(Color.BLACK, PawnBlack_ARRAY_SALTOS);
	
	private long pinnedPositions;

	private List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals;
	
	private boolean kingInCheck;
	
	public CheckAndPinnedAnalyzer(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}	

	public void analyze() {
		pinnedPositions = 0;
		pinnedPositionCardinals = new ArrayList<>(8);
		kingInCheck = false;
		
		Color turnoActual = positionReader.getCurrentTurn();
		
		if(Color.WHITE.equals(turnoActual)){
			analyzerBlack.analyze();
		} else {
			analyzerWhite.analyze();
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
		private final Piece knight;
		private final long[] pawnJumps;
		private final Piece pawn;

		
		public CheckAndPinnedAnalyzerByColor(Color color, long[] pawnJumps) {
			this.color = color;
			this.opponentColor = color.oppositeColor();
			this.rook =  Piece.getRook(color);
			this.bishop = Piece.getBishop(color);
			this.queen = Piece.getQueen(color);
			this.knight = Piece.getKnight(color);
			this.pawn = Piece.getPawn(color);
			this.pawnJumps = pawnJumps;
				
		}

		public void analyze() {
			Square squareKingOpponent = positionReader.getKingSquare(color.oppositeColor());

			analyzeByKnight(squareKingOpponent);
			
			if (!CheckAndPinnedAnalyzer.this.kingInCheck){
				analyzeByBishop(squareKingOpponent);
			}
			
			if (!CheckAndPinnedAnalyzer.this.kingInCheck){
				analyzeByRook(squareKingOpponent);
			}

			if (!CheckAndPinnedAnalyzer.this.kingInCheck){
				analyzeByPawn(squareKingOpponent);
			}
			
			// Another king can not check our king NEVER
			// analyzeByKing(squareKing);

		}
		
		private void analyzeByKnight(Square squareKingOpponent) {
			Iterator<PiecePositioned> iterator = new KnightBitIterator<PiecePositioned>(positionReader, squareKingOpponent);
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(knight.equals(destino.getValue())){		    	
			    	CheckAndPinnedAnalyzer.this.kingInCheck = true;
			    	return;
			    }
			}
		}
		
		private void analyzeByPawn(Square squareKingOpponent) {
			Iterator<PiecePositioned> iterator = positionReader.iterator( pawnJumps[squareKingOpponent.toIdx()] );
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(pawn.equals(destino.getValue())){
			    	CheckAndPinnedAnalyzer.this.kingInCheck = true;
			    	return;
			    }
			}
		}

		private final Cardinal[]  direccionesBishop = BishopMoveGenerator.BISHOP_CARDINAL;
		private void analyzeByBishop(Square square) {
			positionCapturedByDireccion(square, direccionesBishop, this.bishop);
		}

		private final Cardinal[]  direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
		private void analyzeByRook(Square squareKingOpponent) {		
			positionCapturedByDireccion(squareKingOpponent, direccionesRook, this.rook);
		}

		private void positionCapturedByDireccion(Square squareKingOpponent, Cardinal[] direcciones, Piece rookOrBishop) {
			for (Cardinal cardinal : direcciones) {
				if(positionCapturedByCardinalPieza(squareKingOpponent, cardinal, rookOrBishop)){
					CheckAndPinnedAnalyzer.this.kingInCheck = true;
					return;
				}
			}
		}		
		
		private boolean positionCapturedByCardinalPieza(Square squareKingOpponent, Cardinal cardinal, Piece rookOrBishop) {
			PiecePositioned possiblePinned = null;

			Iterator<PiecePositioned> iterator = positionReader.iterator(new CardinalSquareIterator(squareKingOpponent, cardinal));
			
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				Piece piece = destino.getValue();

				if (piece != null) {
					if (possiblePinned == null){
                        // La pieza es nuestra y de las que ponen en jaque al oponente
                        // La pieza es nuestra pero no pone en jaque al oponente
                        if(opponentColor.equals(piece.getColor())){
							// La pieza es del oponente, es posiblemente pinned
							possiblePinned = destino;
						} else {
							return this.queen.equals(piece) || rookOrBishop.equals(piece);
						}
					} else {
						
						// La pieza es nuestra y de las que ponen en jaque al oponente, tenemos pinned
						if (this.queen.equals(piece) || rookOrBishop.equals(piece)) {
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
	
	private static final long[] PawnWhite_ARRAY_SALTOS = {
			0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 2L, 5L, 10L, 20L, 40L, 80L, 160L, 64L, 512L, 1280L, 2560L, 5120L, 10240L,
			20480L, 40960L, 16384L, 131072L, 327680L, 655360L, 1310720L, 2621440L, 5242880L, 10485760L, 4194304L,
			33554432L, 83886080L, 167772160L, 335544320L, 671088640L, 1342177280L, 2684354560L, 1073741824L,
			8589934592L, 21474836480L, 42949672960L, 85899345920L, 171798691840L, 343597383680L, 687194767360L,
			274877906944L, 2199023255552L, 5497558138880L, 10995116277760L, 21990232555520L, 43980465111040L,
			87960930222080L, 175921860444160L, 70368744177664L, 562949953421312L, 1407374883553280L, 2814749767106560L,
			5629499534213120L, 11258999068426240L, 22517998136852480L, 45035996273704960L, 18014398509481984L };
	
	private static final long[] PawnBlack_ARRAY_SALTOS = {
			512L, 1280L, 2560L, 5120L, 10240L, 20480L, 40960L, 16384L, 131072L, 327680L, 655360L, 1310720L, 2621440L,
			5242880L, 10485760L, 4194304L, 33554432L, 83886080L, 167772160L, 335544320L, 671088640L, 1342177280L,
			2684354560L, 1073741824L, 8589934592L, 21474836480L, 42949672960L, 85899345920L, 171798691840L,
			343597383680L, 687194767360L, 274877906944L, 2199023255552L, 5497558138880L, 10995116277760L,
			21990232555520L, 43980465111040L, 87960930222080L, 175921860444160L, 70368744177664L, 562949953421312L,
			1407374883553280L, 2814749767106560L, 5629499534213120L, 11258999068426240L, 22517998136852480L,
			45035996273704960L, 18014398509481984L, 144115188075855872L, 360287970189639680L, 720575940379279360L,
			1441151880758558720L, 2882303761517117440L, 5764607523034234880L, -6917529027641081856L,
			4611686018427387904L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L };	
}