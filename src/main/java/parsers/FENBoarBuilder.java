package parsers;

import builder.ChessBuilder;
import chess.Pieza;
import chess.Square;

public class FENBoarBuilder extends ChessBuilder {

	private FENParser parser = new FENParser();
	
	public FENBoarBuilder withDefaultBoard(){
		withFEN(FENParser.INITIAL_FEN);
		return this;
	}	

	public FENBoarBuilder withFEN(String fenString){
		parser.parseFEN(fenString);
		this.buildTablero(parser.getTablero());
		this.withTurno(parser.getTurno());
		this.withPeonPasanteSquare(parser.getPeonPasanteSquare());
		this.withEnroqueBlancoReyPermitido(parser.isEnroqueBlancoReyPermitido());
		this.withEnroqueBlancoReinaPermitido(parser.isEnroqueBlancoReinaPermitido());
		this.withEnroqueNegroReyPermitido(parser.isEnroqueNegroReyPermitido());
		this.withEnroqueNegroReinaPermitido(parser.isEnroqueNegroReinaPermitido());
		return this;
	}
	
	public FENBoarBuilder withTablero(String piecePlacement){
		parser.parsePiecePlacement(piecePlacement);
		this.buildTablero(parser.getTablero());
		return this;
	}
	
	protected void buildTablero(Pieza[][] piezas){
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				Square square = Square.getSquare(file, rank);
				Pieza pieza = piezas[file][rank];
				if(pieza != null){
					super.withPieza(square, pieza);
				}
			}
		}
	}
}
