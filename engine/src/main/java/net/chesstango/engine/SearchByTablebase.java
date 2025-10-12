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
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.nio.file.Files;
import java.nio.file.Path;

import static net.chesstango.piazzolla.syzygy.Syzygy.*;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByTablebase implements SearchChain {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final SyzygyPosition syzygyPosition;

    @Setter
    private SearchChain next;

    @Getter
    private final Syzygy syzygy;

    SearchByTablebase(Syzygy syzygy) {
        this.syzygy = syzygy;
        this.syzygyPosition = new SyzygyPosition();
    }

    static SearchByTablebase open(String syzygyDirectory) {
        if (syzygyDirectory != null) {
            Path syzygyDirectoryPath = Path.of(syzygyDirectory);
            if (Files.isDirectory(syzygyDirectoryPath)) {
                return new SearchByTablebase(Syzygy.open(syzygyDirectoryPath));
            } else {
                log.error("Syzygy directory '{}' not found", syzygyDirectory);
            }
        }
        return null;
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
    public SearchResult search(SearchContext context) {
        SearchResult searchResult = null;
        if (syzygy != null) {
            searchResult = searchByBook(context.getGame());
        }
        return searchResult == null ? next.search(context) : searchResult;
    }

    SearchResult searchByBook(Game game) {
        final int tbLargest = syzygy.tb_largest();
        if (tbLargest >= 3 && tbLargest >= Long.bitCount(game.getPosition().getAllPositions())) {

            SyzygyPosition syzygyPosition = bindSyzygyPosition(game);

            int[] results = new int[TB_MAX_MOVES];
            int res = syzygy.tb_probe_root(syzygyPosition, results);

            if (res != TB_RESULT_FAILED) {
                final int fromIdx = Syzygy.TB_GET_FROM(res);
                final int toIdx = Syzygy.TB_GET_TO(res);
                final int promotion = Syzygy.TB_GET_PROMOTES(res);

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
                    int tb_result = TB_GET_WDL(res);
                    String tb_resultStr = switch (tb_result) {
                        case TB_WIN -> "TB_WIN";
                        case TB_CURSED_WIN -> "TB_CURSED_WIN";
                        case TB_DRAW -> "TB_DRAW";
                        case TB_BLESSED_LOSS -> "TB_BLESSED_LOSS";
                        case TB_LOSS -> "TB_LOSS";
                        default -> "UNKNOWN";
                    };

                    log.debug("Move: {} - {}", simpleMoveEncoder.encode(move), tb_resultStr);

                    MoveEvaluation bestMove = new MoveEvaluation(move, tb_result, MoveEvaluationType.EXACT);

                    return new SearchResult()
                            .addSearchResultByDepth(new SearchResultByDepth(1).setBestMoveEvaluation(bestMove));

                } else {
                    log.warn("Move not found fromIdx={} toIdx={} fen={}", fromIdx, toIdx, game.getCurrentFEN());
                }
            } else {
                log.warn("Syzygy lookup failed: {}", game.getCurrentFEN());
            }
        }
        return null;
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
        syzygyPosition.setRule50(position.getHalfMoveClock());
        syzygyPosition.setTurn(Color.WHITE.equals(position.getCurrentTurn()));
        return syzygyPosition;
    }
}
