package chess.parsers;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.builder.ChessBuilder;

/**
 * @author Mauricio Coria
 *
 */
public class FENCoder implements ChessBuilder {
	
	private Color turno;
	private Square peonPasanteSquare;
	private boolean enroqueNegroKingAllowed;
	private boolean enroqueNegroQueenAllowed;
	private boolean enroqueBlancoKingAllowed;
	private boolean enroqueBlancoQueenAllowed;	
	
	private Piece[][] tablero = new Piece[8][8];

	public String getFEN() {
		final StringBuilder stringBuilder = new StringBuilder(70);
		
		getPiecePlacement(stringBuilder).append(' ');
		
		getTurno(stringBuilder).append(' ');
		
		getEnroques(stringBuilder).append(' ');
		
		getPawnPasante(stringBuilder).append(" 0 1");

		return stringBuilder.toString();
	}


	public StringBuilder getTurno(StringBuilder stringBuilder) {
		return Color.WHITE.equals(turno) ? stringBuilder.append('w') : stringBuilder.append('b');
	}

	public StringBuilder getPiecePlacement(StringBuilder stringBuilder) {
		for (int i = 7; i >= 0; i--) {
			codePiecePlacementRank(tablero[i], stringBuilder);
			if(i > 0){
				stringBuilder.append('/');
			}
		}
		
		return stringBuilder;
	}
	
	public StringBuilder getPawnPasante(StringBuilder stringBuilder) {
		if (peonPasanteSquare == null) {
			stringBuilder.append('-');
		} else {
			stringBuilder.append(peonPasanteSquare.toString());
		}
		return stringBuilder;
	}
	
	public StringBuilder getEnroques(StringBuilder stringBuilder) {
		if(enroqueBlancoKingAllowed){
			stringBuilder.append('K');
		}
		
		if(enroqueBlancoQueenAllowed){
			stringBuilder.append('Q');
		}
		
		if(enroqueNegroKingAllowed){
			stringBuilder.append('k');
		}
		
		if(enroqueNegroQueenAllowed){
			stringBuilder.append('q');
		}		
		
		if(!enroqueBlancoKingAllowed && !enroqueBlancoQueenAllowed && !enroqueNegroKingAllowed && !enroqueNegroQueenAllowed){
			stringBuilder.append('-');
		}
				
		return stringBuilder;
	}	

	@Override
	public void withPieza(Square square, Piece piece) {
		this.tablero[square.getRank()][square.getFile()] = piece;
	}
	
	@Override
	public void withTurno(Color turno) {
		this.turno = turno;
	}
	
	@Override
	public void withPawnPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed) {
		this.enroqueNegroKingAllowed = enroqueNegroKingAllowed;
	}
	
	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed) {
		this.enroqueNegroQueenAllowed = enroqueNegroQueenAllowed;
	}
	
	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed) {
		this.enroqueBlancoKingAllowed = enroqueBlancoKingAllowed;
	}
	
	@Override
	public void withCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed) {
		this.enroqueBlancoQueenAllowed = enroqueBlancoQueenAllowed;
	}
	
	protected StringBuilder codePiecePlacementRank(Piece[] piezas, StringBuilder stringBuilder) {
		int vacios = 0;
		for (int i = 0; i < piezas.length; i++) {
			if(piezas[i] == null){
				vacios++;
			} else {
				if(vacios > 0){
					stringBuilder.append(vacios);
					vacios = 0;
				}
				stringBuilder.append(getCode(piezas[i]));
			}
		}
		
		if(vacios > 0){
			stringBuilder.append(vacios);
		}		
	
		return stringBuilder;
	}
	
	private char getCode(Piece piece) {
		char result;
		switch (piece) {
		case ROOK_BLACK:
			result = 'r';
			break;
		case KNIGHT_BLACK:
			result = 'n';
			break;
		case QUEEN_BLACK:
			result = 'q';
			break;
		case KING_BLACK:
			result = 'k';
			break;
		case PAWN_BLACK:
			result = 'p';
			break;
		case BISHOP_BLACK:
			result = 'b';
			break;
		case ROOK_WHITE:
			result = 'R';
			break;
		case KNIGHT_WHITE:
			result = 'N';
			break;
		case QUEEN_WHITE:
			result = 'Q';
			break;
		case KING_WHITE:
			result = 'K';
			break;
		case PAWN_WHITE:
			result = 'P';
			break;
		case BISHOP_WHITE:
			result = 'B';
			break;
		default:
			throw new RuntimeException("Falta pieza");
		}
		return result;
	}	

}
