package chess.board.legalmovesgenerators.squarecapturers;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.byposition.bypiece.KingBitIterator;
import chess.board.iterators.byposition.bypiece.KnightBitIterator;
import chess.board.iterators.bysquares.CardinalSquareIterator;
import chess.board.position.PiecePlacementReader;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;

import java.util.Iterator;


/**
 * Este SquareCapturer busca todas las posibilidades de captura que existen para un Square dado.
 *
 * @author Mauricio Coria
 *
 */
// TODO: el capturer para analyzer es distinto, deberia 
//       	- buscar todas las posibilidades de captura de Rey
//       	- durante la busqueda deberia identificar posiciones pinned
//       - deberiamos tener un capturer de posicion mas sencillo para LegalMoveGenerator
//			- Si no se encuentra en Jaque NO es necesario preguntar por jaque de knight; rey o peon !!!
//				deberia buscar el jaque en direccion del pinned
//			- cuando mueve el rey deberia preguntar por todas las posibilidades de captura
//		 - deberiamos tener un capturer especifico para Castling
public class FullScanSquareCapturer {
	
	private final PiecePlacementReader piecePlacementReader;
	private final CapturerImp capturerWhite = new CapturerImp(Color.WHITE, PawnWhite_ARRAY_SALTOS);
	private final CapturerImp capturerBlack = new CapturerImp(Color.BLACK, PawnBlack_ARRAY_SALTOS);
	
	public FullScanSquareCapturer(PiecePlacementReader piecePlacementReader) {
		this.piecePlacementReader = piecePlacementReader;
	}	

	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class CapturerImp {
		private final Piece rook;
		private final Piece bishop;
		private final Piece queen;
		private final Piece knight;
		private final long[] pawnJumps;
		private final Piece pawn;
		private final Piece king;	

		
		public CapturerImp(Color color, long[] pawnJumps) {
			this.rook =  Piece.getRook(color);
			this.bishop = Piece.getBishop(color);
			this.queen = Piece.getQueen(color);
			this.knight = Piece.getKnight(color);
			this.pawn = Piece.getPawn(color);
			this.king = Piece.getKing(color);
			this.pawnJumps = pawnJumps;
				
		}

		public boolean positionCaptured(Square square) {
            return positionCapturedByKnight(square) ||
                    positionCapturedByRook(square) ||
                    positionCapturedByBishop(square) ||
                    positionCapturedByPawn(square) ||
                    positionCapturedByKing(square);
        }

		private final Cardinal[]  direccionesBishop = BishopMoveGenerator.BISHOP_CARDINAL;
		private boolean positionCapturedByBishop(Square square) {
			return positionCapturedByDireccion(square, direccionesBishop,  bishop);
		}

		private final Cardinal[]  direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
		private boolean positionCapturedByRook(Square square) {		
			return positionCapturedByDireccion(square, direccionesRook, rook);
		}

		private boolean positionCapturedByDireccion(Square square, Cardinal[] direcciones, Piece rookObishop) {		
			for (Cardinal cardinal : direcciones) {
				if(positionCapturedByCardinalPieza(rookObishop, queen, square, cardinal)){
					return true;
				}
			}
			return false;
		}
		
		private boolean positionCapturedByKnight(Square square) {
			Iterator<PiecePositioned> iterator = new KnightBitIterator<PiecePositioned>(piecePlacementReader, square);
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(knight.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}		
		
		private boolean positionCapturedByCardinalPieza(Piece rookObishop, Piece queen, Square square, Cardinal cardinal) {
			Iterator<PiecePositioned> iterator = piecePlacementReader.iterator(new CardinalSquareIterator(square, cardinal));
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				Piece piece = destino.getValue();
				if (piece == null) {
					continue;
				} else if (queen.equals(piece)) {
					return true;
				} else if (rookObishop.equals(piece)) {			
					return true;
				} else {
					break;
				}
			}
			return false;
		}


		private boolean positionCapturedByPawn(Square square) {
			Iterator<PiecePositioned> iterator = piecePlacementReader.iterator( pawnJumps[square.toIdx()] );
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(pawn.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}
		
		private boolean positionCapturedByKing(Square square) {
			Iterator<PiecePositioned> iterator = new KingBitIterator<PiecePositioned>(piecePlacementReader, square);
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				if (king.equals(destino.getValue())) {
					return true;
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