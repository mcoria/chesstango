# Overview
This project demonstrates the implementation of Object-oriented design patterns in a classical board game: <b>chess</b>.

Most of the patterns applied in this project are described in more detail by GoF book (Design Patterns: Elements of Reusable Object-Oriented Software (1994)). You should read this book especially if you are a beginner programmer. As the years go by, I always keep a copy near for reference.

There are special cases where patterns applied in this project are not described by GoF book, I explicitly mention those cases and the bibliography references that describe them.

By no means does this project intents to implement all design patterns you may find in the field, instead, I've used only those patterns that helped me to solve specific situations for the task at hand.

Although performance is taken into consideration, this is not the top non-functional priority, clear design patterns implementation is preferred.

## Creational Patterns
- Factory Method
- Builder

### Factory Method
Template method pattern in combination with Factory method pattern can be found at:
- [AbstractCardinalMoveGenerator](src/main/java/chess/pseudomovesgenerators/AbstractCardinalMoveGenerator.java) class.
- [AbstractJumpMoveGenerator](src/main/java/chess/pseudomovesgenerators/AbstractJumpMoveGenerator.java) class.

### Builder
Builder pattern has been implemented with different participant classes
- Builder: ChessBuilder class declares the interface.
- Concrete Builders
    - ASCIIOutput, the result is retrived with method xXX
    - FENCoder, the result is retrived with method XX
    - ChessBuilderParts 
    - ChessBuilderBoard 
    - ChessBuilderGame
- Director: Board.constructBoardRepresentation() is the director method

## Structural Patterns
- Decorator
- Facade

Facade pattern is implemented by Game class.

Decorator pattern can be found at chess.moves package. MoveDecorator is an abstract class that declares a reference to concrete components being decorated.

## Behavioral Patterns
- Template method
- Command
- Strategy
- Visitor
- Iterator

Template method pattern is applied in different situations by abstract classes: 
[PawnAbstractMoveGenerator](src/main/java/chess/pseudomovesgenerators/PawnAbstractMoveGenerator.java)
 


Command pattern in combination with Visitor pattern can be found at chess.moves package. Interface Move define DO and UNDO operations, subclasses in this package implement them.

Strategy pattern is implemented at movegenerators package. MoveGenerator interface declares the interface, all the classes in this package implements the interface (with the exception of PeonPasanteMoveGenerator). 

Iterators can by found at chess.iterators package, two different iterator types are defined:
- Square Iterators
- Piece Placement Iterators

## Project roadmap
- Implement a chess state machine. (DONE)
- Implement communication protocol. (TBD)
- Implement AI player.


