package chess.pseudomovesgenerators;

import chess.Color;
import chess.Piece;
import chess.moves.MoveFactory;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorStrategy {
	private PiecePlacement dummyBoard;
	private ColorBoard colorBoard;
	private PositionState positionState;
	private MoveFactory moveFactory;
	
	private PawnBlancoMoveGenerator pbmg;
	private PawnNegroMoveGenerator pnmg;
	private RookMoveGenerator tbmg;
	private RookMoveGenerator tnmg;
	private KnightMoveGenerator cbmg;
	private KnightMoveGenerator cnmg;
	private AlfilMoveGenerator abmg;
	private AlfilMoveGenerator anmg;
	private QueenMoveGenerator rebmg;
	private QueenMoveGenerator renmg;
	private KingWhiteMoveGenerator rbmg;
	private KingBlackMoveGenerator rnmg;

	private PawnPasanteMoveGenerator ppmg = null;
	
	public MoveGeneratorStrategy() {
		pbmg =  new PawnBlancoMoveGenerator();
		
		pnmg = new PawnNegroMoveGenerator();
		
		tbmg = new RookMoveGenerator(Color.WHITE);
		
		tnmg = new RookMoveGenerator(Color.BLACK);
		
		cbmg = new KnightMoveGenerator(Color.WHITE);
		
		cnmg = new KnightMoveGenerator(Color.BLACK);
		
		abmg = new AlfilMoveGenerator(Color.WHITE);
		
		anmg = new AlfilMoveGenerator(Color.BLACK);
		
		rebmg = new QueenMoveGenerator(Color.WHITE);
		
		renmg = new QueenMoveGenerator(Color.BLACK);
		
		rbmg = new KingWhiteMoveGenerator();
		
		rnmg = new KingBlackMoveGenerator();
		
		ppmg = new PawnPasanteMoveGenerator();
		
		moveFactory = new MoveFactory();
	}

	public KingAbstractMoveGenerator getKingMoveGenerator(Color color) {
		return Color.WHITE.equals(color) ? this.rbmg : this.rnmg;
	}
	
	public void setDummyBoard(PiecePlacement dummyBoard) {
		this.dummyBoard = dummyBoard;
		settupMoveGenerators();
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
		settupMoveGenerators();
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
		settupMoveGenerators();
	}	

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
		settupMoveGenerators();
	}
	
	public PawnBlancoMoveGenerator getPawnBlancoMoveGenerator() {
		return pbmg;
	}

	public PawnNegroMoveGenerator getPawnNegroMoveGenerator() {
		return pnmg;
	}

	public RookMoveGenerator getRookBlancaMoveGenerator() {
		return tbmg;
	}

	public RookMoveGenerator getRookNegraMoveGenerator() {
		return tnmg;
	}	
	
	public KnightMoveGenerator getKnightBlancoMoveGenerator() {
		return cbmg;
	}
	
	public KnightMoveGenerator getKnightNegroMoveGenerator() {
		return cnmg;
	}	
	
	public AlfilMoveGenerator getAlfilBlancoMoveGenerator() {
		return abmg;
	}
	
	public AlfilMoveGenerator getAlfilNegroMoveGenerator() {
		return anmg;
	}	
	
	public QueenMoveGenerator getQueenBlancaMoveGenerator() {
		return rebmg;
	}
	
	public QueenMoveGenerator getQueenNegraMoveGenerator() {
		return renmg;
	}	
	
	public KingWhiteMoveGenerator getKingWhiteMoveGenerator() {
		return rbmg;
	}
	
	public KingBlackMoveGenerator getKingBlackMoveGenerator() {
		return rnmg;
	}
	
	public PawnPasanteMoveGenerator getPawnPasanteMoveGenerator() {
		return ppmg;
	}
	
	public MoveGenerator getMoveGenerator(Piece piece){
		MoveGenerator value  = null;
		switch (piece) {
		case PAWN_WHITE:
			value = this.pbmg;
			break;
		case PAWN_BLACK:
			value = this.pnmg;
			break;
		case ROOK_WHITE:
			value = this.tbmg;
			break;
		case ROOK_BLACK:
			value = this.tnmg;
			break;
		case KNIGHT_WHITE:
			value = this.cbmg;
			break;
		case KNIGHT_BLACK:
			value = this.cnmg;
			break;
		case BISHOP_WHITE:
			value = this.abmg;
			break;
		case BISHOP_BLACK:
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
			generator.setBoardState(positionState);
		}
	}
	

	private void settupPawnPasanteMoveGenerator() {
		ppmg.setBoardState(positionState);
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
		
		settupPawnPasanteMoveGenerator();
	}	

}
