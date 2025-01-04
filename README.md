# Code Fight

**Code Fight** is a text-based, turn-based programming game inspired by the classic "Core War." In this game, multiple artificial intelligences (AIs) compete within a shared memory space by executing commands to dominate their opponents. The game emphasizes strategy, programming logic, and competition.

## Game Flow

### 1. Initialization Phase
- The memory is initialized in one of two modes:
  - **Random Mode (`INIT_MODE_RANDOM`)**: Fills the memory with random commands.
  - **Stop Mode (`INIT_MODE_STOP`)**: Fills the memory with `STOP` commands.
- Players register their AIs (AI programs) as sequences of commands with parameters.
- AIs are distributed evenly across the memory, ensuring no overlaps.

### 2. Gameplay Phase
- Each AI executes commands in a cyclic memory space based on its sequence.
- Commands include:
  - Data manipulation (`MOV_R`, `MOV_I`)
  - Arithmetic operations (`ADD`, `ADD_R`)
  - Control flow (`JMP`, `JMZ`)
- An AI stops executing when it encounters the `STOP` command.
- Rounds continue until one AI remains active or all are stopped.

### 3. Interaction Commands
Users interact with the game using predefined commands:
- **`add-ai`**: Register an AI program.
- **`start-game`**: Start the game with the registered AIs.
- **`next`**: Execute a specified number of commands across all AIs.
- **`show-memory`**: Display the current memory state.
- **`show-ai`**: Show the status of a specific AI.
- **`end-game`**: End the current game phase.
- **`quit`**: Exit the game.

### 4. Winning Conditions
- The last AI actively executing commands is declared the winner.
- An AI continues executing commands even after achieving domination.

---

**Code Fight** provides a fun and challenging platform where players can create, test, and compete with their AI programs in a dynamic memory space. Let the competition begin!


## Project Structure
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
.
└── packages
    ├── AI.java
    ├── Engine.java
    └── commands
        ├── Command.java
        ├── ADD.java
        ├── ADD_R.java
        ├── CMP.java
        ├── JMP.java
        ├── JMZ.java
        ├── MOV_I.java
        ├── MOV_R.java
        ├── STOP.java
        └── SWAP.java
</pre>
</div>

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17
- IDE: `IntelliJ IDEA` — _or_ — your preferred IDE

### Running the Project

1. Clone the repository:
    ```sh
    git clone https://github.com/seiffhany/code-fight.git
    cd CodeFight
    ```

2. Open the project in your preferred IDE.

