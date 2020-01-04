package chess;

public enum Pieza {
	PEON_BLANCO,
	PEON_NEGRO,
	
	TORRE_BLANCO,
	TORRE_NEGRO,
	
	CABALLO_BLANCO,
	CABALLO_NEGRO,
	
	ALFIL_BLANCO,
	ALFIL_NEGRO,
	
	REINA_BLANCO,
	REINA_NEGRO,
	
	REY_BLANCO,
	REY_NEGRO;
	
	public boolean isBlanco(){
		return this.equals(PEON_BLANCO) || 
				this.equals(TORRE_BLANCO) || 
				this.equals(CABALLO_BLANCO) || 
				this.equals(ALFIL_BLANCO) || 
				this.equals(REINA_BLANCO) || 
				this.equals(REY_BLANCO);
	}
	
	public boolean isNegro(){
		return !isBlanco();
	}
	
	public boolean isPeon(){
		return this.equals(PEON_BLANCO) || this.equals(PEON_NEGRO);
	}
	
	public boolean isTorre(){
		return this.equals(TORRE_BLANCO) || this.equals(TORRE_NEGRO);
	}
	
	public boolean isCaballo(){
		return this.equals(CABALLO_BLANCO) || this.equals(CABALLO_NEGRO);
	}	
	
	public boolean isAlfil(){
		return this.equals(ALFIL_BLANCO) || this.equals(ALFIL_NEGRO);
	}	
	
	public boolean isReina(){
		return this.equals(REINA_BLANCO) || this.equals(REINA_NEGRO);
	}	
	
	public boolean isRey(){
		return this.equals(REY_BLANCO) || this.equals(REY_NEGRO);
	}	
}
