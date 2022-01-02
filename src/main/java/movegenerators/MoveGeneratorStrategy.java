package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorStrategy {
	private PosicionPiezaBoard dummyBoard;
	private ColorBoard colorBoard;
	private BoardState boardState;
	
	private PeonBlancoMoveGenerator pbmg;
	private PeonNegroMoveGenerator pnmg;
	private TorreMoveGenerator tbmg;
	private TorreMoveGenerator tnmg;
	private CaballoMoveGenerator cbmg;
	private CaballoMoveGenerator cnmg;
	private AlfilMoveGenerator abmg;
	private AlfilMoveGenerator anmg;
	private ReinaMoveGenerator rebmg;
	private ReinaMoveGenerator renmg;
	private ReyBlancoMoveGenerator rbmg;
	private ReyNegroMoveGenerator rnmg;

	public MoveGeneratorStrategy() {
		pbmg =  new PeonBlancoMoveGenerator();
		
		pnmg = new PeonNegroMoveGenerator();
		
		tbmg = new TorreMoveGenerator(Color.BLANCO);
		
		tnmg = new TorreMoveGenerator(Color.NEGRO);
		
		cbmg = new CaballoMoveGenerator(Color.BLANCO);
		
		cnmg = new CaballoMoveGenerator(Color.NEGRO);
		
		abmg = new AlfilMoveGenerator(Color.BLANCO);
		
		anmg = new AlfilMoveGenerator(Color.NEGRO);
		
		rebmg = new ReinaMoveGenerator(Color.BLANCO);
		
		renmg = new ReinaMoveGenerator(Color.NEGRO);
		
		rbmg = new ReyBlancoMoveGenerator();
		
		rnmg = new ReyNegroMoveGenerator();
	}

	public ReyAbstractMoveGenerator getReyMoveGenerator(Color color) {
		return Color.BLANCO.equals(color) ? this.rbmg : this.rnmg;
	}
	public void setDummyBoard(PosicionPiezaBoard dummyBoard) {
		this.dummyBoard = dummyBoard;
		settupMoveGenerators();
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
		settupMoveGenerators();
	}

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
		settupMoveGenerators();
	}	

	public PeonBlancoMoveGenerator getPeonBlancoMoveGenerator() {
		return pbmg;
	}

	public PeonNegroMoveGenerator getPeonNegroMoveGenerator() {
		return pnmg;
	}

	public TorreMoveGenerator getTorreBlancaMoveGenerator() {
		return tbmg;
	}

	public TorreMoveGenerator getTorreNegraMoveGenerator() {
		return tnmg;
	}	
	
	public CaballoMoveGenerator getCaballoBlancoMoveGenerator() {
		return cbmg;
	}
	
	public CaballoMoveGenerator getCaballoNegroMoveGenerator() {
		return cnmg;
	}	
	
	public AlfilMoveGenerator getAlfilBlancoMoveGenerator() {
		return abmg;
	}
	
	public AlfilMoveGenerator getAlfilNegroMoveGenerator() {
		return anmg;
	}	
	
	public ReinaMoveGenerator getReinaBlancaMoveGenerator() {
		return rebmg;
	}
	
	public ReinaMoveGenerator getReinaNegraMoveGenerator() {
		return renmg;
	}	
	
	public ReyBlancoMoveGenerator getReyBlancoMoveGenerator() {
		return rbmg;
	}
	
	public ReyNegroMoveGenerator getReyNegroMoveGenerator() {
		return rnmg;
	}
	
	private void settupMoveGenerators(){
		settupMoveGenerator(pbmg);

		settupMoveGenerator(pnmg);

		settupMoveGenerator(tbmg);
		
		settupMoveGenerator(tnmg);
		
		settupMoveGenerator(cbmg);
		
		settupMoveGenerator(cnmg);
		
		settupMoveGenerator(abmg);
		
		settupMoveGenerator(anmg);
		
		settupMoveGenerator(rebmg);
		
		settupMoveGenerator(renmg);
		
		settupMoveGenerator(rbmg);
		
		settupMoveGenerator(rnmg);
	}
	
	private void settupMoveGenerator(MoveGenerator moveGenerator) {
		if (moveGenerator instanceof AbstractMoveGenerator) {
			((AbstractMoveGenerator)moveGenerator).setTablero(dummyBoard);
			((AbstractMoveGenerator)moveGenerator).setColorBoard(colorBoard);		
		}
		
		if (moveGenerator instanceof ReyAbstractMoveGenerator) {
			ReyAbstractMoveGenerator generator = (ReyAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
		}
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
