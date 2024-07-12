package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.board.representations.pgn.PGNDecoder;
import net.chesstango.board.representations.pgn.PGN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Transcoding {

    public List<FEN> pgnFileToFenPositions(InputStream inputStream) {

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        BufferedReader bufferReader = new BufferedReader(inputStreamReader);

        List<PGN> pgns = null;
        try {
            pgns = new PGNDecoder().decodeGames(bufferReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Game> games = new ArrayList<>();

        pgns.forEach(pgnGame -> {
            try {
                Game game = pgnGame.buildGame();
                games.add(game);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                System.err.println(pgnGame);
            }
        });

        List<FEN> fenPositions = new ArrayList<>();
        games.forEach(game -> {
            FENEncoder fenEncoder = new FENEncoder();
            game.getChessPosition().constructChessPositionRepresentation(fenEncoder);
            FEN fen = fenEncoder.getChessRepresentation();
            fenPositions.add(fen);
        });

        return fenPositions;
    }
}
