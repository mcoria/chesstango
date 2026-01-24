package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;

import java.nio.file.Files;
import java.nio.file.Path;

import static net.chesstango.piazzolla.syzygy.Syzygy.*;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByTablebase implements SearchByChain {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final SyzygyPosition syzygyPosition;

    @Setter
    private SearchByChain next;

    @Getter
    private final Syzygy syzygy;

    SearchByTablebase(Syzygy syzygy) {
        this.syzygy = syzygy;
        this.syzygyPosition = new SyzygyPosition();
    }

    static SearchByTablebase open(String syzygyPath) {
        return new SearchByTablebase(Syzygy.open(syzygyPath));
    }

    @Override
    public void reset() {
        next.reset();
    }

    @Override
    public void stopSearching() {
        next.stopSearching();
    }


    @Override
    public void close() throws Exception {
        /*
        try {
            syzygy.close();
            System.err.println("Error closing opening book");
        } catch (IOException e) {
            System.err.println("Error closing opening book");
            e.printStackTrace(System.err);
        }
         */
        next.close();
    }

    @Override
    public SearchResponse search(SearchContext context) {
        SearchResponse searchResponse = null;
        if (syzygy != null) {
            int syzygyResult = searchSyzygyTableBases(context.getGame());
            if (syzygyResult != TB_RESULT_FAILED) {
                searchResponse = createSearchResponse(context.getGame(), syzygyResult);
            }
        }
        return searchResponse == null ? next.search(context) : searchResponse;
    }

    int searchSyzygyTableBases(Game game) {
        int result = TB_RESULT_FAILED;
        final int tbLargest = syzygy.tb_largest();
        if (tbLargest >= 3 && tbLargest >= Long.bitCount(game.getPosition().getAllPositions())) {

            SyzygyPosition syzygyPosition = bindSyzygyPosition(game);

            int[] results = new int[TB_MAX_MOVES];

            result = syzygy.tb_probe_root(syzygyPosition, results);

            if (result == TB_RESULT_FAILED) {
                log.warn("Syzygy lookup failed: {}", game.getCurrentFEN());
            }
        }
        return result;
    }

    private SearchResponse createSearchResponse(Game game, int syzygyResult) {
        SearchResponse searchResponse = null;

        final int fromIdx = Syzygy.TB_GET_FROM(syzygyResult);
        final int toIdx = Syzygy.TB_GET_TO(syzygyResult);
        final int promotion = Syzygy.TB_GET_PROMOTES(syzygyResult);

        Square from = Square.squareByIdx(fromIdx);
        Square to = Square.squareByIdx(toIdx);
        Piece promotionPiece = switch (promotion) {
            case TB_PROMOTES_QUEEN -> syzygyPosition.isTurn() ? Piece.QUEEN_WHITE : Piece.QUEEN_BLACK;
            case TB_PROMOTES_KNIGHT -> syzygyPosition.isTurn() ? Piece.KNIGHT_WHITE : Piece.KNIGHT_BLACK;
            case TB_PROMOTES_ROOK -> syzygyPosition.isTurn() ? Piece.ROOK_WHITE : Piece.ROOK_BLACK;
            case TB_PROMOTES_BISHOP -> syzygyPosition.isTurn() ? Piece.BISHOP_WHITE : Piece.BISHOP_BLACK;
            default -> null;
        };

        Move move = promotionPiece == null ? game.getMove(from, to) : game.getMove(from, to, promotionPiece);

        if (move != null) {
            log.debug("Move found: {} - {}", simpleMoveEncoder.encode(move), wdlToString(syzygyResult));
            searchResponse = new SearchByTablebaseResult(move, syzygyResult);
        } else {
            log.warn("Move not found fromIdx={} toIdx={} fen={}", fromIdx, toIdx, game.getCurrentFEN());
        }

        return searchResponse;
    }

    String wdlToString(int syzygyResult) {
        int tb_result = TB_GET_WDL(syzygyResult);
        return switch (tb_result) {
            case TB_WIN -> "TB_WIN";
            case TB_CURSED_WIN -> "TB_CURSED_WIN";
            case TB_DRAW -> "TB_DRAW";
            case TB_BLESSED_LOSS -> "TB_BLESSED_LOSS";
            case TB_LOSS -> "TB_LOSS";
            default -> "UNKNOWN";
        };
    }



    SyzygyPosition bindSyzygyPosition(Game game) {
        PositionReader position = game.getPosition();
        syzygyPosition.setPawns(position.getPawnPositions());
        syzygyPosition.setKnights(position.getKnightPositions());
        syzygyPosition.setBishops(position.getBishopPositions());
        syzygyPosition.setQueens(position.getQueenPositions());
        syzygyPosition.setRooks(position.getRookPositions());
        syzygyPosition.setKings(position.getKingPositions());
        syzygyPosition.setWhite(position.getPositions(Color.WHITE));
        syzygyPosition.setBlack(position.getPositions(Color.BLACK));
        syzygyPosition.setRule50((byte) position.getHalfMoveClock());

        Square ep = position.getEnPassantSquare();
        syzygyPosition.setEp(ep == null ? 0 : (byte) ep.idx());

        syzygyPosition.setTurn(Color.WHITE.equals(position.getCurrentTurn()));

        return syzygyPosition;
    }
}
