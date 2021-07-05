package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.IsKingInCheck;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import positioncaptures.ImprovedCapturer;

public class MoveGeneratorStrategy {
	private PosicionPiezaBoard dummyBoard;
	private ColorBoard colorBoard;
	private BoardState boardState;
	private IsKingInCheck isKingInCheck;
	
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

	public void setIsKingInCheck(IsKingInCheck isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
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
		}
		
		if (moveGenerator instanceof PeonAbstractMoveGenerator) {
			PeonAbstractMoveGenerator generator = (PeonAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			
		} else if (moveGenerator instanceof ReyAbstractMoveGenerator) {
			ReyAbstractMoveGenerator generator = (ReyAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			generator.setCapturer(new ImprovedCapturer(dummyBoard));
			generator.setKingInCheck(isKingInCheck);
			generator.setColorBoard(colorBoard);
			
		} else if(moveGenerator instanceof CardinalMoveGenerator){
			CardinalMoveGenerator generator = (CardinalMoveGenerator) moveGenerator;
			generator.setColorBoard(colorBoard);
		}
	}	
}
