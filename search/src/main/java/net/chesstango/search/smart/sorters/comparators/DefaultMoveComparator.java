package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.containers.MoveToHashMap;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveComparator implements MoveComparator {

    @Override
    public int compare(Move move1, Move move2) {
        PiecePositioned move1From = move1.getFrom();
        PiecePositioned move1To = move1.getTo();
        Piece move1PiecePromotion = move1 instanceof MovePromotion movePromotion ? movePromotion.getPromotion() : null;
        if (Color.BLACK.equals(move1.getFrom().getPiece().getColor())) {
            move1From = move1From.getMirrorPosition();
            move1To = move1To.getMirrorPosition();
            move1PiecePromotion = move1PiecePromotion == null ? null : move1PiecePromotion.getOpposite();
        }

        PiecePositioned move2From = move2.getFrom();
        PiecePositioned move2To = move2.getTo();
        Piece move2PiecePromotion = move2 instanceof MovePromotion movePromotion ? movePromotion.getPromotion() : null;
        if (Color.BLACK.equals(move2.getFrom().getPiece().getColor())) {
            move2From = move2From.getMirrorPosition();
            move2To = move2To.getMirrorPosition();
            move2PiecePromotion = move2PiecePromotion == null ? null : move2PiecePromotion.getOpposite();
        }

        return compare(move1From, move1To, move1PiecePromotion,
                move2From, move2To, move2PiecePromotion);
    }


    private int compare(final PiecePositioned move1From, final PiecePositioned move1To, final Piece move1PiecePromotion,
                        final PiecePositioned move2From, final PiecePositioned move2To, final Piece move2PiecePromotion) {

        int result = 0;

        // En caso que alguno o ambos de los movimientos sea promocion
        if (move1PiecePromotion != null && move2PiecePromotion != null) {
            result = piecePromotionValue(move1PiecePromotion) - piecePromotionValue(move2PiecePromotion); // Desempate abajo
        } else if (move1PiecePromotion != null) {
            return 1;
        } else if (move2PiecePromotion != null) {
            return -1;
        }

        if (result == 0) {
            // En caso que alguno o ambos de los movimientos sea captura
            boolean isMove1Capture = isCapture(move1From, move1To);
            boolean isMove2Capture = isCapture(move2From, move2To);
            if (isMove1Capture && isMove2Capture) {
                result = pieceCaptureValue(move1To.getPiece()) - pieceCaptureValue(move2To.getPiece());  // Desempate abajo

                /**
                 * Preferimos la captura que provenga de la pieza de menor valor
                 */
                if (result == 0) {
                    result = getMovePieceValue(move2From.getPiece()) - getMovePieceValue(move1From.getPiece());
                }
            } else if (isMove1Capture) {
                return 1;
            } else if (isMove2Capture) {
                return -1;
            }
        }

        // Empate promocion o empate captura o movimiento simple
        if (result == 0) {
            result = getMovePieceValue(move1From.getPiece()) - getMovePieceValue(move2From.getPiece());
        }

        if (result == 0) {
            result = move1From.getSquare().getRank() - move2From.getSquare().getRank();
        }

        if (result == 0) {
            result = move1From.getSquare().getFile() - move2From.getSquare().getFile();
        }


        if (result == 0) {
            result = move1To.getSquare().getRank() - move2To.getSquare().getRank();
        }

        if (result == 0) {
            result = move1To.getSquare().getFile() - move2To.getSquare().getFile();
        }

        return result;
    }


    private static boolean isCapture(PiecePositioned moveFrom, PiecePositioned moveTo) {
        if (moveTo.getPiece() != null) {
            return true;
        }

        // Captura de peon
        if (moveFrom.getPiece().isPawn() && moveFrom.getSquare().getFile() != moveTo.getSquare().getFile()) {
            return true;
        }

        return false;
    }

    private static int pieceCaptureValue(Piece piece) {
        if (piece == null) {
            return 1; // Pawn Pasante
        }
        return switch (piece) {
            case QUEEN_BLACK -> 5;
            case ROOK_BLACK -> 4;
            case BISHOP_BLACK -> 3;
            case KNIGHT_BLACK -> 2;
            case PAWN_BLACK -> 1;
            default -> throw new RuntimeException("Invalid capture piece"); // Solo piezas BLACK, no puede ser KING
        };
    }

    private static int piecePromotionValue(Piece piece) {
        return switch (piece) {
            case QUEEN_WHITE -> 5;
            case ROOK_WHITE -> 4;
            case BISHOP_WHITE -> 3;
            case KNIGHT_WHITE -> 2;
            default -> throw new RuntimeException("Invalid promotion piece");
        };
    }


    private static int getMovePieceValue(Piece piece) {
        return switch (piece) {
            case QUEEN_WHITE -> 5;
            case KNIGHT_WHITE -> 4;
            case BISHOP_WHITE -> 3;
            case ROOK_WHITE -> 2;
            case PAWN_WHITE -> 1;
            case KING_WHITE -> 0;
            default -> throw new RuntimeException("Invalid promotion piece");
        };
    }

    @Override
    public void beforeSort(int currentPly, MoveToHashMap moveToZobrist) {

    }

    @Override
    public void afterSort(MoveToHashMap moveToZobrist) {

    }
}
