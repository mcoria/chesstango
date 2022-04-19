/**
 * 
 */
package chess.board.factory;

import chess.board.Game;
import chess.board.GameState;
import chess.board.analyzer.Pinned;
import chess.board.analyzer.PositionAnalyzer;
import chess.board.analyzer.capturers.Capturer;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.legalmovesgenerators.imp.LegalMoveGeneratorImp;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ChessPositionImp;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;

/**
 * @author Mauricio Coria
 *
 */
public class ChessInjector {
	
	private final ChessFactory chessFactory;
	
	private PiecePlacement piecePlacement = null;
	
	private PositionState positionState = null;
	
	private ColorBoard colorBoard = null;
	
	private MoveCacheBoard moveCache = null;

	private KingCacheBoard kingCacheBoard = null;	
	
	private ChessPosition chessPosition = null;	
	
	private MoveGenerator moveGenerator = null;
	
	private MoveGeneratorImp moveGeneratorImp = null;

	private PositionAnalyzer positionAnalyzer = null;
	
	private Pinned pinnedAlanyzer;

	private LegalMoveGenerator defaultMoveCalculator = null;

	private LegalMoveGenerator noCheckLegalMoveGenerator = null;
	
	private LegalMoveGeneratorImp legalMoveGenerator = null;

	private Capturer capturer = null;

	private MoveFilter checkMoveFilter;
	
	private MoveFilter noCheckMoveFilter;
	
	private GameState gameState = null;	
	
	private Game game = null;	
	
	public ChessInjector() {
		this.chessFactory = new ChessFactory();
	}
	
	public ChessInjector(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}
	
	public ChessPosition getChessPosition() {
		if (chessPosition == null) {
			ChessPositionImp chessPositionImp = chessFactory.createChessPosition();

			chessPositionImp.setPiecePlacement(getPiecePlacement());

			chessPositionImp.setBoardState(getPositionState());

			chessPositionImp.setKingCacheBoard(getKingCacheBoard());

			chessPositionImp.setColorBoard(getColorBoard());

			chessPositionImp.setMoveCache(getMoveCacheBoard());
			
			chessPosition = chessPositionImp;
		}
		return chessPosition;
	}

	public PiecePlacement getPiecePlacement() {
		if(piecePlacement == null){
			piecePlacement = chessFactory.createPiecePlacement();
		}
		return piecePlacement;
	}

	public PositionState getPositionState() {
		if (positionState == null) {
			positionState = chessFactory.createPositionState();
		}
		return positionState;
	}
	
	public KingCacheBoard getKingCacheBoard() {
		if (kingCacheBoard == null) {
			kingCacheBoard = chessFactory.createKingCacheBoard();
		}
		return kingCacheBoard;
	}

	public ColorBoard getColorBoard() {
		if (colorBoard == null) {
			colorBoard = chessFactory.createColorBoard();
		}
		return colorBoard;
	}
	
	public MoveCacheBoard getMoveCacheBoard() {
		if (moveCache == null) {
			moveCache = chessFactory.createMoveCacheBoard();
		}
		return moveCache;
	}
	
	public Game getGame() {
		if (game == null) {
			game = chessFactory.createGame(getChessPosition(), getAnalyzer(), getGameState());
			game.init();
		}
		return game;
	}
	

	public GameState getGameState() {
		if (gameState == null) {
			gameState = chessFactory.createGameState();
		}
		return gameState ;
	}

	public PositionAnalyzer getAnalyzer() {
		if (positionAnalyzer == null) {
			positionAnalyzer = chessFactory.createPositionAnalyzer();
			positionAnalyzer.setPositionReader(getChessPosition());
			positionAnalyzer.setCapturer(getCapturer());
			positionAnalyzer.setLegalMoveGenerator(getLegalMoveGenerator());		
			positionAnalyzer.setGameState(getGameState());
			positionAnalyzer.setPinnedAlanyzer(getPinnedAlanyzer());
		}
		return positionAnalyzer;
	}


	private Pinned getPinnedAlanyzer() {
		if(pinnedAlanyzer == null){
			pinnedAlanyzer = chessFactory.createPinnedAlanyzer(getChessPosition());
		}
		return pinnedAlanyzer;
	}

	public Capturer getCapturer() {
		if(capturer == null){
			capturer = chessFactory.createCapturer(getPiecePlacement());
		}
		return capturer;
	}

	
	private LegalMoveGenerator getLegalMoveGenerator() {
		if (this.legalMoveGenerator == null) {
			this.legalMoveGenerator = chessFactory.createLegalMoveGenerator();
			this.legalMoveGenerator.setDefaultMoveCalculator(getDefaultMoveCalculator());
			this.legalMoveGenerator.setNoCheckLegalMoveGenerator(getNoCheckLegalMoveGenerator());

		}
		return this.legalMoveGenerator;
	}
	
	public LegalMoveGenerator getDefaultMoveCalculator() {
		if (defaultMoveCalculator == null) {
			defaultMoveCalculator = chessFactory.createDefaultLegalMoveGenerator(getChessPosition(), getMoveGenerator(), getCheckMoveFilter());
		}
		return this.defaultMoveCalculator;
	}

	public LegalMoveGenerator getNoCheckLegalMoveGenerator() {
		if (noCheckLegalMoveGenerator == null) {
			noCheckLegalMoveGenerator = chessFactory.createNoCheckLegalMoveGenerator(getChessPosition(), getMoveGenerator(), getNoCheckMoveFilter());
		}
		return noCheckLegalMoveGenerator;
	}	

	public MoveGenerator getMoveGenerator() {
		if (moveGenerator == null) {		
			moveGenerator =  chessFactory.createMoveGenaratorWithCacheProxy(getMoveGeneratorImp(), getMoveCacheBoard());
		}
		return moveGenerator;
	}
	

	protected MoveGeneratorImp getMoveGeneratorImp() {
		if (moveGeneratorImp == null) {
			moveGeneratorImp = new MoveGeneratorImp();			
			moveGeneratorImp.setPiecePlacement(getPiecePlacement());
			moveGeneratorImp.setBoardState(getPositionState());
			moveGeneratorImp.setColorBoard(getColorBoard());
			moveGeneratorImp.setKingCacheBoard(getKingCacheBoard());
		}
		return moveGeneratorImp;
	}

	public MoveFilter getCheckMoveFilter() {
		if (checkMoveFilter == null) {
			checkMoveFilter = chessFactory.createCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getColorBoard(),  getPositionState());
		}
		return checkMoveFilter;
	}
	
	
	public MoveFilter getNoCheckMoveFilter() {
		if (noCheckMoveFilter == null) {
			noCheckMoveFilter = chessFactory.createNoCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getColorBoard(),  getPositionState());
		}
		return noCheckMoveFilter;
	}	
}
