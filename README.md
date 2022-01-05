This project demonstrates the implementation of Object-oriented design patterns in a classical board game: <b>chess</b>.

Most of the patterns applied in this project are described in more detail by GoF book (Design Patterns: Elements of Reusable Object-Oriented Software (1994)). You should read this book especially if you are a beginner programmer. As the years go by, I always keep a copy near for reference.

There are special cases where patterns applied in this project are not described by GoF book, I explicitly mention those cases and the bibliography references that describe them.

By no means does this project intents to implement all design patterns you may find in the field, instead, I've used only those patterns that helped me to solve specific situations for the task at hand.

Although performance is taken into consideration, this is not the top non-functional priority, clear design patterns implementation is preferred.

Creational Patterns
- Factory method
- Builder
  
Structural Patterns
- Decorator

 
Behavioral Patterns
- Template method
- Command

Template method pattern in combination with Factory method pattern can be found at AbstractCardinalMoveGenerator.java class. 

Command pattern can be found at moveexecutors package. Interface Move define DO and UNDO operations, subclasses in this package implement them.

Builder pattern has been implemented with different participant classes
- Builder: ChessBuilder class declares the interface.
- Concrete Builders
	-ASCIIOutput, the result is retrived with method xXX
    -FENCoder, the result is retrived with method XX
    -ChessBuilderParts 
	-ChessBuilderBoard 
	-ChessBuilderGame
- Director: Board.constructBoardRepresentation() is the director method

Decorator pattern can be found at moveexecutors package. MoveDecorator is an abstract class that declares a reference to concrete componenets being decoreted.

Project roadmap
- Implement a chess state machine. (DONE)
- Implement communication protocol. (TBD)
- Implement AI player.


