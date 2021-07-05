package parsers;

import builder.ChessBuilder;
import chess.Pieza;
import chess.Square;

public class FENBoarBuilder<TChessBuilder extends ChessBuilder> {

	private FENParser parser = new FENParser();
	
	private TChessBuilder builder;
	
	public FENBoarBuilder(TChessBuilder builder) {
		this.builder = builder;
	}
	
	public FENBoarBuilder<TChessBuilder> constructDefaultBoard(){
		constructFEN(FENParser.INITIAL_FEN);
		return this;
	}	

	public FENBoarBuilder<TChessBuilder> constructFEN(String fenString){
		parser.parseFEN(fenString);
		buildTablero(parser.getTablero());
		builder.withTurno(parser.getTurno());
		builder.withPeonPasanteSquare(parser.getPeonPasanteSquare());
		builder.withEnroqueBlancoReyPermitido(parser.isEnroqueBlancoReyPermitido());
		builder.withEnroqueBlancoReinaPermitido(parser.isEnroqueBlancoReinaPermitido());
		builder.withEnroqueNegroReyPermitido(parser.isEnroqueNegroReyPermitido());
		builder.withEnroqueNegroReinaPermitido(parser.isEnroqueNegroReinaPermitido());
		return this;
	}
	
	
	public FENBoarBuilder<TChessBuilder> constructTablero(String piecePlacement){
		parser.parsePiecePlacement(piecePlacement);
		this.buildTablero(parser.getTablero());
		return this;
	}	
	
	public TChessBuilder getBuilder(){
		return builder;
	}
	
	protected void buildTablero(Pieza[][] piezas){
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				Square square = Square.getSquare(file, rank);
				Pieza pieza = piezas[file][rank];
				if(pieza != null){
					builder.withPieza(square, pieza);
				}
			}
		}
	}
}
