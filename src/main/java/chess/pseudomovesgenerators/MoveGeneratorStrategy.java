package chess.pseudomovesgenerators;

import chess.Color;
import chess.Piece;
import chess.moves.MoveFactory;
import chess.moves.imp.MoveFactoryBlack;
import chess.moves.imp.MoveFactoryWhite;
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
	
	private MoveFactory moveFactoryWhite;
	private MoveFactory moveFactoryBlack;
	
	private PawnWhiteMoveGenerator pbmg;
	private PawnBlackMoveGenerator pnmg;
	private RookMoveGenerator tbmg;
	private RookMoveGenerator tnmg;
	private KnightMoveGenerator cbmg;
	private KnightMoveGenerator cnmg;
	private BishopMoveGenerator abmg;
	private BishopMoveGenerator anmg;
	private QueenMoveGenerator rebmg;
	private QueenMoveGenerator renmg;
	private KingWhiteMoveGenerator rbmg;
	private KingBlackMoveGenerator rnmg;

	private PawnPasanteMoveGenerator ppmg = null;
	
	public MoveGeneratorStrategy() {
		pbmg =  new PawnWhiteMoveGenerator();
		
		pnmg = new PawnBlackMoveGenerator();
		
		tbmg = new RookMoveGenerator(Color.WHITE);
		
		tnmg = new RookMoveGenerator(Color.BLACK);
		
		cbmg = new KnightMoveGenerator(Color.WHITE);
		
		cnmg = new KnightMoveGenerator(Color.BLACK);
		
		abmg = new BishopMoveGenerator(Color.WHITE);
		
		anmg = new BishopMoveGenerator(Color.BLACK);
		
		rebmg = new QueenMoveGenerator(Color.WHITE);
		
		renmg = new QueenMoveGenerator(Color.BLACK);
		
		rbmg = new KingWhiteMoveGenerator();
		
		rnmg = new KingBlackMoveGenerator();
		
		ppmg = new PawnPasanteMoveGenerator();
		
		moveFactoryWhite = new MoveFactoryWhite();
		moveFactoryBlack = new MoveFactoryBlack();
	}

	public AbstractKingMoveGenerator getKingMoveGenerator(Color color) {
		return Color.WHITE.equals(color) ? this.rbmg : this.rnmg;
	}
	
	public void setPiecePlacement(PiecePlacement dummyBoard) {
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
	
	public PawnWhiteMoveGenerator getPawnWhiteMoveGenerator() {
		return pbmg;
	}

	public PawnBlackMoveGenerator getPawnBlackMoveGenerator() {
		return pnmg;
	}

	public RookMoveGenerator getRookBlancaMoveGenerator() {
		return tbmg;
	}

	public RookMoveGenerator getRookNegraMoveGenerator() {
		return tnmg;
	}	
	
	public KnightMoveGenerator getKnightWhiteMoveGenerator() {
		return cbmg;
	}
	
	public KnightMoveGenerator getKnightBlackMoveGenerator() {
		return cnmg;
	}	
	
	public BishopMoveGenerator getBishopWhiteMoveGenerator() {
		return abmg;
	}
	
	public BishopMoveGenerator getBishopBlackMoveGenerator() {
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
			
			if(moveGenerator.equals(pbmg) || moveGenerator.equals(tbmg) || moveGenerator.equals(cbmg) || moveGenerator.equals(abmg) || moveGenerator.equals(rebmg) || moveGenerator.equals(rbmg)){
				((AbstractMoveGenerator)moveGenerator).setMoveFactory(moveFactoryWhite);
			} else {
				((AbstractMoveGenerator)moveGenerator).setMoveFactory(moveFactoryBlack);
			}
		}
		
		if (moveGenerator instanceof AbstractKingMoveGenerator) {
			AbstractKingMoveGenerator generator = (AbstractKingMoveGenerator) moveGenerator;
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
