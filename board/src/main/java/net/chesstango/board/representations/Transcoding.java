package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.board.representations.pgn.PGNDecoder;
import net.chesstango.board.representations.pgn.PGNGame;

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

    public List<String> pgnFileToFenPositions(InputStream inputStream){

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        BufferedReader bufferReader = new BufferedReader(inputStreamReader);

        List<PGNGame> pgnGames = null;
        try {
            pgnGames = new PGNDecoder().decodeGames(bufferReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Game> games = new ArrayList<>();

        pgnGames.forEach(pgnGame -> {
            try {
                Game game = pgnGame.buildGame();
                games.add(game);
            } catch (RuntimeException e){
                e.printStackTrace(System.err);
                System.err.println(pgnGame);
            }
        });

        List<String> fenPositions = new ArrayList<>();
        games.forEach( game -> {
            FENEncoder fenEncoder = new FENEncoder();
            game.getChessPosition().constructChessPositionRepresentation(fenEncoder);
            String fenString = fenEncoder.getChessRepresentation().toString();
            fenPositions.add(fenString);
        });

        return fenPositions;
    }
}
