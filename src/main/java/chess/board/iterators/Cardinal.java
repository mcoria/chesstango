package chess.board.iterators;

import java.util.Objects;

import chess.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public enum Cardinal {
	Norte(null,true), Sur(null,false), Este(true,null), Oeste(false,null),
	NorteEste(true,true), SurEste(true, false), SurOeste(false, false), NorteOeste(false, true);
	
	private final Boolean este;
	private final Boolean norte;
	
	private final int offSetEste;
	private final int offSetNorte;
	
	Cardinal(Boolean este, Boolean norte) {
		this.este = este;
		this.norte = norte;
		
		this.offSetEste = this.este != null ? (this.este ? 1 : -1)  : 0;
		this.offSetNorte = this.norte != null ? (this.norte ? 1 : -1)  : 0;
	}
	
	public Square calcularNextPoint(Square from) {
		return Square.getSquare(from.getFile() + offSetEste, from.getRank() + offSetNorte);
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
                        return to.getFile() - from.getFile() == to.getRank() - from.getRank();
					} else { // norte.equals(false) --  SurEste
                        return to.getFile() - from.getFile() == from.getRank() - to.getRank();
					}
				} else { // este.equals(false)  -- Puede ser Oeste,NorteOeste, SurOeste
					if (norte == null) { // Oeste
						return true;
					} else if (norte.equals(true)) { // NorteOeste
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
		} else return from < to;
	}
}