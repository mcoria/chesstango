package chess;

public enum Color {
	BLANCO,
	NEGRO;
	
	public Color opositeColor(){
		if(this == BLANCO) 
			return Color.NEGRO;
		else
			return Color.BLANCO;
	}	
}
