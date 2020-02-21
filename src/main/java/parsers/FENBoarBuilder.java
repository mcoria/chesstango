package parsers;

import builder.BoardBuilder;
import chess.Pieza;

public class FENBoarBuilder extends BoardBuilder {

	private FENParser parser = new FENParser();

	@Override
	protected Pieza[][] getTablero() {
		return parser.getTablero();
	}
	
	public FENBoarBuilder withDefaultBoard(){
		withFEN(FENParser.INITIAL_FEN);
		return this;
	}	

	public FENBoarBuilder withFEN(String fenString){
		parser.parseFEN(fenString);
		this.withTablero(parser.getTablero());
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
		this.withTablero(parser.getTablero());
		return this;
	}
	
	public FENBoarBuilder withTurno(String activeColor){
		parser.parseTurno(activeColor);
		this.withTurno(parser.getTurno());
		return this;
	}
	
	public FENBoarBuilder withPeonPasanteSquare(String peonPasante){
		parser.parsePeonPasanteSquare(peonPasante);
		this.withPeonPasanteSquare(parser.getPeonPasanteSquare());
		return this;
	}	
}
