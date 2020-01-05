package chess;

public class DummyBoard {
	//56,57,58,59,60,61,62,63,
	//48,49,50,51,52,53,54,55,
	//40,41,42,43,44,45,46,47,
	//32,33,34,35,36,37,38,39,
	//24,25,26,27,28,29,30,31,
	//16,17,18,19,20,21,22,23,
    //08,09,10,11,12,13,14,15,
    //00,01,02,03,04,05,06,07,	
	private Pieza[][] tablero;
	
	public DummyBoard(Pieza[][] tablero){
		this.tablero = tablero;
	}

	public Pieza[][] getTablero() {
		return tablero;
	}

	public void setTablero(Pieza[][] tablero) {
		this.tablero = tablero;
	}

	public Pieza getPieza(Square square) {
		// TODO Auto-generated method stub
		return tablero[square.getFile()][square.getRank()];
	}

	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
}
