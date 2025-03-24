package net.chesstango.search.smart.features.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.*;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCaptureEnPassant;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.*;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsWrapper implements Game {
    private GameImp imp;

    @Setter
    @Getter
    private int executedMoves;

    public GameStatisticsWrapper(GameImp imp) {
        this.imp = imp;
    }

    @Override
    public FEN getInitialFEN() {
        return imp.getInitialFEN();
    }

    @Override
    public FEN getCurrentFEN() {
        FENEncoder encoder = new FENEncoder();
        getChessPosition().constructChessPositionRepresentation(encoder);
        return encoder.getChessRepresentation();
    }

    @Override
    public void threefoldRepetitionRule(boolean flag) {
        imp.threefoldRepetitionRule(flag);
    }

    @Override
    public void fiftyMovesRule(boolean flag) {
        imp.fiftyMovesRule(flag);
    }

    @Override
    public Game undoMove() {
        imp.undoMove();
        return this;
    }


    @Override
    public void accept(GameVisitor visitor) {
        imp.accept(visitor);
    }

    @Override
    public GameStateReader getState() {
        return imp.getState();
    }

    @Override
    public ChessPositionReader getChessPosition() {
        return imp.getChessPosition();
    }

    @Override
    public GameStatus getStatus() {
        return imp.getStatus();
    }

    @Override
    public MoveContainerReader<Move> getPossibleMoves() {
        MoveContainer<Move> moveContainer = new MoveContainer<>();
        for (Move move : imp.getPossibleMoves()) {
            moveContainer.add(wrappMove(move));
        }
        return moveContainer;
    }

    @Override
    public Move getMove(Square from, Square to) {
        return wrappMove(imp.getMove(from, to));
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        return wrappMove(imp.getMove(from, to, promotionPiece));
    }

    @Override
    public Game executeMove(Square from, Square to) {
        executedMoves++;
        imp.executeMove(from, to);
        return this;
    }

    @Override
    public Game executeMove(Square from, Square to, Piece promotionPiece) {
        executedMoves++;
        imp.executeMove(from, to, promotionPiece);
        return this;
    }

    @Override
    public Game mirror() {
        return new GameStatisticsWrapper((GameImp) imp.mirror());
    }

    private Move wrappMove(Move moveImp) {
        if (moveImp instanceof MoveCaptureEnPassant moveCaptureEnPassant) {
            return new MoveCaptureEnPassant() {
                @Override
                public PiecePositioned getCapture() {
                    return moveCaptureEnPassant.getCapture();
                }

                @Override
                public PiecePositioned getFrom() {
                    return moveImp.getFrom();
                }

                @Override
                public PiecePositioned getTo() {
                    return moveImp.getTo();
                }

                @Override
                public void executeMove() {
                    executedMoves++;
                    moveImp.executeMove();
                }

                @Override
                public void undoMove() {
                    moveImp.undoMove();
                }

                @Override
                public Cardinal getMoveDirection() {
                    return moveImp.getMoveDirection();
                }

                @Override
                public boolean isQuiet() {
                    return moveImp.isQuiet();
                }

                @Override
                public long getZobristHash(ChessPosition chessPosition) {
                    return moveImp.getZobristHash(chessPosition);
                }

                @Override
                public String toString() {
                    return moveImp.toString();
                }

                @Override
                public boolean equals(Object obj) {
                    return moveImp.equals(obj);

                }
            };
        } else if (moveImp instanceof MovePromotion movePromotion) {
            return new MovePromotion() {
                @Override
                public Piece getPromotion() {
                    return movePromotion.getPromotion();
                }

                @Override
                public PiecePositioned getFrom() {
                    return moveImp.getFrom();
                }

                @Override
                public PiecePositioned getTo() {
                    return moveImp.getTo();
                }

                @Override
                public void executeMove() {
                    executedMoves++;
                    moveImp.executeMove();
                }

                @Override
                public void undoMove() {
                    moveImp.undoMove();
                }

                @Override
                public Cardinal getMoveDirection() {
                    return moveImp.getMoveDirection();
                }

                @Override
                public boolean isQuiet() {
                    return moveImp.isQuiet();
                }

                @Override
                public long getZobristHash(ChessPosition chessPosition) {
                    return moveImp.getZobristHash(chessPosition);
                }

                @Override
                public String toString() {
                    return moveImp.toString();
                }

                @Override
                public boolean equals(Object obj) {
                    return moveImp.equals(obj);
                }
            };
        } else if (moveImp instanceof MoveCastling moveCastling) {
            return new MoveCastling() {


                @Override
                public PiecePositioned getRookFrom() {
                    return moveCastling.getRookFrom();
                }

                @Override
                public PiecePositioned getRookTo() {
                    return moveCastling.getRookTo();
                }

                @Override
                public PiecePositioned getFrom() {
                    return moveImp.getFrom();
                }

                @Override
                public PiecePositioned getTo() {
                    return moveImp.getTo();
                }

                @Override
                public void executeMove() {
                    executedMoves++;
                    moveImp.executeMove();
                }

                @Override
                public void undoMove() {
                    moveImp.undoMove();
                }

                @Override
                public Cardinal getMoveDirection() {
                    return moveImp.getMoveDirection();
                }

                @Override
                public boolean isQuiet() {
                    return moveImp.isQuiet();
                }

                @Override
                public long getZobristHash(ChessPosition chessPosition) {
                    return moveImp.getZobristHash(chessPosition);
                }

                @Override
                public String toString() {
                    return moveImp.toString();
                }

                @Override
                public boolean equals(Object obj) {
                    return moveImp.equals(obj);
                }
            };
        }

        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return moveImp.getFrom();
            }

            @Override
            public PiecePositioned getTo() {
                return moveImp.getTo();
            }

            @Override
            public void executeMove() {
                executedMoves++;
                moveImp.executeMove();
            }

            @Override
            public void undoMove() {
                moveImp.undoMove();
            }

            @Override
            public Cardinal getMoveDirection() {
                return moveImp.getMoveDirection();
            }

            @Override
            public boolean isQuiet() {
                return moveImp.isQuiet();
            }

            @Override
            public long getZobristHash(ChessPosition chessPosition) {
                return moveImp.getZobristHash(chessPosition);
            }

            @Override
            public String toString() {
                return moveImp.toString();
            }

            @Override
            public boolean equals(Object obj) {
                return moveImp.equals(obj);
            }
        };
    }
}
