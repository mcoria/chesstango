package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByOpenBook implements SearchByChain {
    private final Random random = new Random();

    private final PolyglotBook book;

    SearchByOpenBook(PolyglotBook book) {
        this.book = book;
    }


    @Override
    public SearchResponse search(SearchContext context) {
        SearchResponse searchResponse = null;
        if (book != null) {
            Optional<PolyglotEntry> polyglotEntryOpt = searchByBook(context.getGame());
            if (polyglotEntryOpt.isPresent()) {
                long timeSearching = Duration.between(context.getStartSearchInstant(), Instant.now()).toMillis();
                searchResponse = createSearchResponse(context.getGame(), polyglotEntryOpt.get(), timeSearching);
            }
        }
        return searchResponse;
    }

    private Optional<PolyglotEntry> searchByBook(Game game) {
        List<PolyglotEntry> polyglotEntries = book.search(game.getPosition().getZobristHash());
        if (polyglotEntries != null && !polyglotEntries.isEmpty()) {
            return Optional.of(selectRandomEntry(polyglotEntries));
        }
        return Optional.empty();
    }

    /**
     * Selects weighted random entry from available entries
     */
    private PolyglotEntry selectRandomEntry(List<PolyglotEntry> entries) {
        long totalWeight = entries.stream()
                .mapToLong(PolyglotEntry::weight)
                .sum();

        long randomValue = random.nextLong(totalWeight);
        int cumulative = 0;

        for (PolyglotEntry entry : entries) {
            cumulative += entry.weight();
            if (randomValue < cumulative) {
                return entry;
            }
        }

        return entries.getFirst();
    }


    private SearchResponse createSearchResponse(Game game, PolyglotEntry polyglotEntry, long timeSearching) {
        SearchResponse searchResponse = null;

        Optional<Move> optMove = polyglotEntryToMove(game, polyglotEntry);

        if (optMove.isPresent()) {
            searchResponse = new SearchByOpenBookResult(optMove.get(), polyglotEntry, timeSearching);
        }

        return searchResponse;
    }

    static Optional<Move> polyglotEntryToMove(Game game, PolyglotEntry polyglotEntry) {
        MoveContainerReader<Move> possibleMoves = game.getPossibleMoves();

        Square from = Square.of(polyglotEntry.from_file(), polyglotEntry.from_rank());

        Square to = Square.of(polyglotEntry.to_file(), polyglotEntry.to_rank());

        PositionReader position = game.getPosition();
        if (position.getCurrentTurn() == Color.WHITE) {
            if (Square.e1.equals(from) && Piece.KING_WHITE.equals(position.getPosition(from).piece())) {
                if (Square.h1.equals(to) && Piece.ROOK_WHITE.equals(position.getPosition(to).piece()) && position.isCastlingWhiteKingAllowed()) {
                    to = Square.g1;
                } else if (Square.a1.equals(to) && Piece.ROOK_WHITE.equals(position.getPosition(to).piece()) && position.isCastlingWhiteQueenAllowed()) {
                    to = Square.c1;
                }
            }
        } else {
            if (Square.e8.equals(from) && Piece.KING_BLACK.equals(position.getPosition(from).piece())) {
                if (Square.h8.equals(to) && Piece.ROOK_BLACK.equals(position.getPosition(to).piece()) && position.isCastlingBlackKingAllowed()) {
                    to = Square.g8;
                } else if (Square.a8.equals(to) && Piece.ROOK_BLACK.equals(position.getPosition(to).piece()) && position.isCastlingBlackQueenAllowed()) {
                    to = Square.c8;
                }
            }
        }

        Move move = possibleMoves.getMove(from, to);

        if (move != null) {
            log.debug("OpenBook move found: {}; weight: {}", SimpleMoveEncoder.INSTANCE.encode(move), polyglotEntry.weight());

            return Optional.of(move);
        } else {
            log.warn("Move not found fromIdx={} toIdx={} fen={}", from, to, game.getCurrentFEN());

            return Optional.empty();
        }
    }
}
