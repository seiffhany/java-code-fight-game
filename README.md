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
Users interact with the game using predefined commands ([more on user commands](#user-commands)):
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

A bomb is one of these three commands (more details on AI commands [here](#ai-commands)):
1. `STOP X X` | _where `X` is a don't care_
2. `JMP 0 X` | _where `X` is a don't care_
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
    - **`[Commands]` Format**:  
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

AI Commands are essentially what's stored in the memory. Each memory cell is represented as follows:
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
NAME [Entry_A] [Entry_B]
</pre>
</div>

where `Entry_B` is considered the "value" of that memory cell.
The second entry, or both entries, may be useless/irrelevant to the functionality of the command itself.

#### An AI can do any of the following commands:
- `STOP`<a id="stop-command"></a>: Stops the AI.
- `MOV_R [Source_R] [Target_R]`: Relative move command that copies a value from source memory cell to target memory cell. `Source_R` and `Target_R` are relative to the current memory cell.
  - e.g. For a command in `memory cell 5`:
    - `MOV_R -3 1`: Copies contents of `memory cell 2` into `memory cell 6`
- `MOV_I [Source_R] [Target_I]`: Moves a value from relative source memory cell into the target cell which is given indirectly by `Target_I`.
  <div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
  <pre>
  0 | STOP 1 3
  1 | STOP 7 8
  2 | → MOV_I 0 -2
  3 | STOP 13 37
  </pre>
  </div>
  
  In this example, the `MOV_I` command uses the first entry to determine the source memory cell. In this case it is `2 + 0 = 2` (so it will copy its own content).
  
  The target cell is then obtained by, first, going to a relative memory cell using `Target_I`. Thus by looking at cell `2 + (-2) = 0` memory cell 0 holds this command `STOP 1 3`; 

  The **value** (second entry) of `memory cell 0` is `3` meaning the target memory cell is `memory cell 3`.
  
  So the action of this command is to copy the contents of `memory cell 2` into `memory cell 3`;

- `ADD [Val_A] [Val_B]`: Adds the values of `Val_A` and `Val_B` | result is saved in Entry_B.
- `ADD_R [Val_A] [Target_R]`: Adds the values of `Val_A` and Entry_B of memory cell given by `Target_R` | result is saved in Entry_B of the target.
- `JMP [Target_R]`: Jumps to the memory cell specified by the value of `Target_R`.
- `JMZ [Target_R] [CheckCell_R]`: Jumps to target memory cell given by `Target_R` if Entry_B value of relative memory cell given by `CheckCell_R` is zero.
- `CMP [First_R] [Second_R]`: Skips next AI commands if Entry_A of `First` and Entry_B of `Second` are **not equal**.
- `SWAP [First_R] [Second_R]`: Swaps Entry_A of `First` with Entry_B of `Second`.

---

## Example Interaction

### Example AIs
#### 1. Dwarf
```
add-ai Dwarf ADD_R,4,3,MOV_I,2,2,JMP,-2,0,STOP,0,0
```
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
ADD_R 4 3
MOV_I 2 2
JMP -2 0
STOP 0 0
</pre>
</div>

#### 2. Sleepy
```
add-ai Sleepy ADD,10,-1,MOV_I,2,-1,JMP,-2,0,STOP,13,37
```
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
ADD 10 -1
MOV_I 2 -1
JMP -2 0
STOP 13 37
</pre>
</div>

#### 3. Caterpillar
```
add-ai Caterpillar MOV_R,0,1
```
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
MOV_R 0 1
</pre>
</div>

#### 4. Pogo
```
add-ai Pogo JMP,0,0
```
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
JMP 0 0
</pre>
</div>
  
### Example Full Interaction
<div style="user-select: none; -webkit-user-select: none; -ms-user-select: none;">
<pre>
 1 | %> java CodeFight 32 # ? _ ^ G g B b  
 2 | Welcome to CodeFight 2024. Enter'help' for more details.  
 3 | > add-ai Dwarf ADD_R,4,3,MOV_I,2,2,JMP,-2,0,STOP,0,0  
 4 | Dwarf  
 5 | > add-ai Sleepy ADD,10,-1,MOV_I,2,-1,JMP,-2,0,STOP,13,37  
 6 | Sleepy  
 7 | > start-game Dwarf Sleepy  
 8 | Game started.  
 9 | > show-memory  
10 | _GGG############^BBB############  
11 | > next 50  
12 | Sleepy executed 12 steps until stopping.  
13 | Dwarf executed 24 steps until stopping.  
14 | > show-memory 16  
15 | gGGg###g###ggb##?gBBBg###gb?##gb##  
16 | g 16: STOP | 13 | 45  
17 | B 17: MOV_I | 2 | -1  
18 | B 18: JMP | -2 | 0  
19 | B 19: STOP | 13 | 37  
20 | g 20: STOP | 13 | 49  
21 | # 21: STOP | 0 | 0  
22 | # 22: STOP | 0 | 0  
23 | # 23: STOP | 0 | 0  
24 | g 24: STOP | 13 | 53  
25 | b 25: STOP | 13 | 37  
26 | > end-game  
27 | Stopped AIs: Dwarf, Sleepy  
28 | > add-ai Caterpillar MOV_R,0,1  
29 | Caterpillar  
30 | > add-ai Pogo JMP,0,0  
31 | Pogo  
32 | > set-init-mode INIT_MODE_RANDOM 28  
33 | Changed init mode from INIT_MODE_STOP to INIT_MODE_RANDOM 28  
34 | > start-game Pogo Caterpillar  
35 | Game started.  
36 | > next 181  
37 | > show-memory 9  
38 | BBBBBBBBB?B_^BBBBBBB?BBBBBBBBBBBBB  
39 | B 9: MOV_R | 0 | 1  
40 | _ 10: MOV_R | 0 | 1  
41 | ^ 11: MOV_R | 0 | 1  
42 | B 12: MOV_R | 0 | 1  
43 | B 13: MOV_R | 0 | 1  
44 | B 14: MOV_R | 0 | 1  
45 | B 15: MOV_R | 0 | 1  
46 | B 16: MOV_R | 0 | 1  
47 | B 17: MOV_R | 0 | 1  
48 | B 18: MOV_R | 0 | 1  
49 | > show-ai Caterpillar  
50 | Caterpillar (RUNNING@90)  
51 | Next Command: MOV_R|0|1 @10  
52 | > show-ai Pogo  
53 | Pogo (RUNNING@91)  
54 | Next Command: MOV_R|0|1 @11  
55 | > end-game  
56 | Running AIs: Pogo, Caterpillar  
57 | > quit  

</pre>
</div>
  
