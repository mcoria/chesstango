package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.IsKingInCheck;
import chess.IsPositionCaptured;
import chess.Pieza;
import layers.ColorBoard;
import layers.DummyBoard;

public class MoveGeneratorStrategy {
	private DummyBoard dummyBoard;
	private ColorBoard colorBoard;
	private BoardState boardState;
	private IsKingInCheck isKingInCheck;
	private IsPositionCaptured positionCaptured;
	
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
	
	public void settupMoveGenerators(){
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

	public ReyAbstractMoveGenerator getReyMoveGenerator(Color color) {
		return Color.BLANCO.equals(color) ? this.rbmg : this.rnmg;
	}

	private void settupMoveGenerator(MoveGenerator moveGenerator) {
		moveGenerator.setTablero(dummyBoard);
		
		if (moveGenerator instanceof PeonAbstractMoveGenerator) {
			PeonAbstractMoveGenerator generator = (PeonAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			
		} else if (moveGenerator instanceof ReyAbstractMoveGenerator) {
			ReyAbstractMoveGenerator generator = (ReyAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			generator.setPositionCaptured(positionCaptured);
			generator.setKingInCheck(isKingInCheck);
			generator.setBoardCache(colorBoard);
			
		} else if(moveGenerator instanceof CardinalMoveGenerator){
			CardinalMoveGenerator generator = (CardinalMoveGenerator) moveGenerator;
			generator.setBoardCache(colorBoard);
		}
	}

	public DummyBoard getDummyBoard() {
		return dummyBoard;
	}

	public void setDummyBoard(DummyBoard dummyBoard) {
		this.dummyBoard = dummyBoard;
	}

	public ColorBoard getColorBoard() {
		return colorBoard;
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public BoardState getBoardState() {
		return boardState;
	}

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}

	public IsKingInCheck getIsKingInCheck() {
		return isKingInCheck;
	}

	public void setIsKingInCheck(IsKingInCheck isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
	}

	public IsPositionCaptured getPositionCaptured() {
		return positionCaptured;
	}

	public void setPositionCaptured(IsPositionCaptured positionCaptured) {
		this.positionCaptured = positionCaptured;
	}
	
}
