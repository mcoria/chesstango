package parsers;

import builder.ChessBuilder;

public class FENBoarBuilder extends ChessBuilder {

	private FENParser parser = new FENParser();
	
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
}
