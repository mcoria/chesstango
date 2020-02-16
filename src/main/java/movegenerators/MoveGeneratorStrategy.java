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
		
		pnmg = new PeonNegroMoveGenerator();
		pnmg.setTablero(board);
		
		tbmg = new TorreMoveGenerator(Color.BLANCO);
		tbmg.setTablero(board);
		
		tnmg = new TorreMoveGenerator(Color.NEGRO);
		tnmg.setTablero(board);
		
		cbmg = new CaballoMoveGenerator(Color.BLANCO);
		cbmg.setTablero(board);
		
		cnmg = new CaballoMoveGenerator(Color.NEGRO);
		cnmg.setTablero(board);
		
		abmg = new AlfilMoveGenerator(Color.BLANCO);
		abmg.setTablero(board);
		
		anmg = new AlfilMoveGenerator(Color.NEGRO);
		anmg.setTablero(board);
		
		rebmg = new ReinaMoveGenerator(Color.BLANCO);
		rebmg.setTablero(board);
		
		renmg = new ReinaMoveGenerator(Color.NEGRO);
		renmg.setTablero(board);
		
		rbmg = new ReyBlancoMoveGenerator();
		rbmg.setTablero(board);
		
		rnmg = new ReyNegroMoveGenerator();
		rnmg.setTablero(board);
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
