package chess;

import java.util.function.Function;

import chess.pseudomovesgenerators.MoveGeneratorByPiecePositioned;
import chess.pseudomovesgenerators.imp.MoveGeneratorImp;

/**
 * @author Mauricio Coria
 *
 */
public enum Piece {
	PAWN_WHITE(Color.WHITE, generator -> generator.getPawnWhiteMoveGenerator()),
	PAWN_BLACK(Color.BLACK,  generator -> generator.getPawnBlackMoveGenerator()),
	
	ROOK_WHITE(Color.WHITE, generator -> generator.getRookBlancaMoveGenerator()),
	ROOK_BLACK(Color.BLACK, generator -> generator.getRookNegraMoveGenerator()),
	
	KNIGHT_WHITE(Color.WHITE, generator -> generator.getKnightWhiteMoveGenerator()),
	KNIGHT_BLACK(Color.BLACK, generator -> generator.getKnightBlackMoveGenerator()),
	
	BISHOP_WHITE(Color.WHITE, generator -> generator.getBishopWhiteMoveGenerator()),
	BISHOP_BLACK(Color.BLACK, generator -> generator.getBishopBlackMoveGenerator()),
	
	QUEEN_WHITE(Color.WHITE, generator -> generator.getQueenBlancaMoveGenerator()),
	QUEEN_BLACK(Color.BLACK, generator -> generator.getQueenNegraMoveGenerator()),
	
	KING_WHITE(Color.WHITE, generator -> generator.getKingWhiteMoveGenerator()),
	KING_BLACK(Color.BLACK, generator -> generator.getKingBlackMoveGenerator());
	
	private final Color color;
	private final Function<MoveGeneratorImp, MoveGeneratorByPiecePositioned> selector;
	
	private Piece(Color color, Function<MoveGeneratorImp, MoveGeneratorByPiecePositioned> selector) {
		this.color = color;
		this.selector = selector;
	}

	public Color getColor() {
		return color;
	}

	public MoveGeneratorByPiecePositioned selectMoveGeneratorStrategy(MoveGeneratorImp strategy) {
		return selector.apply(strategy);
	}	
	
	public static Piece getKing(Color color){
		switch (color) {
		case  WHITE:
			return KING_WHITE;
		case  BLACK:
			return KING_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Piece getQueen(Color color){
		switch (color) {
		case  WHITE:
			return QUEEN_WHITE;
		case  BLACK:
			return QUEEN_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Piece getBishop(Color color){
		switch (color) {
		case  WHITE:
			return BISHOP_WHITE;
		case  BLACK:
			return BISHOP_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Piece getRook(Color color){
		switch (color) {
		case  WHITE:
			return ROOK_WHITE;
		case  BLACK:
			return ROOK_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Piece getKnight(Color color) {
		switch (color) {
		case  WHITE:
			return KNIGHT_WHITE;
		case  BLACK:
			return KNIGHT_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Piece getPawn(Color color) {
		switch (color) {
		case  WHITE:
			return PAWN_WHITE;
		case  BLACK:
			return PAWN_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}	

}
