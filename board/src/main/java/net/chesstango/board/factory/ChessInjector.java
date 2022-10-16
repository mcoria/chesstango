/**
 * 
 */
package net.chesstango.board.factory;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.CheckAndPinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.ChessPositionImp;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;

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
	
	private CheckAndPinnedAnalyzer checkAndPinnedAnalyzer;

	private LegalMoveGenerator defaultMoveCalculator = null;

	private LegalMoveGenerator noCheckLegalMoveGenerator = null;
	
	private LegalMoveGeneratorImp legalMoveGenerator = null;

	private FullScanSquareCapturer fullScanSquareCapturer = null;

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
			game = chessFactory.createGame(getChessPosition(), getMoveGenerator(), getAnalyzer(), getGameState());
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
			positionAnalyzer.setLegalMoveGenerator(getLegalMoveGenerator());		
			positionAnalyzer.setGameState(getGameState());
			positionAnalyzer.setCheckAndPinnedAnalyzer(getCheckAndPinnedAnalyzer());
		}
		return positionAnalyzer;
	}

	private CheckAndPinnedAnalyzer getCheckAndPinnedAnalyzer() {
		if(checkAndPinnedAnalyzer==null){
			checkAndPinnedAnalyzer = chessFactory.createCheckAndPinnedAnalyzer(getChessPosition());
		}
		return checkAndPinnedAnalyzer;
	}

	public FullScanSquareCapturer getCapturer() {
		if(fullScanSquareCapturer == null){
			fullScanSquareCapturer = chessFactory.createCapturer(getPiecePlacement());
		}
		return fullScanSquareCapturer;
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
