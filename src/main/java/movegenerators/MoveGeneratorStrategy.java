package movegenerators;

import chess.Color;
import chess.Board;
import chess.Pieza;

public class MoveGeneratorStrategy {
	
	private PeonBlancoMoveGenerator pbmg;
	private PeonNegroMoveGenerator pnmg;
	private TorreMoveGenerator tnmg;
	private TorreMoveGenerator tbmg;
	private CaballoMoveGenerator cnmg;
	private CaballoMoveGenerator cbmg;
	private AlfilMoveGenerator abmg;
	private AlfilMoveGenerator anmg;
	private ReinaMoveGenerator rebmg;
	private ReinaMoveGenerator renmg;
	private ReyBlancoMoveGenerator rbmg;
	private ReyNegroMoveGenerator rnmg;

	public MoveGeneratorStrategy(Board board, MoveFilter filter) {
		pbmg =  new PeonBlancoMoveGenerator();
		pbmg.setTablero(board);
		pbmg.setFilter(filter);
		
		pnmg = new PeonNegroMoveGenerator();
		pnmg.setTablero(board);
		pnmg.setFilter(filter);
		
		tbmg = new TorreMoveGenerator(Color.BLANCO);
		tbmg.setTablero(board);
		tbmg.setFilter(filter);
		
		tnmg = new TorreMoveGenerator(Color.NEGRO);
		tnmg.setTablero(board);
		tnmg.setFilter(filter);
		
		cbmg = new CaballoMoveGenerator(Color.BLANCO);
		cbmg.setTablero(board);
		cbmg.setFilter(filter);
		
		cnmg = new CaballoMoveGenerator(Color.NEGRO);
		cnmg.setTablero(board);
		cnmg.setFilter(filter);
		
		abmg = new AlfilMoveGenerator(Color.BLANCO);
		abmg.setTablero(board);
		abmg.setFilter(filter);
		
		anmg = new AlfilMoveGenerator(Color.NEGRO);
		anmg.setTablero(board);
		anmg.setFilter(filter);
		
		rebmg = new ReinaMoveGenerator(Color.BLANCO);
		rebmg.setTablero(board);
		rebmg.setFilter(filter);
		
		renmg = new ReinaMoveGenerator(Color.NEGRO);
		renmg.setTablero(board);
		renmg.setFilter(filter);
		
		rbmg = new ReyBlancoMoveGenerator();
		rbmg.setTablero(board);
		rbmg.setFilter(filter);
		
		rnmg = new ReyNegroMoveGenerator();
		rnmg.setTablero(board);
		rnmg.setFilter(filter);
	}
	
	public MoveGenerator getMoveGenerator(Pieza pieza){
		MoveGenerator value  = null;
		switch (pieza) {
		case PEON_BLANCO:
			value = this.pbmg;
			break;
		case PEON_NEGRO:
			value = this.pnmg;
			break;
		case TORRE_BLANCO:
			value = this.tbmg;
			break;
		case TORRE_NEGRO:
			value = this.tnmg;
			break;
		case CABALLO_BLANCO:
			value = this.cbmg;
			break;
		case CABALLO_NEGRO:
			value = this.cnmg;
			break;
		case ALFIL_BLANCO:
			value = this.abmg;
			break;
		case ALFIL_NEGRO:
			value = this.anmg;
			break;
		case REINA_BLANCO:
			value = this.rebmg;
			break;
		case REINA_NEGRO:
			value = this.renmg;
			break;
		case REY_BLANCO:
			value = this.rbmg;
			break;
		case REY_NEGRO:
			value = this.rnmg;
			break;
		default:
			throw new RuntimeException("Generator not found");
		}
		return value;
	}

}
