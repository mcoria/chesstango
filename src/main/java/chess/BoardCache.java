package chess;

public class BoardCache {
	
	private Square squareKingBlancoCache = null;
	
	private Square squareKingNegroCache = null;
	
	public Square getSquareKingBlancoCache() {
		return squareKingBlancoCache;
	}
	
	public void setSquareKingBlancoCache(Square squareKingBlancoCache) {
		this.squareKingBlancoCache = squareKingBlancoCache;
	}
	
	public Square getSquareKingNegroCache() {
		return squareKingNegroCache;
	}
	
	public void setSquareKingNegroCache(Square squareKingNegroCache) {
		this.squareKingNegroCache = squareKingNegroCache;
	}
}