3. Compile and run the [Engine](https://github.com/seiffhany/code-fight/blob/main/src/packages/Engine.java) class:
    ```sh
    javac src/packages/Engine.java
    java src/packages/Engine
    ```

## Usage
### Initialization
#### Example Configuration
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
<span></span>
%> java CodeFight 32 # ? _ ^ G g B b
<span></span>
</pre>
</div>

- `32`: The number of memory slots (size of array/memory)
- `#`: Map preview — Symbol for _NOT\_PROCESSED_ 
- `?`: Map preview — Symbol for the _RANGE\_BOUNDARIES_
- `_`: Map preview — Symbol for _CURRENT\_COMMAND_
- `^`: Map preview — Symbol for _NEXT\_COMMAND_
- The remaining sequence of letter pairs —`(UPPERCASE, lowercase)`— represent the possible symbols for the different AIs
  - `G g`: Symbols for the first AI where:
    - uppercase letter —`G`— represents one of its commands
    - lowercase letter —`g`— represents a bomb placed by this AI
  - and so on with the consequent sequence of pairs

A bomb is one of these three commands:
1. `STOP`
2. `JMP 0 X` (where `X` is a don't care)
3. `JMZ 0 0`

#### Example Corresponding Map Preview
Each character of this string represents a memory slot (except where `?` show which are simply indicators for the range).
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
> show-memory 16
G^Gg###g###ggb##?_BBB#####b?###b##
_ 16: STOP | 13 | 45
B 17: MOV_I | 2 | -1
B 18: JMP | -2 | 0
B 19: STOP | 13 | 37
# 20: STOP | 0 | 0
# 21: STOP | 0 | 0
# 22: STOP | 0 | 0
# 23: STOP | 0 | 0
# 24: STOP | 0 | 0
b 25: STOP | 13 | 37
</pre>
</div>

### User Commands
- #### Before the Game Starts:
  - **`add-ai [AI_Name] [Commands]`**  
    Add a new AI with the specified name and command sequence.  
    - **Commands Format**:  
      `COMMAND_1,ARG_1_1,ARG_1_2,COMMAND_2,ARG_2_1,ARG_2_2,...`
  
  - **`remove-ai [AI_Name]`**  
    Remove the AI with the specified name.
  
  - **`set-init-mode [Mode]`**  
    Set the initialization mode for the game.  
    - **Modes**:  
      - `INIT_MODE_STOP`  
      - `INIT_MODE_RANDOM [Seed]`  
  
  - **`start-game [AI_List]`**  
    Start a game with the specified list of AIs.  
    - **AI List Format**: AIs should be separated by spaces.  
    - Optionally, you can use `#[Number]` to specify multiple instances of the same AI.
      - e.g. `> start-game Dwarf#2 Sleepy`


- #### During the Game:
  - **`next [Iterations]`**  
    Execute the next specified number of iterations in the game.
  
  - **`show-ai [AI_Name]`**  
    Display the status and next command of the specified AI.
  
  - **`show-memory [Optional_Index]`**  
    Display the current state of the memory.  
    - Optionally, specify an index to display 10 memory cells starting from that position.
  
  - **`end-game`**  
    End the current game.
  
  - **`quit`**  
    Quit the program.

---

### AI Commands

Every memory cell is represented as follows:
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
NAME [Entry_A] [Entry_B]
</pre>
</div>

where `Entry_B` is considered the "value" of that memory cell.
The second entry, or both entries, may be useless/irrelevant to the functionality of the command itself.

#### An AI can do any of the following commands:
- `STOP`<a name="stop-command"></a>: Stops the AI.
- `MOV_R [Source] [Target]`: Relative move command that copies a value from source memory cell to target cell.
  - e.g. For a command in `memory cell 5`:
    - `MOV_R -3 1`: Copies contents of `memory cell 2` into `memory cell 6`
- `MOV_I [Source] [Entry_B]`: Moves a value indirectly from source memory cell. The target cell is not given directly using `Entry_B`.
  <div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
  <pre>
  0 | STOP 1 3
  1 | STOP 7 8
  2 | → MOV_I 0 -2
  3 | STOP 13 37
  </pre>
  </div>
  
  In this example, the `MOV_I` command uses the first entry to determine the source memory cell. In this case it is `2 + 0 = 2` (so it will copy its own content).
  
  The target cell is then obtained by, first, going to a relative memory cell using `Entry_B`. Thus by looking at cell `2 + (-2) = 0` memory cell 0 holds this command `STOP 1 3`; 

  The **value** (second entry) of `memory cell 0` is `3` meaning the target memory cell is `memory cell 3`.
  
  So the action of this command is to copy the contents of `memory cell 2` into `memory cell 3`;

- `ADD`: Adds a value to the target.
- `ADD_R`: Adds a value to the target register.
- `JMP`: Jumps to a specific memory cell.
- `JMZ`: Jumps if the target is zero.
- `CMP`: Compares two values.
- `SWAP`: Swaps two values.

### Example Usage

```sh
add-ai Dwarf ADD_R,4,3,MOV_I,2,2,JMP,-2,0,STOP,0,0
add-ai Sleepy ADD,10,-1,MOV_I,2,-1,JMP,-2,0,STOP,13,37
set-init-mode INIT_MODE_STOP
start-game Dwarf Sleepy
show-memory
next 50
show-memory 16
end-game
```
