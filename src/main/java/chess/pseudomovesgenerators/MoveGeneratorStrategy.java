package chess.pseudomovesgenerators;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moves.MoveFactory;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorStrategy {
	private PosicionPiezaBoard dummyBoard;
	private ColorBoard colorBoard;
	private BoardState boardState;
	private MoveFactory moveFactory;
	
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
	private KingBlancoMoveGenerator rbmg;
	private KingNegroMoveGenerator rnmg;

	private PeonPasanteMoveGenerator ppmg = null;
	
	public MoveGeneratorStrategy() {
		pbmg =  new PeonBlancoMoveGenerator();
		
		pnmg = new PeonNegroMoveGenerator();
		
		tbmg = new TorreMoveGenerator(Color.WHITE);
		
		tnmg = new TorreMoveGenerator(Color.BLACK);
		
		cbmg = new CaballoMoveGenerator(Color.WHITE);
		
		cnmg = new CaballoMoveGenerator(Color.BLACK);
		
		abmg = new AlfilMoveGenerator(Color.WHITE);
		
		anmg = new AlfilMoveGenerator(Color.BLACK);
		
		rebmg = new ReinaMoveGenerator(Color.WHITE);
		
		renmg = new ReinaMoveGenerator(Color.BLACK);
		
		rbmg = new KingBlancoMoveGenerator();
		
		rnmg = new KingNegroMoveGenerator();
		
		ppmg = new PeonPasanteMoveGenerator();
		
		moveFactory = new MoveFactory();
	}

	public KingAbstractMoveGenerator getKingMoveGenerator(Color color) {
		return Color.WHITE.equals(color) ? this.rbmg : this.rnmg;
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

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
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
	
	public KingBlancoMoveGenerator getKingBlancoMoveGenerator() {
		return rbmg;
	}
	
	public KingNegroMoveGenerator getKingNegroMoveGenerator() {
		return rnmg;
	}
	
	public PeonPasanteMoveGenerator getPeonPasanteMoveGenerator() {
		return ppmg;
	}
	
	public MoveGenerator getMoveGenerator(Pieza pieza){
		MoveGenerator value  = null;
		switch (pieza) {
		case PEON_WHITE:
			value = this.pbmg;
			break;
		case PEON_BLACK:
			value = this.pnmg;
			break;
		case TORRE_WHITE:
			value = this.tbmg;
			break;
		case TORRE_BLACK:
			value = this.tnmg;
			break;
		case CABALLO_WHITE:
			value = this.cbmg;
			break;
		case CABALLO_BLACK:
			value = this.cnmg;
			break;
		case ALFIL_WHITE:
			value = this.abmg;
			break;
		case ALFIL_BLACK:
			value = this.anmg;
			break;
		case QUEEN_WHITE:
			value = this.rebmg;
			break;
		case QUEEN_BLACK:
			value = this.renmg;
			break;
		case KING_WHITE:
			value = this.rbmg;
			break;
		case KING_BLACK:
			value = this.rnmg;
			break;
		default:
			throw new RuntimeException("Generator not found");
		}
		return value;
	}
	
	
	private void settupMoveGenerator(MoveGenerator moveGenerator) {
		if (moveGenerator instanceof AbstractMoveGenerator) {
			((AbstractMoveGenerator)moveGenerator).setTablero(dummyBoard);
			((AbstractMoveGenerator)moveGenerator).setColorBoard(colorBoard);		
			((AbstractMoveGenerator)moveGenerator).setMoveFactory(moveFactory);
		}
		
		if (moveGenerator instanceof KingAbstractMoveGenerator) {
			KingAbstractMoveGenerator generator = (KingAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
		}
	}
	

	private void settupPeonPasanteMoveGenerator() {
		ppmg.setBoardState(boardState);
		ppmg.setTablero(dummyBoard);
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
		
		settupPeonPasanteMoveGenerator();
	}	

}
