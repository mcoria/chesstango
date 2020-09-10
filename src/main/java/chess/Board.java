package chess;

import java.util.Iterator;

import layers.ColorBoard;
import layers.DummyBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movegenerators.CardinalMoveGenerator;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.PeonAbstractMoveGenerator;
import movegenerators.ReyAbstractMoveGenerator;


public class Board {

	// Al final del dia estas son dos representaciones distintas del tablero. Uno con mas informacion que el otro.
	//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. Me imagino un tablero con X y O para representar los distintos colores.
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	
	// Esta es una capa mas de informacion del tablero
	private MoveCache moveCache = null;
	
	private BoardState boardState = null;
	
	private MoveGeneratorStrategy strategy = null;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	
	private BoardAnalyzer analyzer = null; 

	public Board(DummyBoard dummyBoard, BoardState boardState) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.colorBoard = new ColorBoard(dummyBoard);
		this.strategy = new MoveGeneratorStrategy(this);
		this.moveCache = new MoveCache();
		
		this.analyzer = new BoardAnalyzer(this, dummyBoard, boardState, colorBoard, strategy);
		this.defaultMoveCalculator = new DefaultLegalMoveCalculator(this, dummyBoard, boardState, colorBoard, strategy, (Square square) -> isPositionCaptured(square));
	}
	
	public BoardResult getBoardResult() {
		analyzer.analyze();
		
		LegalMoveCalculator calculator = selectCalculator(analyzer);
		
		BoardResult result = new BoardResult();
		result.setKingInCheck(analyzer.isKingInCheck());
		result.setLegalMoves(calculator.getLegalMoves(analyzer));

		return result;
	}
	
	private LegalMoveCalculator selectCalculator(BoardAnalyzer analyzer) {
		return defaultMoveCalculator;
	}

	protected boolean isPositionCaptured(Square square){
		return positionCaptured(boardState.getTurnoActual().opositeColor(), square) != null;
	}	

	//TODO: Esto hay que reimplementarlo, observar buscar los peones, caballos; alfiles; torres y reinas que podrian capturar la posicion
	// EN VEZ DE RECORRER TODO EL TABLERO EN BUSCA DE LA PIESA
	/*
	 * Observar que este método itera las posiciones en base a boardCache.
	 * Luego obtiene la posicion de dummyBoard.
	 * Esto implica que boardCache necesita estar actualizado en todo momento. 
	 */
	private PosicionPieza positionCaptured(Color color, Square square){
		for (Iterator<PosicionPieza> iterator = dummyBoard.iterator(colorBoard.getPosiciones(color)); iterator.hasNext();) {
			PosicionPieza origen = iterator.next();
			Pieza currentPieza = origen.getValue();
			//if(currentPieza != null){
			MoveGenerator moveGenerator = this.strategy.getMoveGenerator(currentPieza);
			if(moveGenerator.puedeCapturarPosicion(origen, square)){
				return origen;
			}
			//} else {
			//	throw new RuntimeException("El cache quedó desactualizado");
			//}
		}
		return null;
	}	

	///////////////////////////// START Move execution Logic /////////////////////////////		
	public void execute(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.executeMove(dummyBoard);

		move.executeMove(colorBoard);

		move.executeMove(moveCache);

		move.executeMove(boardState);

		// boardCache.validarCacheSqueare(dummyBoard);
	}

	public void undo(Move move) {
		// boardCache.validarCacheSqueare(dummyBoard);

		move.undoMove(boardState);

		move.undoMove(moveCache);

		move.undoMove(colorBoard);

		move.undoMove(dummyBoard);

		// boardCache.validarCacheSqueare(dummyBoard);
	}
	///////////////////////////// END Move execution Logic /////////////////////////////
	
	
	public void settupMoveGenerator(MoveGenerator moveGenerator) {
		moveGenerator.setTablero(this.dummyBoard);
		
		if (moveGenerator instanceof PeonAbstractMoveGenerator) {
			PeonAbstractMoveGenerator generator = (PeonAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			
		} else if (moveGenerator instanceof ReyAbstractMoveGenerator) {
			ReyAbstractMoveGenerator generator = (ReyAbstractMoveGenerator) moveGenerator;
			generator.setBoardState(boardState);
			generator.setPositionCaptured((Square square) -> isPositionCaptured(square));
			generator.setKingInCheck(() -> this.analyzer.isKingInCheck());
			generator.setBoardCache(this.colorBoard);
			
		} else if(moveGenerator instanceof CardinalMoveGenerator){
			CardinalMoveGenerator generator = (CardinalMoveGenerator) moveGenerator;
			generator.setBoardCache(this.colorBoard);
		}
	}
	
	public Square getKingSquare() {
		return Color.BLANCO.equals(boardState.getTurnoActual()) ? colorBoard.getSquareKingBlancoCache() : colorBoard.getSquareKingNegroCache();
	}	
	
	@Override
	public String toString() {
	    return this.dummyBoard.toString();
	}

	public DummyBoard getDummyBoard() {
		return this.dummyBoard;
	}
	
	public BoardState getBoardState() {
		return boardState;
	}	
}
