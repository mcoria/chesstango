package iterators;

import java.util.Iterator;
import java.util.Map;

import chess.Pieza;
import chess.Square;

public interface BoardIterator extends Iterator<Map.Entry<Square, Pieza>>{

}
