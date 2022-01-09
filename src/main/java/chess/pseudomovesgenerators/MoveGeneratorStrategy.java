package chess.pseudomovesgenerators;

import chess.BoardState;
import chess.Color;
import chess.Piece;
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
	
	private PawnBlancoMoveGenerator pbmg;
	private PawnNegroMoveGenerator pnmg;
	private TorreMoveGenerator tbmg;
	private TorreMoveGenerator tnmg;
	private CaballoMoveGenerator cbmg;
	private CaballoMoveGenerator cnmg;
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
		
		tbmg = new TorreMoveGenerator(Color.WHITE);
		
		tnmg = new TorreMoveGenerator(Color.BLACK);
		
		cbmg = new CaballoMoveGenerator(Color.WHITE);
		
		cnmg = new CaballoMoveGenerator(Color.BLACK);
		
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
	
	public PawnBlancoMoveGenerator getPawnBlancoMoveGenerator() {
		return pbmg;
	}

	public PawnNegroMoveGenerator getPawnNegroMoveGenerator() {
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
			generator.setBoardState(boardState);
		}
	}
	

	private void settupPawnPasanteMoveGenerator() {
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
		
		settupPawnPasanteMoveGenerator();
	}	

}
