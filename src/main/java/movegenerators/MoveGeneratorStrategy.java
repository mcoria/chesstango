package movegenerators;

import chess.Board;
import chess.Color;
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

	public MoveGeneratorStrategy(Board board) {
		pbmg =  new PeonBlancoMoveGenerator();
		board.settupMoveGenerator(pbmg);
		
		pnmg = new PeonNegroMoveGenerator();
		board.settupMoveGenerator(pnmg);
		
		tbmg = new TorreMoveGenerator(Color.BLANCO);
		board.settupMoveGenerator(tbmg);
		
		tnmg = new TorreMoveGenerator(Color.NEGRO);
		board.settupMoveGenerator(tnmg);
		
		cbmg = new CaballoMoveGenerator(Color.BLANCO);
		board.settupMoveGenerator(cbmg);
		
		cnmg = new CaballoMoveGenerator(Color.NEGRO);
		board.settupMoveGenerator(cnmg);
		
		abmg = new AlfilMoveGenerator(Color.BLANCO);
		board.settupMoveGenerator(abmg);
		
		anmg = new AlfilMoveGenerator(Color.NEGRO);
		board.settupMoveGenerator(anmg);
		
		rebmg = new ReinaMoveGenerator(Color.BLANCO);
		board.settupMoveGenerator(rebmg);
		
		renmg = new ReinaMoveGenerator(Color.NEGRO);
		board.settupMoveGenerator(renmg);
		
		rbmg = new ReyBlancoMoveGenerator();
		board.settupMoveGenerator(rbmg);
		
		rnmg = new ReyNegroMoveGenerator();
		board.settupMoveGenerator(rnmg);
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
