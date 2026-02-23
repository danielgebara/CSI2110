# Copilot Instructions for CSI2110 Data Structures

## Project Overview
This is a **university computer science course repository** (CSI2110) focused on **foundational data structures and algorithms**. It contains labs, assignments, and programming projects implementing and analyzing core ADTs in Java.

## Architecture & Key Components

### Lab Modules (Progressive Skill Building)
- **lab1**: Linked list foundations (Node, LinkedList, DNode)
- **lab2**: Stack implementations (ArrayStack, LinkedStack) with practical applications (BracketsBalance)
- **lab3**: Sequences & positional lists - abstractions for managing collections (List, PositionalList, ArrayList, LinkedSequence)
- **lab5**: Binary Search Trees (LinkedBinarySearchTree with recursive and iterator-based traversals)

### Major Assignments
- **ProgrammingAssignment1**: Island/Lake Survey using Partition ADT for union-find clustering
- **ProgrammingAssignment2**: Paris Metro graph algorithm (Kruskal's MST using DSU with path compression and union by rank)
- **SEGq2**: Object-oriented banking system with inheritance (Account types, Client types, Rewards)

### Design Patterns
1. **Interface-based abstractions**: `Stack<E>`, `List<E>`, `PositionalList<E>` define contract before concrete implementations
2. **Generic type parameters**: All collections use `<E>` for type safety
3. **Nested inner classes**: DSU, Edge classes embedded in assignment solutions for encapsulation
4. **Position-based abstraction**: `Position<E>` interface allows collections to track element locations (lab3)

## Code Style & Conventions

### Licensing & Attribution
- Most lab files include GNU GPL v3 header + copyright to Goodrich/Tamassia/Goldwasser authors
- Always preserve copyright headers when modifying lab files
- Reference source: "Data Structures and Algorithms in Java, Sixth Edition"

### Generics Usage
```java
// Standard pattern - use Type variable E
public class ArrayStack<E> implements Stack<E> {
    private E[] data = (E[]) new Object[capacity]; // safe cast, may warn
}
```

### Testing Patterns
- **No formal test framework** (no JUnit) - tests use manual print statements
- Test classes: `SequenceTester`, `TestTree` contain main methods with hardcoded test data
- Tests read from files: `tests.txt` (lab2 BracketsBalance), input files in ProgrammingAssignment1/Inputs
- Output validation: compare against expected outputs in PartAOutputs/PartBOutputs directories

## Critical Implementation Details

### Union-Find (Disjoint Set Union) - Repeated Pattern
Used in ProgrammingAssignment1 and ProgrammingAssignment2:
```java
private static class DSU {
    int[] parent;
    int[] rank;
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]); // path compression
        return parent[x];
    }
    boolean union(int a, int b) { /* union by rank */ }
}
```
**Key optimization**: Path compression in `find()` + union by rank for O(α(n)) amortized complexity.

### Positional List Pattern (lab3)
- `PositionalList<E>` interface separates position concept from element access
- Implementations: `LinkedPositionalList` (doubly-linked), `ArraySequence`
- Usage: Methods like `addLast()`, `remove(Position<E>)`, `set(int, E)`

### Stack Applications
- **BracketsBalance**: Uses stack to validate bracket matching `{(x+y)-[2*z]}`
- Pattern: push opening brackets, pop on closing brackets, validate matching types

## Build & Execution

### Compilation
```bash
javac lab2/ArrayStack.java
javac lab3/LinkedPositionalList.java
```

### Running Tests
- Manual main method execution: `java lab2.BracketsBalance '{(x+y)}'`
- Test file mode: `java lab2.BracketsBalance` (reads tests.txt)
- Tree tests: `java lab5.TestTree` (produces level-order, preorder, inorder, postorder output)

### File Structure Conventions
- Source files organized by lab/assignment directory matching package names
- Test data in subdirectories: `Inputs/`, `PartAOutputs/`, `PartBOutputs/`
- README.txt files document authors and submission details

## Common Workflows

### Adding New Collection Method
1. Define contract in interface (e.g., `PositionalList.java`)
2. Implement in array-based version (e.g., `ArraySequence.java`)
3. Implement in linked version (e.g., `LinkedPositionalList.java`)
4. Test in corresponding Tester class (e.g., `SequenceTester.java`)

### Graph/Tree Algorithm Development
- Implement helper classes (DSU, Edge) as nested static classes
- Use vertex indexing (0-based arrays) paired with name mappings
- Kruskal's algorithm pattern: sort edges → union-find → include if union succeeds

### Testing New Data Structure
- Add test case to existing Tester class with print statements
- Validate against manual calculation or reference implementation
- Update corresponding Output files if behavior changes

## Known Characteristics
- **No external dependencies** beyond Java standard library
- **No build tool** (Maven/Gradle) - direct javac compilation
- **Course material origin**: Implementations based on textbook examples
- **Incomplete implementations**: Some files have placeholder methods (e.g., BracketsBalance `isBalanced()`)

## Keywords for Navigation
- **Positional abstraction**: lab3/Position.java, PositionalList.java
- **Graph algorithms**: ProgrammingAssignment2 (MST), ProgrammingAssignment1 (clustering)
- **Stack use cases**: lab2/BracketsBalance.java (expression validation)
- **Inheritance/OOP**: SEGq2 (banking system with multiple account/client types)
