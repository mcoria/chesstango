package net.chesstango.board.factory;

import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.GameVisitor;
import net.chesstango.board.GameVisitorAcceptor;
import net.chesstango.board.analyzer.Analyzer;
import net.chesstango.board.analyzer.KingSafePositionsAnalyzer;
import net.chesstango.board.analyzer.PinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.movesgenerators.legal.LegalMoveFilter;
import net.chesstango.board.movesgenerators.legal.imp.LegalMoveGeneratorImp;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorImp;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.ChessPositionImp;
import net.chesstango.board.position.imp.KingSquareImp;
import net.chesstango.board.position.imp.MoveCacheBoardImp;

/**
 * @author Mauricio Coria
 */
public class ChessInjector {

    private final ChessFactory chessFactory;

    private SquareBoard squareBoard = null;

    private PositionState positionState = null;

    private BitBoard bitBoard = null;

    private MoveCacheBoardImp moveCache = null;

    private KingSquareImp kingCacheBoard = null;

    private ZobristHash zobristHash = null;

    private ChessPosition chessPosition = null;

    private MoveGenerator moveGenerator = null;

    private MoveGeneratorImp moveGeneratorImp = null;

    private PositionAnalyzer positionAnalyzer = null;

    private KingSafePositionsAnalyzer kingSafePositionsAnalyzer = null;

    private PinnedAnalyzer pinnedAnalyzer = null;

    private LegalMoveGenerator defaultMoveCalculator = null;

    private LegalMoveGenerator noCheckLegalMoveGenerator = null;

    private LegalMoveGeneratorImp legalMoveGenerator = null;

    private LegalMoveFilter checkLegalMoveFilter;

    private LegalMoveFilter noCheckLegalMoveFilter;

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

            chessPositionImp.setSquareBoard(getPiecePlacement());

            chessPositionImp.setPositionState(getPositionState());

            chessPositionImp.setKingSquare(getKingCacheBoard());

            chessPositionImp.setBitBoard(getBitBoard());

            chessPositionImp.setMoveCache(getMoveCacheBoard());

            chessPositionImp.setZobristHash(getZobristHash());

            chessPosition = chessPositionImp;
        }
        return chessPosition;
    }

    public SquareBoard getPiecePlacement() {
        if (squareBoard == null) {
            squareBoard = chessFactory.createPiecePlacement();
        }
        return squareBoard;
    }

    public PositionState getPositionState() {
        if (positionState == null) {
            positionState = chessFactory.createPositionState();
        }
        return positionState;
    }

    protected KingSquareImp getKingCacheBoard() {
        if (kingCacheBoard == null) {
            kingCacheBoard = chessFactory.createKingCacheBoard();
        }
        return kingCacheBoard;
    }

    protected BitBoard getBitBoard() {
        if (bitBoard == null) {
            bitBoard = chessFactory.createColorBoard();
        }
        return bitBoard;
    }

    protected MoveCacheBoard getMoveCacheBoard() {
        if (moveCache == null) {
            moveCache = chessFactory.createMoveCacheBoard();
        }
        return moveCache;
    }

    protected ZobristHash getZobristHash() {
        if (zobristHash == null) {
            zobristHash = chessFactory.createZobristHash();
        }
        return zobristHash;
    }

    public Game getGame() {
        if (game == null) {
            game = chessFactory.createGame(getChessPosition(), getGameState(), getAnalyzer(), new GameVisitorAcceptor() {
                @Override
                public void accept(GameVisitor visitor) {
                    visitor.visit(getGameState());
                    visitor.visit(getPseudoMoveGenerator());
                    visitor.visit(getChessPosition());
                }
            });
        }
        return game;
    }


    public GameState getGameState() {
        if (gameState == null) {
            gameState = chessFactory.createGameState();
        }
        return gameState;
    }

    public PositionAnalyzer getAnalyzer() {
        if (positionAnalyzer == null) {
            positionAnalyzer = chessFactory.createPositionAnalyzer();
            positionAnalyzer.setLegalMoveGenerator(getLegalMoveGenerator());
            positionAnalyzer.setGameState(getGameState());
            positionAnalyzer.setPositionReader(getChessPosition());
            positionAnalyzer.setPinnedAnalyzer(getPinnedAnalyzer());
            positionAnalyzer.setKingSafePositionsAnalyzer(getKingSafePositionsAnalyzer());
        }
        return positionAnalyzer;
    }

    private Analyzer getKingSafePositionsAnalyzer() {
        if (kingSafePositionsAnalyzer == null) {
            kingSafePositionsAnalyzer = chessFactory.createKingSafePositionsAnalyzer(getChessPosition());
        }
        return kingSafePositionsAnalyzer;
    }

    private PinnedAnalyzer getPinnedAnalyzer() {
        if (pinnedAnalyzer == null) {
            pinnedAnalyzer = chessFactory.createPinnedAnalyzer(getChessPosition());
        }
        return pinnedAnalyzer;
    }

    private LegalMoveGenerator getLegalMoveGenerator() {
        if (this.legalMoveGenerator == null) {
            this.legalMoveGenerator = chessFactory.createLegalMoveGenerator();
            this.legalMoveGenerator.setCheckLegalMoveGenerator(getCheckLegalMoveGenerator());
            this.legalMoveGenerator.setNoCheckLegalMoveGenerator(getNoCheckLegalMoveGenerator());

        }
        return this.legalMoveGenerator;
    }

    public MoveGenerator getPseudoMoveGenerator() {
        if (moveGenerator == null) {
            moveGenerator = chessFactory.createMoveGeneratorWithCacheProxy(getMoveGeneratorImp(), getMoveCacheBoard());
        }
        return moveGenerator;
    }

    protected LegalMoveGenerator getCheckLegalMoveGenerator() {
        if (defaultMoveCalculator == null) {
            defaultMoveCalculator = chessFactory.createCheckLegalMoveGenerator(getChessPosition(), getPseudoMoveGenerator(), getCheckMoveFilter());
        }
        return this.defaultMoveCalculator;
    }

    protected LegalMoveGenerator getNoCheckLegalMoveGenerator() {
        if (noCheckLegalMoveGenerator == null) {
            noCheckLegalMoveGenerator = chessFactory.createNoCheckLegalMoveGenerator(getChessPosition(), getPseudoMoveGenerator(), getNoCheckMoveFilter());
        }
        return noCheckLegalMoveGenerator;
    }

    protected MoveGenerator getMoveGeneratorImp() {
        if (moveGeneratorImp == null) {
            moveGeneratorImp = chessFactory.createMoveGenerator();
            moveGeneratorImp.setPiecePlacement(getPiecePlacement());
            moveGeneratorImp.setBoardState(getPositionState());
            moveGeneratorImp.setColorBoard(getBitBoard());
            moveGeneratorImp.setKingSquare(getKingCacheBoard());
        }
        return moveGeneratorImp;
    }

    private LegalMoveFilter getCheckMoveFilter() {
        if (checkLegalMoveFilter == null) {
            checkLegalMoveFilter = chessFactory.createCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getBitBoard(), getPositionState());
        }
        return checkLegalMoveFilter;
    }


    private LegalMoveFilter getNoCheckMoveFilter() {
        if (noCheckLegalMoveFilter == null) {
            noCheckLegalMoveFilter = chessFactory.createNoCheckMoveFilter(getPiecePlacement(), getKingCacheBoard(), getBitBoard(), getPositionState());
        }
        return noCheckLegalMoveFilter;
    }

}
