# Collaboration of classes in the net.chesstango.uci.engine package

The classes in this package collaborate within the **State Design Pattern** to implement the behavior of a chess engine that uses the **Universal Chess Interface (UCI)** protocol. The **State Design Pattern** allows the engine to encapsulate its behavior and manage the transitions between different operational states. Each state is represented as a class that defines specific behavior and controls transitions to other states.

## **Overview of State Collaboration**

At the center of this architecture is the `UciTango` class, which functions as the **Context** for the State Design Pattern. It holds the current state (`currentState`) and enables state transitions by invoking the `changeState()` method. The various state classes (`WaitCmdUciState`, `ReadyState`, `WaitCmdGoState`, `SearchingState`, and `EndState`) implement the `UCIEngine` interface, defining the behavior for each specific phase of the UCI engine's lifecycle. These states interact with each other and the `Tango` class (responsible for core chess functionalities like searching and managing the game state).

---

## **Key Components and Their Roles**

### **1. `UciTango` (Context)**
- Manages the current state of the UCI engine (`currentState`).
- Delegates commands to the appropriate state instance.
- Coordinates state transitions via `changeState()` (package-private to ensure only state classes trigger transitions).
- Acts as an intermediary between the states and external UCI commands.

---

### **2. States (`UCIEngine` Implementations)**

Each state defines specific behavior for requests such as `do_uci`, `do_setOption`, `do_go`, and more. They govern transitions to other states when specific events or commands are handled.

#### **Key States**

#### **a. `WaitCmdUciState`** (Initial State)
- The starting state of the engine, waiting for the `"uci"` command to initialize.
- Responds with engine identification (`RspId`) and transitions to `ReadyState` after replying with `RspUciOk`.

#### **b. `ReadyState`**
- The default operational state upon successful initialization.
- The engine is "ready," meaning it can accept most UCI commands like `setOption`, `position`, and `isReady`.
- **Handles Transitions**:
    - To `WaitCmdGoState` when a position is set and the `"go"` command is expected.
    - To `EndState` when the `"quit"` command is received.

#### **c. `WaitCmdGoState`** (Transitions From `ReadyState`)
- Entered after setting up a game position and waiting for the `"go"` command.
- Processes the `"go"` command using the `ReqGoExecutor`, which starts a search in the `Tango` instance.
- After processing the `"go"`, switches to the `SearchingState`.

#### **d. `SearchingState`**
- Represents the state where the engine is actively searching for the best move.
- **During Search**:
    - Certain commands (e.g., `newGame`, `position`, or a second `go`) are restricted.
    - Emits `"info"` messages (`RspInfo`) during the search process.
- **Handles Transitions**:
    - Once a move is found, the engine transitions back to `ReadyState` after sending the result (`RspBestMove`).
    - Can transition to `EndState` if a `"quit"` command is received during a search.

#### **e. `EndState`** (Terminal State)
- The final state of the engine after the `"quit"` command.
- All UCI commands are no-ops, effectively shutting down the engine's functionality.

---

## **State Transitions**

Below is an outline of how states interact and transition:

### **1. Startup Phase**
- The engine starts in `WaitCmdUciState`.
- On receiving the `"uci"` command, identifies itself and transitions to `ReadyState`.

### **2. Setting Up a Game**
- In `ReadyState`, the engine can receive commands such as `"setOption"`, `"position"`, and `"newGame"`.
- When a `"position"` command is received, it transitions to `WaitCmdGoState`.

### **3. Starting a Search**
- In `WaitCmdGoState`, the `"go"` command triggers the search process, transitioning to `SearchingState`.

### **4. During Search**
- In `SearchingState`, the engine actively searches for the best move.
- Search progress can generate `"info"` messages.
- Once a move is found, the engine transitions back to `ReadyState`.

### **5. Exiting**
- At any point, if the `"quit"` command is received, the engine transitions to the `EndState`.

---

## **How Each Class Collaborates**

### **1. `UciTango` as the Coordinator**
- Holds the `currentState` and delegates incoming commands to the appropriate state instance.
- Provides controlled state transitions using `changeState`.

### **2. State Responsibilities**
- Each state implements `UCIEngine` and overrides methods to define behavior during that phase.
- States can request transitions by calling `UciTango.changeState`.

### **3. Interaction With `Tango`**
- States interact with `Tango` to control chess-specific operations like starting/stopping searches or setting positions.
- **Examples**:
    - `ReadyState` sets the board position in `Tango`.
    - `SearchingState` uses `Tango`'s search capabilities and listens for search results.

### **4. State-Specific Dependencies**
- `ReadyState` and `WaitCmdGoState` reference each other to manage transitions.
- Searching-related states like `SearchingState` set up listeners (`SearchListener`) to handle search events.

---

## **Collaborative Workflow Example**

1. **Client Issues `"uci"`**
    - `WaitCmdUciState` processes the input and transitions to `ReadyState`.

2. **Client Issues `"position"`**
    - `ReadyState` transitions to `WaitCmdGoState`.

3. **Client Issues `"go"`**
    - `WaitCmdGoState` starts the search and transitions to `SearchingState`.

4. **Search Completes**
    - `SearchingState` transitions back to `ReadyState` with the best move.

5. **Client Issues `"quit"`**
    - The current state transitions to the `EndState`.

By defining behavior in distinct state classes, the UCI engine achieves clear separation of concerns, dynamic behavior switching, and extensibility for future features.
