package chess;

import layers.KingCacheBoard;

public interface KingMove extends Move {
	
	void executeMove(KingCacheBoard kingCacheBoard);
	void undoMove(KingCacheBoard kingCacheBoard);	
}