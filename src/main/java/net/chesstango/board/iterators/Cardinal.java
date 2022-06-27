package net.chesstango.board.iterators;

import java.util.Objects;

import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public enum Cardinal {
	Norte(null,true), Sur(null,false), Este(true,null), Oeste(false,null),
	NorteEste(true,true), SurEste(true, false), SurOeste(false, false), NorteOeste(false, true);
	
	private final Boolean east;
	private final Boolean north;
	
	private final int offSetEast;
	private final int offSetNorth;
	
	Cardinal(Boolean east, Boolean north) {
		this.east = east;
		this.north = north;
		
		this.offSetEast = this.east == null ? 0 : (this.east ? 1 : -1);
		this.offSetNorth = this.north == null ? 0 : (this.north ? 1 : -1);
	}
	
	public Square calcularNextPoint(Square from) {
		return Square.getSquare(from.getFile() + offSetEast, from.getRank() + offSetNorth);
	}
	
	public boolean isInDirection(Square from, Square to) {
		if (Objects.equals(east, getDirection(from.getFile(), to.getFile()))) {
			if (Objects.equals(north, getDirection(from.getRank(), to.getRank()))) {
				if (east == null) { // Puede ser mismo lugar; NORTE o SUR
					if (north == null) {
						throw new RuntimeException("from equals to");
					} else { // NORTE o SUR
						return true;
					}
				} else if (east.equals(true)) { // Puede ser Este, NorteEste; SurEste
					if (north == null) { // Este
						return true;
					} else if (north.equals(true)) { // NorteEste
                        return to.getFile() - from.getFile() == to.getRank() - from.getRank();
					} else { // norte.equals(false) --  SurEste
                        return to.getFile() - from.getFile() == from.getRank() - to.getRank();
					}
				} else { // este.equals(false)  -- Puede ser Oeste,NorteOeste, SurOeste
					if (north == null) { // Oeste
						return true;
					} else if (north.equals(true)) { // NorteOeste
                        return from.getFile() - to.getFile() == to.getRank() - from.getRank();
					} else { // norte.equals(false) SurOeste
                        return from.getFile() - to.getFile() == from.getRank() - to.getRank();
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
		} else {
			return from < to;
		}
	}

	public static Cardinal calculateSquaresDirection(Square from, Square to) {
		for(Cardinal direction: Cardinal.values()){
			if(direction.isInDirection(from, to)){
				return direction;
			}
		}
		return null;
	}
}