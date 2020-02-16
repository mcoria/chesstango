package movegenerators;

import chess.Color;
import chess.DummyBoard;
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

	public MoveGeneratorStrategy(DummyBoard board) {
		pbmg =  new PeonBlancoMoveGenerator();
		pbmg.setTablero(board);
		pbmg.setFilter(board);
		
		pnmg = new PeonNegroMoveGenerator();
		pnmg.setTablero(board);
		pnmg.setFilter(board);
		
		tbmg = new TorreMoveGenerator(Color.BLANCO);
		tbmg.setTablero(board);
		tbmg.setFilter(board);
		
		tnmg = new TorreMoveGenerator(Color.NEGRO);
		tnmg.setTablero(board);
		tnmg.setFilter(board);
		
		cbmg = new CaballoMoveGenerator(Color.BLANCO);
		cbmg.setTablero(board);
		cbmg.setFilter(board);
		
		cnmg = new CaballoMoveGenerator(Color.NEGRO);
		cnmg.setTablero(board);
		cnmg.setFilter(board);
		
		abmg = new AlfilMoveGenerator(Color.BLANCO);
		abmg.setTablero(board);
		abmg.setFilter(board);
		
		anmg = new AlfilMoveGenerator(Color.NEGRO);
		anmg.setTablero(board);
		anmg.setFilter(board);
		
		rebmg = new ReinaMoveGenerator(Color.BLANCO);
		rebmg.setTablero(board);
		rebmg.setFilter(board);
		
		renmg = new ReinaMoveGenerator(Color.NEGRO);
		renmg.setTablero(board);
		renmg.setFilter(board);
		
		rbmg = new ReyBlancoMoveGenerator();
		rbmg.setTablero(board);
		rbmg.setFilter(board);
		
		rnmg = new ReyNegroMoveGenerator();
		rnmg.setTablero(board);
		rnmg.setFilter(board);
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
