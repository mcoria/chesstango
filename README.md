[![Java CI with Maven](https://github.com/mcoria/chesstango/actions/workflows/maven.yml/badge.svg)](https://github.com/mcoria/chesstango/actions/workflows/maven.yml)
[![lichess-blitz](https://lichess-shield.vercel.app/api?username=chesstango_bot&format=bullet)](https://lichess.org/@/chesstango_bot/perf/bullet)
[![lichess-blitz](https://lichess-shield.vercel.app/api?username=chesstango_bot&format=blitz)](https://lichess.org/@/chesstango_bot/perf/blitz)
[![lichess-rapid](https://lichess-shield.vercel.app/api?username=chesstango_bot&format=rapid)](https://lichess.org/@/chesstango_bot/perf/rapid)


# Overview
ChessTango exemplifies the practical application of object-oriented design patterns within a board game context: a chess engine.

The majority of these patterns have been extensively outlined in the GoF book, "Design Patterns: Elements of Reusable Object-Oriented Software (1994)". This book is particularly recommended for beginner programmers. Over time, I've consistently kept a copy of this book on hand for easy reference.

It's important to note that while this project draws heavily from the patterns described in the GoF book, there are instances where unique patterns are employed. These exceptional cases are explicitly identified, along with references to external sources that elucidate their concepts.

It's worth clarifying that the objective of this project isn't to incorporate every conceivable design pattern present within the domain. Rather, I've selectively adopted patterns that aptly address specific challenges posed by the task.

While I've certainly taken performance considerations into account, the predominant non-functional priority remains the precise and coherent implementation of design patterns.

[Patterns Index](docs/PatternIndex.md) provides a non-exhaust list of the patterns that have been used.

# Engine Features
- Board
  - square-centric 8x8 board representation
  - piece-centric bitboard representation
- Move generation 
  - Implementation has been [tested](PerftMainTestSuiteResult.txt) with +100K positions (perft)
- Search
  - Alpha Beta Pruning
  - Zobrist hash
  - Quiescence Search
  - Iterative Deepening
  - Aspiration Windows
  - Transposition Tables
- Evaluation
  - Piece-Square Tables
- Engine
  - Polyglot Opening Books
- Supported communication protocols
  - Universal Chess Interface (UCI)
  - [Lichess API](https://lichess.org/api)
- Supported formats
  - Forsyth–Edwards Notation (FEN)
  - Portable Game Notation (PGN)
  - Extended Position Description (EPD)
  - Standard Algebraic Notation (SAN)
- GraalVM for native image (.exe) packaging  

# Usage

## Chess GUI with UCI support
1. Download ChessTango binary distribution
2. Download [Arena Chess GUI](http://www.playwitharena.de/)
3. From the Arena GUI MenuBar, Select Engines > Install New Engine
4. Select .exe file type and navigate to location of the saved binary file.
5. Select the binary file and choose UCI protocol.
6. Go to the MenuBar, Select Engines > Manage... > Details and Select ChessTango Chess Engine
7. Under General, Click on the Type drop-down list > select UCI. Apply changes.
8. You can play now !!!


## Lichess BOT
If you prefer to play on-line you can challenge [chesstango_bot](https://lichess.org/@/chesstango_bot).
Keep in mind the engine may be offline, I occasionally run it for fun.  
For the moment Standard time-controlled (Bullet/Blitz/Rapid) games are acceptable.


# Credits
- [www.chessprogramming.org](https://www.chessprogramming.org) probably is one of the best chess programming help resources
- [chariot](https://github.com/tors42/chariot) has been used for invoking Lichess API

