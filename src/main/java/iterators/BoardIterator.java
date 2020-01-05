package iterators;

import java.util.Iterator;
import java.util.AbstractMap.SimpleImmutableEntry;

import chess.Pieza;
import chess.Square;

public interface BoardIterator extends Iterator<SimpleImmutableEntry<Square, Pieza>>{

}
