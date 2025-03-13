# Double Dispatch Implementation with `LegalMoveFilter` and `Move`

The collaboration among the interfaces/classes `LegalMoveFilter`, `LegalMoveFilterSelector`, `Move`, `MoveKing`, and `MoveCastling` demonstrates **Double Dispatch** in Java. Using the interplay between `Move` and `LegalMoveFilter` interfaces, specialized by `MoveKing` and `MoveCastling`, Double Dispatch is effectively implemented. Here's a detailed breakdown of how these components collaborate:

---

## **1. LegalMoveFilter Interface**
The `LegalMoveFilter` interface provides overloaded methods to determine the legality of different types of moves:

- `isLegalMove(Move move)`
- `isLegalMoveKing(MoveKing move)`
- `isLegalMoveCastling(MoveCastling moveCastling)`

These methods form the **first layer of dispatch**: based on the runtime type of `Move`, one of the methods in `LegalMoveFilter` is selected when invoked.

---

## **2. Move Interface**
The `Move` interface acts as the base interface for all chess moves (such as `MoveKing` and `MoveCastling`) and extends `LegalMoveFilterSelector`. It provides a default implementation of the `isLegalMove(LegalMoveFilter filter)` method:

```java
@Override
default boolean isLegalMove(LegalMoveFilter filter) {
    return filter.isLegalMove(this);  // Delegates to LegalMoveFilter
}
```

This delegation makes it possible for `LegalMoveFilter` to determine whether a move is legal by invoking one of its methods. The actual **runtime type** of the `Move` instance ensures that the correct method is selected.

---

## **3. Specialized Moves (MoveKing and MoveCastling Interfaces)**
Specialized move types such as `MoveKing` and `MoveCastling` **override the default behavior** of `isLegalMove()` and provide logic specific to their respective move types.

They delegate their legality checks to the appropriate method of the `LegalMoveFilter` interface:

- **In `MoveKing`:**
  ```java
  @Override
  default boolean isLegalMove(LegalMoveFilter filter) {
      return filter.isLegalMoveKing(this);  // Delegates to the specific method for king moves
  }
  ```

- **In `MoveCastling`:**
  ```java
  @Override
  default boolean isLegalMove(LegalMoveFilter filter) {
      return filter.isLegalMoveCastling(this);  // Delegates to the specific method for castling moves
  }
  ```

The **runtime type** of the move (`MoveKing` or `MoveCastling`) determines which `isLegalMove()` implementation is executed. These implementations then ensure that the correct `LegalMoveFilter` method is invoked.

---

## **4. LegalMoveFilterSelector Interface**
The `LegalMoveFilterSelector` interface provides a utility for any move to generically check its legality via:

```java
boolean isLegalMove(LegalMoveFilter filter);
```

It ensures that moves (like `MoveKing` or `MoveCastling`) rely on the filtering mechanism provided by `LegalMoveFilter`.

---

## **Double Dispatch in Action**

### **Steps:**
1. **First Dispatch**  
   The caller invokes `isLegalMove()` on a `Move` reference (or a subclass like `MoveKing` or `MoveCastling`). The **runtime type of the move** determines which implementation of `isLegalMove()` will execute.

2. **Second Dispatch**  
   Inside the specialized `isLegalMove()` implementation (e.g., `MoveCastling`'s), the method delegates the call to a specific method in `LegalMoveFilter`. The **runtime type of the filter** further determines how the legality check is performed.

---

## **Why Use Double Dispatch Here?**
Double Dispatch enables **decoupling** between move types and filter logic. This design supports the addition of:

1. New move types (`MoveQueen`, `MovePawn`, etc.)
2. New legality filters (`AdvancedLegalMoveFilter`, `BasicLegalMoveFilter`, etc.)

...without modifying existing logic. It adheres to the **Open/Closed Principle** by keeping the codebase extensible and flexible.

---

## **Example Flow**
1. A `LegalMoveFilter` instance (e.g., `BasicLegalMoveFilter`) is provided.
2. A `Move` instance, say of type `MoveCastling`, invokes `isLegalMove(filter)` (either from its parent or its own implementation).
3. Based on the runtime type of `filter`, the corresponding method (`isLegalMoveCastling(MoveCastling moveCastling)`) is invoked, completing the **Double Dispatch**.

---

## **Summary**
Double Dispatch is achieved by:
1. Using the base `Move` interface to provide a general `isLegalMove()` implementation.
2. Overriding the method in specialized moves (e.g., `MoveKing`, `MoveCastling`) to customize the behavior.
3. Invoking the `LegalMoveFilter` methods, where the runtime types of both `Move` and `LegalMoveFilter` ensure the correct logic is executed.

This design ensures extensibility, flexibility, and separation of responsibilities when determining the legality of a move.