# Collaboration in Terms of the Template Method Design Pattern

The collaboration between the `AbstractPawnMoveGenerator`, `PawnWhiteMoveGenerator`, and `PawnBlackMoveGenerator` classes is an example of the **Template Method Design Pattern**. Below is a detailed explanation of this collaboration.

---

## **Role of `AbstractPawnMoveGenerator`**
- This class serves as the **abstract base class** in the Template Method pattern and defines the **algorithm skeleton** for generating pawn moves using the `generatePseudoMoves()` method (or similar methods).
- The overall framework of how pawn moves are generated and structured is centralized within this class, meaning it handles the **high-level logic**, ensuring consistency across pawn move generations, regardless of color or specific behavior.
- To facilitate customization for specific pawn behaviors, `AbstractPawnMoveGenerator` defines a series of **abstract methods** (like `getOneSquareForward()`, `getTwoSquareForward()`, `getAttackSquareLeft()`, `getAttackSquareRight()`, etc.) that must be implemented by concrete subclasses. These methods represent the **customization points** of the algorithm.

---

## **Role of `PawnWhiteMoveGenerator` and `PawnBlackMoveGenerator`**
- These two classes act as **concrete implementations** that customize the steps of the algorithm according to the specific rules for white and black pawns, respectively.
- Both classes override the abstract methods from `AbstractPawnMoveGenerator` to define behaviors specific to the pawn's **color** and **movement direction**:
    - White pawns move "northward" on the board (increasing rank values).
    - Black pawns move "southward" on the board (decreasing rank values).
- By overriding methods like:
    - `getOneSquareForward()`
    - `getTwoSquareForward()`
    - `getAttackSquareLeft()`
    - `getAttackSquareRight()`

  the two classes ensure that **basic moves** like forward steps, double steps, and diagonal captures adhere to the correct movement direction and rules for white and black pawns.

---

## **Core Principles of the Template Method Pattern**
### 1. **Algorithm Skeleton in the Base Class**
- `AbstractPawnMoveGenerator` encapsulates the **core framework** for move generation and mandates specific abstract methods for components requiring customization.
- The structure ensures that the process remains consistent while delegating color-specific logic to concrete classes.

---

### 2. **Customization through Subclasses**
- `PawnWhiteMoveGenerator` and `PawnBlackMoveGenerator` **specialize behavior** in line with the rules for white and black pawns, respectively, by implementing the abstract methods.
- For example:
    - `getOneSquareForward()` has `Rank + 1` for white pawns but `Rank - 1` for black pawns.
    - Promotion pieces (`getPromotionPieces()`) differ, specifying white and black promotion options.

---

### 3. **Invariant Control Flow**
- The `generatePseudoMoves()` method in `AbstractPawnMoveGenerator` executes the **same overall steps** for generating moves (e.g., calculating potential moves, checking for promotions, adding special rules like captures) without knowledge of the pawn's color-specific rules.
- The details are handled by the subclasses.

---

## **Benefits of Using the Template Method Pattern**

- **Code Reusability**: Shared logic in the `AbstractPawnMoveGenerator` reduces code duplication, enabling coherent maintenance and extension.
- **Flexibility and Scalability**: The framework can easily accommodate more variations in pawn behaviors or rules (e.g., custom pawn movements for special scenarios).
- **Separation of Concerns**: High-level move generation (framework) is decoupled from low-level details (e.g., directional moves for specific pawn colors).

---

## **How the Collaboration Works**
1. The `AbstractPawnMoveGenerator` calls the concrete implementations of abstract methods based on the pawn's color.
2. Subclasses (`PawnWhiteMoveGenerator` and `PawnBlackMoveGenerator`) ensure that move generation aligns with specific pawn movement rules.
3. Example:
    - Suppose a white pawn is positioned on a specific square.
    - `generatePseudoMoves()` in `AbstractPawnMoveGenerator` orchestrates the flow:
        - When it encounters behavior-specific steps like moving forward or capturing, it delegates to `getOneSquareForward()` or `getAttackSquareLeft()`, which are implemented differently in `PawnWhiteMoveGenerator`.

---

## **Summary**
The collaboration encapsulates the **Template Method Pattern** by separating the **invariant move generation process** (defined in the abstract class) from the **variant, color-specific logic** (customized in the concrete `PawnWhiteMoveGenerator` and `PawnBlackMoveGenerator` subclasses). This ensures a consistent, extensible design tailored to the specific behaviors of white and black pawns.