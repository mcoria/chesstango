package chess.board;

/**
 * @author Mauricio Coria
 *
 */
public enum Color {
	WHITE,
	BLACK;
	
	public Color opositeColor(){
		if(this == WHITE) 
			return Color.BLACK;
		else
			return Color.WHITE;
	}	
}
