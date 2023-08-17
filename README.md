[![Java CI with Maven](https://github.com/mcoria/chesstango/actions/workflows/maven.yml/badge.svg)](https://github.com/mcoria/chesstango/actions/workflows/maven.yml)

# Overview
This project exemplifies the practical application of object-oriented design patterns within a traditional board game: chess.

The majority of these patterns have been extensively outlined in the GoF book, "Design Patterns: Elements of Reusable Object-Oriented Software (1994)". This book is particularly recommended for beginner programmers. Over time, I've consistently kept a copy of this book on hand for easy reference.

It's important to note that while this project draws heavily from the patterns described in the GoF book, there are instances where unique patterns are employed. These exceptional cases are explicitly identified, along with references to external sources that elucidate their concepts.

It's worth clarifying that the objective of this project isn't to incorporate every conceivable design pattern present within the domain. Rather, I've selectively adopted patterns that aptly address specific challenges posed by the task.

While I've certainly taken performance considerations into account, the predominant non-functional priority remains the precise and coherent implementation of design patterns.

## Engine Features
- Board
  - square-centric 8x8 board representation
  - piece-centric bitboard representation
- Move generation 
  - Implementation has been [tested](PerftMainTestSuiteResult.txt) with +100K positions (perft)
- Representations
  - Forsyth–Edwards Notation (FEN)
  - Portable Game Notation (PGN) 
  - Extended Position Description (EPD)
  - Standard Algebraic Notation (SAN)
- Communication protocols
  - UCI
  - [Lichess API](https://lichess.org/api)
- Search
  - Zobrist hash
  - MinMax
  - Alpha Beta Pruning
  - Iterative Deepening
  - Transposition Tables
- Evaluation
  - Piece-Square Tables
- Polyglot Opening Books
- Arena for tournaments between ChessTango vs other UCI-compliant engines

## Creational Patterns
- Factory Method
- Builder
- Abstract Factory

### Factory Method
Factory method pattern is usually applied in combination with Template method pattern:
- [AbstractCardinalMoveGenerator](board/src/main/java/net/chesstango/board/movesgenerators/pseudo/strategies/AbstractCardinalMoveGenerator.java) class.
- [AbstractJumpMoveGenerator](board/src/main/java/net/chesstango/board/movesgenerators/pseudo/strategies/AbstractJumpMoveGenerator.java) class.

### Abstract Factory
Abstract Factory pattern examples:
- [MoveFactory](board/src/main/java/net/chesstango/board/moves/MoveFactory.java) is the interface and [MoveFactoryBlack](board/src/main/java/net/chesstango/board/moves/imp/MoveFactoryBlack.java) and [MoveFactoryWhite](board/src/main/java/net/chesstango/board/moves/imp/MoveFactoryWhite.java) are two different implementations.

### Builder
Builder pattern has been implemented with different participant classes
- Builder: ChessBuilder class declares the interface.
- Concrete Builders
    - ASCIIOutput, the result is retried with method xXX
    - FENCoder, the result is retried with method XX
    - ChessBuilderParts 
    - ChessBuilderBoard 
    - ChessBuilderGame
- Director: Board.constructBoardRepresentation() is the director method

## Structural Patterns
- Facade
- Proxy

Facade pattern is implemented by [GameImp](board/src/main/java/net/chesstango/board/GameImp.java) class.

Proxy pattern is implemented by ChessPositionReader as access control interface to underlying implementation classes. Game and ChessPosition implements this interface, Game forwards all requests to ChessPosition class for actual implementation.
MoveGenaratorWithCacheProxy implements a cache proxy. Another example is MoveGeneratorWithCacheProxy.

## Behavioral Patterns
- Template method
- Command
- Strategy
- Visitor
- Iterator

### Template method
Template method pattern is applied in different situations by abstract classes: 
[AbstractPawnMoveGenerator](board/src/main/java/net/chesstango/board/movesgenerators/pseudo/strategies/AbstractPawnMoveGenerator.java)

By convention, those classes with template methods are abstract classes and named with the prefix Abstract.
 
### Command
Command pattern in combination with Visitor pattern can be found at chess.moves package. 

Interfaces Move/MoveKing/MoveCastling define DO and UNDO operations.

Classes in package net.chesstango.board.moves.imp implement these interfaces.

### Strategy
Strategy pattern is implemented at net.chesstango.board.movesgenerators.pseudo.strategies package. MoveGenerator interface declares the interface, all the classes in this package implements the interface (with the exception of PeonPasanteMoveGenerator). 

### Iterators
Iterators can be found at net.chesstango.board.iterators package.


# Credits
- [www.chessprogramming.org](https://www.chessprogramming.org) probably is one of the best chess programming help resources
- [chariot](https://github.com/tors42/chariot) has been used for invoking Lichess API
- [chessboard.js](https://chessboardjs.com) has been used for implementing ArenaTV UI
- [jenetics](https://jenetics.io) has been used for game evaluation parameter tuning
