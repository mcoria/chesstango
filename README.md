[![Java CI with Maven](https://github.com/mcoria/chesstango/actions/workflows/maven.yml/badge.svg)](https://github.com/mcoria/chesstango/actions/workflows/maven.yml)

# Overview
This project exemplifies the practical application of object-oriented design patterns within a traditional board game: chess.

The majority of these patterns have been extensively outlined in the GoF book, "Design Patterns: Elements of Reusable Object-Oriented Software (1994)". This book is particularly recommended for beginner programmers. Over time, I've consistently kept a copy of this book on hand for easy reference.

It's important to note that while this project draws heavily from the patterns described in the GoF book, there are instances where unique patterns are employed. These exceptional cases are explicitly identified, along with references to external sources that elucidate their concepts.

It's worth clarifying that the objective of this project isn't to incorporate every conceivable design pattern present within the domain. Rather, I've selectively adopted patterns that aptly address specific challenges posed by the task.

While I've certainly taken performance considerations into account, the predominant non-functional priority remains the precise and coherent implementation of design patterns.

## Creational Patterns
- Factory Method
- Builder
- Abstract Factory

### Factory Method
Factory method pattern is usually applied in combination with Template method pattern:
- [AbstractCardinalMoveGenerator](src/main/java/net/chesstango/pseudomovesgenerators/AbstractCardinalMoveGenerator.java) class.
- [AbstractJumpMoveGenerator](src/main/java/net/chesstango/pseudomovesgenerators/AbstractJumpMoveGenerator.java) class.

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

### Abstract Factory
Abstract Factory pattern examples:

MoveFactory class declares the interface. MoveFactoryBlack and MoveFactoryWhite are two different implementations. 

## Structural Patterns
- Decorator
- Facade
- Proxy

Facade pattern is implemented by [Game](src/main/java/net/chesstango/Game.java) class.

Decorator pattern is implemented by [MoveDecorator](src/main/java/net/chesstango/moves/imp/MoveDecorator.java) class, this abstract class declares a reference to concrete Move being decorated.

Proxy pattern is implemented by ChessPositionReader as access control interface to underlying implementation classes. Game and ChessPosition implements this interface, Game forwards all requests to ChessPosition class for actual implementation.
MoveGenaratorWithCacheProxy implements a cache proxy.

## Behavioral Patterns
- Template method
- Command
- Strategy
- Visitor
- Iterator

### Template method
Template method pattern is applied in different situations by abstract classes: 
[AbstractPawnMoveGenerator](src/main/java/net/chesstango/pseudomovesgenerators/AbstractPawnMoveGenerator.java)

By convention, those classes with template methods are abstract classes and named with the prefix Abstract.
 
### Command
Command pattern in combination with Visitor pattern can be found at chess.moves package. 

Interface Move/MoveKing/MoveCastling define DO and UNDO operations.

Classes in package chess.moves.imp implement these interfaces.

### Strategy
Strategy pattern is implemented at chess.pseudomovesgenerators package. MoveGenerator interface declares the interface, all the classes in this package implements the interface (with the exception of PeonPasanteMoveGenerator). 

### Iterators
Iterators can be found at chess.iterators package, two different iterator types are defined:
- Square Iterators
- Piece Placement Iterators

## Project roadmap
- Implement chess state machine. (DONE)
- FEN decoder/encoder and PGN decoder. (DONE)
- UCI communication protocol. (DONE)
- Implement Search algorithms (WIP)
  - MinMax
  - MinMax with Alpha Beta Pruning
  - Iterative Deepening
- Implement Evaluation algorithms (WIP)
