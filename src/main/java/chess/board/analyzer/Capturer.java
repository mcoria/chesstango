package chess.board.analyzer;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.iterators.square.JumpSquareIterator;
import chess.board.position.PiecePlacementReader;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;



/**
 * @author Mauricio Coria
 *
 */
// TODO: el capturer para analyzer es distinto, deberia 
//       - buscar todas las posibilidades de captura de Rey
//       - durante la busqueda deberia identificar posiciones pinned
//       - deberia haber un capturer de posicion mas sencillo para LegalMoveGenerator
//		 - deberiamos tener un capturer especifico para Castling
public class Capturer {
	
	private static final long Knight_ARRAY_SALTOS[] = { 132096L, 329728L, 659712L, 1319424L, 2638848L, 5277696L, 10489856L, 4202496L,
			33816580L, 84410376L, 168886289L, 337772578L, 675545156L, 1351090312L, 2685403152L, 1075839008L,
			8657044482L, 21609056261L, 43234889994L, 86469779988L, 172939559976L, 345879119952L, 687463207072L,
			275414786112L, 2216203387392L, 5531918402816L, 11068131838464L, 22136263676928L, 44272527353856L,
			88545054707712L, 175990581010432L, 70506185244672L, 567348067172352L, 1416171111120896L, 2833441750646784L,
			5666883501293568L, 11333767002587136L, 22667534005174272L, 45053588738670592L, 18049583422636032L,
			145241105196122112L, 362539804446949376L, 725361088165576704L, 1450722176331153408L, 2901444352662306816L,
			5802888705324613632L, -6913025356609880064L, 4620693356194824192L, 288234782788157440L, 576469569871282176L,
			1224997833292120064L, 2449995666584240128L, 4899991333168480256L, -8646761407372591104L,
			1152939783987658752L, 2305878468463689728L, 1128098930098176L, 2257297371824128L, 4796069720358912L,
			9592139440717824L, 19184278881435648L, 38368557762871296L, 4679521487814656L, 9077567998918656L };

	private static final long King_ARRAY_SALTOS[] = {
			770L, 1797L, 3594L, 7188L, 14376L, 28752L, 57504L, 49216L, 197123L, 460039L, 920078L, 1840156L, 3680312L,
			7360624L, 14721248L, 12599488L, 50463488L, 117769984L, 235539968L, 471079936L, 942159872L, 1884319744L,
			3768639488L, 3225468928L, 12918652928L, 30149115904L, 60298231808L, 120596463616L, 241192927232L,
			482385854464L, 964771708928L, 825720045568L, 3307175149568L, 7718173671424L, 15436347342848L,
			30872694685696L, 61745389371392L, 123490778742784L, 246981557485568L, 211384331665408L, 846636838289408L,
			1975852459884544L, 3951704919769088L, 7903409839538176L, 15806819679076352L, 31613639358152704L,
			63227278716305408L, 54114388906344448L, 216739030602088448L, 505818229730443264L, 1011636459460886528L,
			2023272918921773056L, 4046545837843546112L, 8093091675687092224L, -2260560722335367168L,
			-4593460513685372928L, 144959613005987840L, 362258295026614272L, 724516590053228544L, 1449033180106457088L,
			2898066360212914176L, 5796132720425828352L, -6854478632857894912L, 4665729213955833856L };
	
	private final PiecePlacementReader piecePlacementReader;
	private final ImprovedCapturerColor capturerWhite = new ImprovedCapturerColor(Color.WHITE);
	private final ImprovedCapturerColor capturerBlack = new ImprovedCapturerColor(Color.BLACK);
	
	public Capturer(PiecePlacementReader piecePlacementReader) {
		this.piecePlacementReader = piecePlacementReader;
	}	

	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class ImprovedCapturerColor {
		private final Piece torre;
		private final Piece alfil;
		private final Piece queen;
		private final Piece caballo;
		private final int[][] saltosPawn;
		private final Piece pawn;
		private final Piece king;
		
		private final int[][] casillerosPawnWhite = {
				{ -1, -1 }, 
				{ 1, -1 }
			};
				
		private final int[][] casillerosPawnBlack = {
			{ -1, 1 }, 
			{ 1, 1 }
		};		

		
		public ImprovedCapturerColor(Color color) {
			torre =  Piece.getRook(color);
			alfil = Piece.getBishop(color);
			queen = Piece.getQueen(color);
			caballo = Piece.getKnight(color);
			pawn = Piece.getPawn(color);
			king = Piece.getKing(color);		
			

			if (Color.WHITE.equals(color)) {
				saltosPawn = casillerosPawnWhite;
			} else {
				saltosPawn = casillerosPawnBlack;
			}		
		}

		public boolean positionCaptured(Square square) {
			if(positionCapturedByKnight(square)	||
			   positionCapturedByRook(square)	||
			   positionCapturedByBishop(square)   ||
			   positionCapturedByPawn(square) ||
			   positionCapturedByKing(square)) {
				return true;
			}
			return false;
		}

		private final Cardinal[]  direccionesBishop = BishopMoveGenerator.BISHOP_CARDINAL;
		private boolean positionCapturedByBishop(Square square) {
			return positionCapturedByDireccion(square, direccionesBishop,  alfil);
		}

		private final Cardinal[]  direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
		private boolean positionCapturedByRook(Square square) {		
			return positionCapturedByDireccion(square, direccionesRook, torre);
		}

		private boolean positionCapturedByDireccion(Square square, Cardinal[] direcciones, Piece torreOalfil) {		
			for (Cardinal cardinal : direcciones) {
				if(positionCapturedByCardinalPieza(torreOalfil, queen, square, cardinal)){
					return true;
				}
			}
			return false;
		}
		
		private boolean positionCapturedByKnight(Square square) {
			PiecePlacementIterator iterator = piecePlacementReader.iterator( Knight_ARRAY_SALTOS[square.toIdx()] );
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(caballo.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}		
		
		private boolean positionCapturedByCardinalPieza(Piece torreOalfil, Piece queen, Square square, Cardinal cardinal) {
			PiecePlacementIterator iterator = piecePlacementReader.iterator(new CardinalSquareIterator(square, cardinal));
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				Piece piece = destino.getValue();
				if (piece == null) {
					continue;
				} else if (queen.equals(piece)) {
					return true;
				} else if (torreOalfil.equals(piece)) {			
					return true;
				} else {
					break;
				}
			}
			return false;
		}


		private boolean positionCapturedByPawn(Square square) {
			PiecePlacementIterator iterator = piecePlacementReader.iterator(new JumpSquareIterator(square, saltosPawn));
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(pawn.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}
		
		private boolean positionCapturedByKing(Square square) {
			PiecePlacementIterator iterator = piecePlacementReader.iterator(King_ARRAY_SALTOS[square.toIdx()]);
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				if (king.equals(destino.getValue())) {
					return true;
				}
			}
			return false;
		}	

	}
}