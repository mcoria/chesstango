package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.IsKingInCheck;
import chess.IsPositionCaptured;
import chess.Pieza;
import layers.ColorBoard;
import layers.DummyBoard;

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

	public MoveGeneratorStrategy(DummyBoard dummyBoard, ColorBoard colorBoard, BoardState boardState, IsKingInCheck isKingInCheck, IsPositionCaptured positionCaptured) {
		pbmg =  new PeonBlancoMoveGenerator();
		settupMoveGenerator(pbmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		pnmg = new PeonNegroMoveGenerator();
		settupMoveGenerator(pnmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		tbmg = new TorreMoveGenerator(Color.BLANCO);
		settupMoveGenerator(tbmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		tnmg = new TorreMoveGenerator(Color.NEGRO);
		settupMoveGenerator(tnmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		cbmg = new CaballoMoveGenerator(Color.BLANCO);
		settupMoveGenerator(cbmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		cnmg = new CaballoMoveGenerator(Color.NEGRO);
		settupMoveGenerator(cnmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		abmg = new AlfilMoveGenerator(Color.BLANCO);
		settupMoveGenerator(abmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		anmg = new AlfilMoveGenerator(Color.NEGRO);
		settupMoveGenerator(anmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		rebmg = new ReinaMoveGenerator(Color.BLANCO);
		settupMoveGenerator(rebmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		renmg = new ReinaMoveGenerator(Color.NEGRO);
		settupMoveGenerator(renmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		rbmg = new ReyBlancoMoveGenerator();
		settupMoveGenerator(rbmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
		
		rnmg = new ReyNegroMoveGenerator();
		settupMoveGenerator(rnmg, dummyBoard, colorBoard, boardState, isKingInCheck, positionCaptured);
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

	private void settupMoveGenerator(MoveGenerator moveGenerator, DummyBoard dummyBoard, ColorBoard colorBoard, BoardState boardState, IsKingInCheck isKingInCheck, IsPositionCaptured positionCaptured) {
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
	
}
