package chess.board.analyzer;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;



/**
 * @author Mauricio Coria
 *
 */
// TODO: el capturer para analyzer es distinto, deberia 
//       	- buscar todas las posibilidades de captura de Rey
//       	- durante la busqueda deberia identificar posiciones pinned
//       - deberiamos tener un capturer de posicion mas sencillo para LegalMoveGenerator
//			- Si no se encuentra en Jaque NO es necesario preguntar por jaque de caballo; rey o peon !!!
//				deberia buscar el jaque en direccion del pinned
//			- cuando mueve el rey deberia preguntar por todas las posibilidades de captura
//		 - deberiamos tener un capturer especifico para Castling
public class CheckAndPinnedAnalyzer {
	
	private final ChessPositionReader positionReader;
	
	private final CheckAndPinnedAnalyzerByColor analyzerWhite = new CheckAndPinnedAnalyzerByColor(Color.WHITE, PawnWhite_ARRAY_SALTOS);
	private final CheckAndPinnedAnalyzerByColor analyzerBlack = new CheckAndPinnedAnalyzerByColor(Color.BLACK, PawnBlack_ARRAY_SALTOS);
	
	private long pinnedPositions;
	
	private boolean kingInCheck;
	
	public CheckAndPinnedAnalyzer(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}	

	public void analyze() {
		pinnedPositions = 0;
		kingInCheck = false;
		
		Color turnoActual = positionReader.getTurnoActual();
		
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


	private class CheckAndPinnedAnalyzerByColor {
		private final Color color;
		private final Piece torre;
		private final Piece alfil;
		private final Piece queen;
		private final Piece knight;
		private final long[] pawnJumps;
		private final Piece pawn;

		
		public CheckAndPinnedAnalyzerByColor(Color color, long[] pawnJumps) {
			this.color = color;
			this.torre =  Piece.getRook(color);
			this.alfil = Piece.getBishop(color);
			this.queen = Piece.getQueen(color);
			this.knight = Piece.getKnight(color);
			this.pawn = Piece.getPawn(color);
			this.pawnJumps = pawnJumps;
				
		}

		public void analyze() {
			Square squareKingOpponent = positionReader.getKingSquare(color.oppositeColor());

			analyzeByKnight(squareKingOpponent);

			if (CheckAndPinnedAnalyzer.this.kingInCheck == false){
				analyzeByPawn(squareKingOpponent);
			}
			
			if (CheckAndPinnedAnalyzer.this.kingInCheck == false){
				analyzeByBishop(squareKingOpponent);
			}
			
			if (CheckAndPinnedAnalyzer.this.kingInCheck == false){
				analyzeByRook(squareKingOpponent);
			}			
			
			// Jamas el rey va a estar en jaque por el Rey oponente
			// analyzeByKing(squareKing);

		}
		
		private void analyzeByKnight(Square squareKingOpponent) {
			PiecePlacementIterator iterator = positionReader.iterator( Knight_ARRAY_SALTOS[squareKingOpponent.toIdx()] );
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(knight.equals(destino.getValue())){		    	
			    	CheckAndPinnedAnalyzer.this.kingInCheck = true;
			    	return;
			    }
			}
		}
		
		private void analyzeByPawn(Square squareKingOpponent) {
			PiecePlacementIterator iterator = positionReader.iterator( pawnJumps[squareKingOpponent.toIdx()] );
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
			positionCapturedByDireccion(square, direccionesBishop, this.alfil);
		}

		private final Cardinal[]  direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
		private void analyzeByRook(Square squareKingOpponent) {		
			positionCapturedByDireccion(squareKingOpponent, direccionesRook, this.torre);
		}

		private void positionCapturedByDireccion(Square squareKingOpponent, Cardinal[] direcciones, Piece torreOalfil) {		
			for (Cardinal cardinal : direcciones) {
				if(positionCapturedByCardinalPieza(squareKingOpponent, cardinal, torreOalfil)){
					CheckAndPinnedAnalyzer.this.kingInCheck = true;
					return;
				}
			}
		}		
		
		private boolean positionCapturedByCardinalPieza(Square squareKingOpponent, Cardinal cardinal, Piece torreOalfil) {
			Color opponentColor = this.color.oppositeColor();
			
			PiecePositioned possiblePinned = null;
			
			PiecePlacementIterator iterator = positionReader.iterator(new CardinalSquareIterator(squareKingOpponent, cardinal));
			
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
						} else return this.queen.equals(piece) || torreOalfil.equals(piece);
					} else {
						
						// La pieza es nuestra y de las que ponen en jaque al oponente, tenemos pinned
						if (this.queen.equals(piece) || torreOalfil.equals(piece)) {
							// Confirmado, tenemos pinned
							CheckAndPinnedAnalyzer.this.pinnedPositions |= possiblePinned.getKey().getPosicion();
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
	
	
	private static final long[] Knight_ARRAY_SALTOS = { 132096L, 329728L, 659712L, 1319424L, 2638848L, 5277696L, 10489856L, 4202496L,
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