package parsers;

import java.util.Iterator;

import chess.Board;
import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Game;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.TopDownSquareIterator;

public class FENCoder {
	
	private String tablero;

	private String turno;

	private String peonPasanteSquare;

	private String enroques;	
	
	
	public String code(Game input) {
		return code(input.getTablero());
	}

	public String code(Board input) {
		BoardState state = input.getBoardState();
		char colorActual = state.getTurnoActual().equals(Color.BLANCO) ? 'w' : 'b';
		String peonPasante = codePeonPasante(state.getPeonPasanteSquare());
		String enroques = codeEnroques(state);
		return codePiecePlacement(input.getDummyBoard()) + " " + colorActual + " " + enroques + " " + peonPasante + " 0 1";
	}

	public String codePiecePlacement(DummyBoard board) {
		int idx = 0;
		int idxLinea = 0;
		Pieza[] piezas = new Pieza[8];
		String[] lineasStr = new String[8];
		for (Iterator<PosicionPieza> iterator = board.iterator(new TopDownSquareIterator()); iterator.hasNext();) {
			PosicionPieza element = iterator.next();
			Pieza pieza = element.getValue();
			piezas[idx] = pieza;
			if (idx == 7) {
				String lineaStr = codePiecePlacement(piezas);
				lineasStr[idxLinea] = lineaStr;
				idx = -1;
				idxLinea++;
			}
			idx++;
		}
		return lineasStr[0] + '/' + lineasStr[1] + '/' + lineasStr[2] + '/' + lineasStr[3] + '/' + lineasStr[4] + '/'
				+ lineasStr[5] + '/' + lineasStr[6] + '/' + lineasStr[7];
	}

	protected String codePiecePlacement(Pieza[] piezas) {
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
	
	public String codePeonPasante(Square peonPasanteSquare) {
		String result = "-";
		if (peonPasanteSquare != null) {
			result = peonPasanteSquare.toString();
		}
		return result;
	}
	
	protected String codeEnroques(BoardState state) {
		String result =  ((state.isEnroqueBlancoReyPermitido() ? "K" : "") + (state.isEnroqueBlancoReinaPermitido() ? "Q" : "")
				+ (state.isEnroqueNegroReyPermitido() ? "k" : "") + (state.isEnroqueNegroReinaPermitido() ? "q" : ""));
		
		return "".equals(result) ? "-" : result;
	}

	public String getTablero() {
		return tablero;
	}

	public String getTurno() {
		return turno;
	}

	public String getPeonPasanteSquare() {
		return peonPasanteSquare;
	}

	public String getEnroques() {
		return enroques;
	}
}
