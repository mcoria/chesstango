package positioncaptures;

import chess.Color;
import chess.Square;
import layers.DummyBoard;

public class ImprovedCapturer implements Capturer {
	
	private ImprovedCapturerColor capturerBlanco = null;
	private ImprovedCapturerColor capturerNegro = null;
	
	public ImprovedCapturer(DummyBoard dummyBoard) {
		this.capturerBlanco = new ImprovedCapturerColor(Color.BLANCO, dummyBoard);
		this.capturerNegro = new ImprovedCapturerColor(Color.NEGRO, dummyBoard);
	}

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(Color.BLANCO.equals(color)){
			return capturerBlanco.positionCaptured(square);
		} else {
			return capturerNegro.positionCaptured(square);
		}
	}


}
