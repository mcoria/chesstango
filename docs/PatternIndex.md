# Patterns Index

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
- [MoveFactory](board/src/main/java/net/chesstango/board/moves/MoveFactory.java) is the interface and [MoveFactoryBlack](../board/src/main/java/net/chesstango/board/moves/imp/MoveFactoryBlack.java) and [MoveFactoryWhite](../board/src/main/java/net/chesstango/board/moves/imp/MoveFactoryWhite.java) are two different implementations.

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

Facade pattern is implemented by [GameImp](../board/src/main/java/net/chesstango/board/GameImp.java) class.

Proxy pattern is implemented by ChessPositionReader as access control interface to underlying implementation classes. Game and ChessPosition implements this interface, Game forwards all requests to ChessPosition class for actual implementation.
MoveGenaratorWithCacheProxy implements a cache proxy. Another example is MoveGeneratorWithCacheProxy.


## Behavioral Patterns
- State
- Template method
- Command
- Strategy
- Visitor
- Iterator
- Memento (game state)

### State
The classes in the package `net.chesstango.uci.engine` collaborate within the **State Design Pattern** to implement the behavior of a chess engine that uses the **Universal Chess Interface (UCI)** protocol. 
The **State Design Pattern** allows the engine to encapsulate its behavior and manage the transitions between different operational states. 
Each state is represented as a class that defines specific behavior and controls transitions to other states. Please read [State Pattern](behavioral-state-pattern) for further description.

### Template method
Template method pattern is applied in different situations by abstract classes. By convention, those classes with template methods are abstract classes and named with the `Abstract` prefix.

- [AbstractPawnMoveGenerator](../board/src/main/java/net/chesstango/board/moves/generators/pseudo/strategies/AbstractPawnMoveGenerator.java): provides a structured algorithm for generating pawn moves, while allowing subclasses to customize specific steps of the process, including the calculation of square movements, attack directions, and promotion logic.


### Command
Command pattern in combination with Visitor pattern can be found at chess.moves package.

Interfaces Move/MoveKing/MoveCastling define DO and UNDO operations.

Classes in package net.chesstango.board.moves.imp implement these interfaces.

### Strategy
Strategy pattern is implemented at net.chesstango.board.movesgenerators.pseudo.strategies package. MoveGenerator interface declares the interface, all the classes in this package implements the interface (with the exception of PeonPasanteMoveGenerator).

### Iterators
Iterators can be found at net.chesstango.board.iterators package.
