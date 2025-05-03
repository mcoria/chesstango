package net.chesstango.board.builders;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class GameBuilderTest {
    private GameBuilder builder;
    private FENExporter exporter;

    @BeforeEach
    public void setUp() throws Exception {
        builder = new GameBuilder();
        exporter = new FENExporter(builder);
    }


    @Test
    public void testBuildWithEnPassantSquare() {
        exporter.export(FEN.of("rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8"));

        Game game = builder.getPositionRepresentation();

        PositionReader position = game.getPosition();

        assertEquals(Square.b6, position.getEnPassantSquare());
    }
}
