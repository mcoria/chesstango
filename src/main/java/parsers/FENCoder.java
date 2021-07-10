package parsers;

import builder.ChessBuilder;
import chess.Color;
import chess.Pieza;
import chess.Square;

public class FENCoder implements ChessBuilder {
	
	private Color turno;
	private Square peonPasanteSquare;
	private boolean enroqueNegroReyPermitido;
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueBlancoReyPermitido;
	private boolean enroqueBlancoReinaPermitido;	
	
	private Pieza[][] tablero = new Pieza[8][8];

	public String getFEN() {
		String colorActual = getTurno();
		String peonPasante = getPeonPasante();
		String enroques = getEnroques();
		String codePiecePlacement = getPiecePlacement();
		return codePiecePlacement + " " + colorActual + " " + enroques + " " + peonPasante + " 0 1";
	}


	public String getTurno() {
		return Color.BLANCO.equals(turno) ? "w" : "b";
	}

	public String getPiecePlacement() {
		String[] lineasStr = new String[8];
		for (int i = 7; i >= 0; i--) {
			String lineaStr = codePiecePlacementRank(tablero[i]);
			lineasStr[7 - i] = lineaStr;

		}
		return lineasStr[0] + '/' + lineasStr[1] + '/' + lineasStr[2] + '/' + lineasStr[3] + '/' + lineasStr[4] + '/'
				+ lineasStr[5] + '/' + lineasStr[6] + '/' + lineasStr[7];
	}
	
	public String getPeonPasante() {
		String result = "-";
		if (peonPasanteSquare != null) {
			result = peonPasanteSquare.toString();
		}
		return result;
	}
	
	public String getEnroques() {
		String result =  (enroqueBlancoReyPermitido ? "K" : "") + (enroqueBlancoReinaPermitido ? "Q" : "")
				+ (enroqueNegroReyPermitido ? "k" : "") + (enroqueNegroReinaPermitido ? "q" : "");
		
		return "".equals(result) ? "-" : result;
	}	

	@Override
	public void withPieza(Square square, Pieza pieza) {
		this.tablero[square.getRank()][square.getFile()] = pieza;
	}
	
	@Override
	public void withTurno(Color turno) {
		this.turno = turno;
	}
	
	@Override
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
	}
	
	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
	}
	
	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
	}
	
	@Override
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
	}
	
	protected String codePiecePlacementRank(Pieza[] piezas) {
		String result = "";
		int vacios = 0;
		for (int i = 0; i < piezas.length; i++) {
			if(piezas[i] == null){
				vacios++;
			} else {
				if(vacios > 0){
					result = result + vacios;
					vacios = 0;
				}
				char caracter = getCode(piezas[i]);
				result = result + caracter;
			}
		}
		
		if(vacios > 0){
			result = result + vacios;
			vacios = 0;
		}		
	
		return result;
	}
	
	private char getCode(Pieza pieza) {
		char result;
		switch (pieza) {
		case TORRE_NEGRO:
			result = 'r';
			break;
		case CABALLO_NEGRO:
			result = 'n';
			break;
		case REINA_NEGRO:
			result = 'q';
			break;
		case REY_NEGRO:
			result = 'k';
			break;
		case PEON_NEGRO:
			result = 'p';
			break;
		case ALFIL_NEGRO:
			result = 'b';
			break;
		case TORRE_BLANCO:
			result = 'R';
			break;
		case CABALLO_BLANCO:
			result = 'N';
			break;
		case REINA_BLANCO:
			result = 'Q';
			break;
		case REY_BLANCO:
			result = 'K';
			break;
		case PEON_BLANCO:
			result = 'P';
			break;
		case ALFIL_BLANCO:
			result = 'B';
			break;
		default:
			throw new RuntimeException("Falta pieza");
		}
		return result;
	}	

}
