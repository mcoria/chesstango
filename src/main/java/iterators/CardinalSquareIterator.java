package iterators;

import java.util.Objects;

import chess.Square;

public class CardinalSquareIterator implements SquareIterator {
	
	public static enum Cardinal {
		Norte(null,true), Sur(null,false), Este(true,null), Oeste(false,null),
		NorteEste(true,true), SurEste(true, false), SurOeste(false, false), NorteOeste(false, true);

		
		private Boolean este;
		private Boolean norte;
		private Cardinal(Boolean este, Boolean norte) {
			this.este = este;
			this.norte = norte;
		}
		
		public boolean isInDirection(Square from, Square to) {
			if (Objects.equals(este, getDirection(from.getFile(), to.getFile()))) {
				if (Objects.equals(norte, getDirection(from.getRank(), to.getRank()))) {
					if (este == null) { // Puede ser mismo lugar; NORTE o SUR
						if (norte == null) {
							throw new RuntimeException("from equals to");
						} else { // NORTE o SUR
							return true;
						}
					} else if (este.equals(true)) { // Puede ser Este, NorteEste; SurEste
						if (norte == null) { // Este
							return true;
						} else if (norte.equals(true)) { // NorteEste
							if (to.getFile() - from.getFile() == to.getRank() - from.getRank()) {
								return true;
							} else {
								return false;
							}
						} else { // norte.equals(false) --  SurEste
							if (to.getFile() - from.getFile() == from.getRank() - to.getRank()) {
								return true;
							} else {
								return false;
							}
						}
					} else { // este.equals(false)  -- Puede ser Oeste,NorteOeste, SurOeste
						if (norte == null) { // Oeste
							return true;
						} else if (norte.equals(true)) { // NorteOeste
							if (from.getFile() - to.getFile() == to.getRank() - from.getRank()) {
								return true;
							} else {
								return false;
							}
						} else { // norte.equals(false) SurOeste
							if (from.getFile() - to.getFile() == from.getRank() - to.getRank()) {
								return true;
							} else {
								return false;
							}
						}
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		private static Boolean getDirection(int from, int to) {
			if( from == to ) {
				return null;
			} else if ( from < to){
				return true;
			} else {
				return false;
			}
		}
	}
	
	private final Cardinal cardinal;
	private Square nextPoint;
	
	public CardinalSquareIterator(Cardinal cardinal, Square startingPoint) {
		this.cardinal = cardinal;
		calcularNextPoint(startingPoint);
	}

	private void calcularNextPoint(Square from) {
		switch (cardinal) {
		case Norte:
			this.nextPoint =  Square.getSquare(from.getFile(), from.getRank() + 1);
			break;
		case Sur:
			this.nextPoint =  Square.getSquare(from.getFile(), from.getRank() - 1);	
			break;
		case Este:
			this.nextPoint =  Square.getSquare(from.getFile() + 1, from.getRank());	
			break;			
		case Oeste:
			this.nextPoint =  Square.getSquare(from.getFile() - 1, from.getRank());	
			break;
		case NorteEste:
			this.nextPoint =  Square.getSquare(from.getFile() + 1, from.getRank() + 1);
			break;
		case SurEste:
			this.nextPoint =  Square.getSquare(from.getFile() + 1, from.getRank() - 1);
			break;	
		case SurOeste:
			this.nextPoint =  Square.getSquare(from.getFile() - 1, from.getRank() - 1);
			break;	
		case NorteOeste:
			this.nextPoint =  Square.getSquare(from.getFile() - 1, from.getRank() + 1);
			break;			
		default:
			throw new RuntimeException("Cardinal not defined");
		}
	}

	@Override
	public boolean hasNext() {
		return nextPoint !=null;
	}

	@Override
	public Square next() {
		Square currentPoint = nextPoint;
		calcularNextPoint(currentPoint);
		return currentPoint;
	}

}
