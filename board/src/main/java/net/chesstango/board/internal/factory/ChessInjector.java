package net.chesstango.board.internal.factory;

import net.chesstango.board.GameListener;
import net.chesstango.board.analyzer.Analyzer;
import net.chesstango.board.analyzer.KingSafePositionsAnalyzer;
import net.chesstango.board.analyzer.PinnedAnalyzer;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.internal.GameImp;
import net.chesstango.board.internal.moves.generators.pseudo.MoveGeneratorImp;
import net.chesstango.board.internal.position.ChessPositionDebug;
import net.chesstango.board.internal.position.ChessPositionImp;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class ChessInjector {

    private final ChessFactory chessFactory;

    private SquareBoard squareBoard = null;

    private PositionState positionState = null;

    private BitBoard bitBoard = null;

    private MoveCacheBoard moveCache = null;

    private KingSquare kingCacheBoard = null;

    private ZobristHash zobristHash = null;

    private ChessPositionImp chessPosition = null;

    private MoveGenerator moveGenerator = null;

    private MoveGeneratorImp moveGeneratorImp = null;

    private PositionAnalyzer positionAnalyzer = null;

    private KingSafePositionsAnalyzer kingSafePositionsAnalyzer = null;

    private PinnedAnalyzer pinnedAnalyzer = null;

    private LegalMoveGenerator defaultMoveCalculator = null;

    private LegalMoveGenerator noCheckLegalMoveGenerator = null;

    private LegalMoveGenerator legalMoveGenerator = null;

    private LegalMoveFilter checkLegalMoveFilter;

    private LegalMoveFilter noCheckLegalMoveFilter;

    private GameState gameState = null;

    private GameImp game = null;

    private MoveFactory moveFactoryBlack;

    private MoveFactory moveFactoryWhite;

    public ChessInjector() {
        this.chessFactory = new ChessFactory();
    }

    public ChessInjector(ChessFactory chessFactory) {
        this.chessFactory = chessFactory;
    }

    public ChessPositionImp getChessPosition() {
        if (chessPosition == null) {
            chessPosition = chessFactory.createChessPosition();

            chessPosition.setSquareBoard(getSquareBoard());

            chessPosition.setPositionState(getPositionState());

            chessPosition.setKingSquare(getKingCacheBoard());

            chessPosition.setBitBoard(getBitBoard());

            chessPosition.setMoveCache(getMoveCacheBoard());

            chessPosition.setZobristHash(getZobristHash());
        }
        return chessPosition;
    }

    public SquareBoard getSquareBoard() {
        if (squareBoard == null) {
            squareBoard = chessFactory.createSquareBoard();
        }
        return squareBoard;
    }

    public PositionState getPositionState() {
        if (positionState == null) {
            positionState = chessFactory.createPositionState();
        }
        return positionState;
    }

    public KingSquare getKingCacheBoard() {
        if (kingCacheBoard == null) {
            kingCacheBoard = chessFactory.createKingCacheBoard();
        }
        return kingCacheBoard;
    }

    public BitBoard getBitBoard() {
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

    public GameImp getGame() {
        if (game == null) {
            game = chessFactory.createGame(getChessPosition(), getGameState(), visitor -> {
                visitor.visit(chessPosition);
                visitor.visit(gameState);
                visitor.visit(moveGenerator);
            });

            // Validation should be executed before the position is analyzed
            if (chessFactory instanceof ChessFactoryDebug) {
                ChessPositionDebug chessPositionDebug = (ChessPositionDebug) getChessPosition();
                game.addGameListener(new GameListener() {
                                         @Override
                                         public void notifyDoMove(Move move) {
                                             chessPositionDebug.validar();
                                         }

                                         @Override
                                         public void notifyUndoMove(Move move) {
                                             chessPositionDebug.validar();
                                         }
                                     }
                );
            }

            game.setAnalyzer(getAnalyzer());
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
            this.legalMoveGenerator = chessFactory.createLegalMoveGenerator(getCheckLegalMoveGenerator(), getNoCheckLegalMoveGenerator());
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
            moveGeneratorImp.setSquareBoardReader(getSquareBoard());
            moveGeneratorImp.setBoardState(getPositionState());
            moveGeneratorImp.setBitBoardReader(getBitBoard());
            moveGeneratorImp.setKingSquareReader(getKingCacheBoard());
            moveGeneratorImp.setMoveFactoryWhite(getMoveFactoryWhite());
            moveGeneratorImp.setMoveFactoryBlack(getMoveFactoryBlack());
        }
        return moveGeneratorImp;
    }

    public MoveFactory getMoveFactoryBlack() {
        if (moveFactoryBlack == null) {
            moveFactoryBlack = chessFactory.createMoveFactoryBlack(getGame());
        }
        return moveFactoryBlack;
    }

    public MoveFactory getMoveFactoryWhite() {
        if (moveFactoryWhite == null) {
            moveFactoryWhite = chessFactory.createMoveFactoryWhite(getGame());
        }
        return moveFactoryWhite;
    }

    private LegalMoveFilter getCheckMoveFilter() {
        if (checkLegalMoveFilter == null) {
            checkLegalMoveFilter = chessFactory.createCheckMoveFilter(getSquareBoard(), getKingCacheBoard(), getBitBoard(), getPositionState());
        }
        return checkLegalMoveFilter;
    }

    private LegalMoveFilter getNoCheckMoveFilter() {
        if (noCheckLegalMoveFilter == null) {
            noCheckLegalMoveFilter = chessFactory.createNoCheckMoveFilter(getSquareBoard(), getKingCacheBoard(), getBitBoard(), getPositionState());
        }
        return noCheckLegalMoveFilter;
    }

}
