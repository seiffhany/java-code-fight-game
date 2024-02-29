package packages;

import packages.commands.Command;
import packages.commands.STOP;

import java.util.*;

public class Engine {
    public static Map<String, String> createdAIs;
    public static Map<String, AI> competingAIs;
    public static ArrayList<AI> stoppedAIs;
    public static ArrayList<AI> turnOrder;
    public static int turnIndex = 0;

    public String initMode = "STOP";
    public boolean gameStarted;

    String SYMBOL_NOT_PROCESSED;
    String SYMBOL_RANGE_BOUNDARIES;
    String SYMBOL_CURR_COMMAND;
    String SYMBOL_NEXT_COMMAND;
    ArrayList<String[]> SYMBOL_PER_AI;

    private Random rand;

    public Engine() {
        createdAIs = new HashMap<String, String>();
        SYMBOL_PER_AI = new ArrayList<String[]>();

        Scanner sc = new Scanner(System.in);
        boolean validInit;
        do {
            System.out.print("\n%> java CodeFight ");
            String config = sc.nextLine();
            validInit = init(config);
        } while (!validInit);
        System.out.println("Welcome to CodeFight 2024. Enter 'help' for more details.");
    }

    public boolean init(String config) {
        String[] parsed = config.split(" ", 6);

        if (parsed.length < 6) {
            System.out.println("Missing arguments. Expected 6 but found " + parsed.length);
            return false;
        }
        String[] symbolsTemp = parsed[5].split(" ");

        try {
            int memSize = Integer.parseInt(parsed[0]);
            if (memSize < 7 || memSize > 1337) {
                System.out.println("Memory size should be a number between 7 and 1337");
                return false;
            }
            AI.setMemorySize(memSize);
        } catch (NumberFormatException e) {
            System.out.println("Memory size should be a number");
        }

        if (symbolsTemp.length % 2 == 1) {
            System.out.println("A symbol for an AI is missing");
            return false;
        }
        for (int i = 0; i < symbolsTemp.length; i += 2) {
            SYMBOL_PER_AI.add(new String[]{symbolsTemp[i], symbolsTemp[i + 1]});
        }

        SYMBOL_NOT_PROCESSED = parsed[1];
        SYMBOL_RANGE_BOUNDARIES = parsed[2];
        SYMBOL_CURR_COMMAND = parsed[3];
        SYMBOL_NEXT_COMMAND = parsed[4];
        return true;
    }

    public int getCurrentCommandIndex() {
        if (turnOrder.isEmpty()) return -1;
        return turnOrder.get(turnIndex).getProgramCounter();
    }

    public int getNextCommandIndex() {
        if (turnOrder.isEmpty()) return -1;
        int index = turnIndex + 1 >= turnOrder.size() ? 0 : turnIndex + 1;
        return turnOrder.get(index).getProgramCounter();
    }

    public static void nextAI(AI currAI) {
        String status = currAI.status;
        if (status.equals("STOPPED")) {
            if (turnIndex == turnOrder.size() - 1) turnIndex = 0;
            turnOrder.remove(currAI);
            stoppedAIs.add(currAI);
            System.out.println(currAI.getName() + " executed " + (currAI.getSteps() - 1) + " steps until stopping.");
        }
        else if(status.equals("RUNNING")) turnIndex = turnIndex + 1 >= turnOrder.size() ? 0 : turnIndex + 1;
    }

    public void next(int iterations) {
        for (int i = 0; i < iterations && !turnOrder.isEmpty(); i++) {
            AI currAI = turnOrder.get(turnIndex);
            currAI.execute();
            nextAI(currAI);
        }
    }

    public void setInitMode(String config) {
        String stopMode = "INIT_MODE_STOP";
        String randomMode = "INIT_MODE_RANDOM";

        String[] mode = config.split(" ");
        if (!mode[0].equals(randomMode) && !mode[0].equals(stopMode)) {
            System.out.println("Invalid init mode!");
            return;
        }

        if (mode[0].equals(randomMode)) {
            if (mode.length == 1 || mode[1].isEmpty()) {
                System.out.println("Please provide the seed parameter");
                return;
            }
            if (mode.length > 2) {
                System.out.println("Expected only 2 arguments but found " + mode.length);
            }
            if (!AI.isInteger(mode[1])) {
                System.out.println("Seed must be a value within the range of [-1337, 1337]");
                return;
            }
        }

        if (mode[0].equals(stopMode) && initMode.equals("STOP") ||
                mode[0].equals(randomMode) && initMode.equals("RANDOM " + mode[1])) {
            System.out.println("Init mode is already set to " + config);
            return;
        }

        System.out.println("Changed init mode from INIT_MODE_" + initMode + " to " + config);
        if (config.equals("INIT_MODE_STOP")) {
            initMode = "STOP";
        } else if (mode[0].equals(randomMode)) {
            int seed = Integer.parseInt(config.split(" ")[1]);
            initMode = "RANDOM " + seed;
            /*
            Uncomment below line if you don't want to reset the Random object every time set-init-mode INIT_MODE_RANDOM
            is called
            */
//            if (rand == null)
            rand = new Random(seed);
        }
    }

    public String printSymbol(int i) {
        int currIndex = getCurrentCommandIndex();
        int nextIndex = getNextCommandIndex();
        if (i == currIndex) return SYMBOL_CURR_COMMAND;
        else if (i == nextIndex) return SYMBOL_NEXT_COMMAND;
        else return AI.memory[i].getSymbol();
    }

    public void showMemory() {
        for (int i = 0; i < AI.memory.length; i++) {
            System.out.print(printSymbol(i));
        }
        System.out.println();
    }

    public void showMemory(int startIndex) {
        for (int i = 0; i < AI.memory.length; i++) {
            if (i == startIndex) System.out.print(SYMBOL_RANGE_BOUNDARIES);
            System.out.print(printSymbol(i));
            if (i == startIndex + 9) System.out.print(SYMBOL_RANGE_BOUNDARIES);
        }
        System.out.println();

        for (int i = startIndex; i < AI.memory.length && i < startIndex + 10; i++) {
            Command currCommand = AI.memory[i];
            System.out.println(printSymbol(i) + " " + i + ": " + currCommand);
        }
    }

    public void addAI(String config) {
        String[] split = config.split(" ");
        String name = split[0];

        if (split.length > 2) {
            System.out.println("Arguments should no contain any whitespaces");
        } else if (split.length < 2) {
            System.out.println("Missing Arguments. Expected 2 but found " + split.length);
        } else if (createdAIs.containsKey(name)) {
            System.out.println("An AI with the name '" + name + "' already exists");
        } else {
            String[] commands = split[1].split(",");
            if (commands.length % 3 != 0) {
                System.out.println("Each command should have exactly 2 integer entries");
            } else {
                for (int i = 0; i < commands.length; i += 3) {
                    if (!AI.isValidCommand(commands[i])) {
                        System.out.println(commands[i] + " is not a valid command");
                        return;
                    }
                    if (!AI.isInteger(commands[i + 1]) || !AI.isInteger(commands[i + 2])) {
                        System.out.println("All command arguments should be integers");
                        return;
                    }
                }
                createdAIs.put(name, config);
                System.out.println(name);
            }
        }
    }

    public void removeAI(String name) {
        if (createdAIs.containsKey(name)) {
            createdAIs.remove(name);
            System.out.println("Removed " + name);
        } else {
            System.out.println("There is no AI with this name");
        }
    }

    public void showAI(String name) {
        if (!competingAIs.containsKey(name)) {
            String[] parsed = name.split("#", 2);

            // Check if forgot to specify number
            if (parsed.length == 1 && competingAIs.containsKey(name + "#0")) {
                System.out.println("Please specify which " + name + " AI to show using the format -> " + name + "#[Number]");
            } else {
                System.out.println("There is no " + name + " competing");
            }
        } else {
            System.out.println(competingAIs.get(name));
        }
    }

    public void resetMemory() {
        for (int i = 0; i < AI.memory.length; i++) {
            if (initMode.equals("STOP")) {
                AI.memory[i] = new STOP(0, 0);
            } else {
                AI.memory[i] = AI.generateRandomCommand(this.rand);
            }
            AI.memory[i].setSymbol(SYMBOL_NOT_PROCESSED);
        }
    }

    private void printListContents(List<AI> list, String message, boolean reverse) {
        if (!list.isEmpty()) {
            System.out.print(message + ": ");
            if (reverse) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    String comma = i > 0 ? ", " : "";
                    System.out.print(list.get(i).getName() + comma);
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    String comma = i < list.size() - 1 ? ", " : "";
                    System.out.print(list.get(i).getName() + comma);
                }
            }
            System.out.println();
            list.clear();
        }
    }

    public void endGame() {
        this.gameStarted = false;
        printListContents(turnOrder, "Running AIs", false);
        printListContents(stoppedAIs, "Stopped AIs", true);
        resetMemory();
    }

    public void startGame(String AIs) {
        String[] competitors = AIs.split(" ");
        int extraCounter = 0;

        // VALIDATE COMPETITORS AND THEIR FORMAT
        for (String item : competitors) {
            String[] itemSplit = item.split("#", 2);
            String name = itemSplit[0];
            if (!createdAIs.containsKey(name)) {
                System.out.println(name + " does not exist");
                return;
            }
            if (itemSplit.length == 2) {
                if (!AI.isInteger(itemSplit[1])) {
                    System.out.println("Please only specify a numerical value after the '#'");
                    return;
                } else {
                    extraCounter += Integer.parseInt(itemSplit[1]) - 1;
                }
            }
        }

        if (competitors.length + extraCounter > SYMBOL_PER_AI.size()) {
            System.out.println("You cannot compete with more than " + SYMBOL_PER_AI.size() + " AIs at once.");
            return;
        }

        if (competitors.length <= 1) {
            System.out.println("You cannot compete with less than 2 AIs");
            return;
        }

        // SET UP TURN ORDER
        competingAIs = new HashMap<String, AI>();
        stoppedAIs = new ArrayList<AI>();
        turnOrder = new ArrayList<AI>(competitors.length);
        for (String key : competitors) {
            String[] keySplit = key.split("#");

            String name = keySplit[0];
            String aiConfig = createdAIs.get(name);

            if (keySplit.length == 2) {
                int count = Integer.parseInt(keySplit[1]);
                for (int i = 0; i < count; i++){
                    AI newAI = new AI(aiConfig);
                    String currName = name + "#" + i;
                    newAI.setName(currName);
                    turnOrder.add(newAI);
                    competingAIs.put(currName, newAI);
                }
            } else {
                AI newAI = new AI(aiConfig);
                turnOrder.add(newAI);
                competingAIs.put(name, newAI);
            }
        }

        // INITIALIZE DEFAULT MEMORY CELLS FROM INIT MODE
        resetMemory();

        // LOAD MEMORY WITH AI COMMANDS
        int memPerAI = AI.memory.length / turnOrder.size();
        int counter = 0;
        for (AI currAI : turnOrder) {
            int startMem = counter * memPerAI;
            int endMem = Math.min(startMem + memPerAI, AI.memory.length);

            currAI.setProgramCounter(startMem);
            currAI.setSymbols(SYMBOL_PER_AI.get(counter++));

            for (int i = startMem; i < endMem && i - startMem < currAI.getAllCommands().size(); i++) {
                Command currCommand = currAI.getCommand(i - startMem);
                currCommand.setSymbol(currAI.getDefaultSymbol());
                AI.memory[i] = currCommand;
            }
        }

        this.gameStarted = true;
        System.out.println("Game Started.");
    }

    public boolean checkGameStart(boolean status, String message) {
        if (this.gameStarted == status) {
            System.out.println(message);
            return true;
        }
        return false;
    }

    public void help() {
        String output = "You can run the following commands:\n";

        if (!gameStarted) {
            output += "\n- add-ai [AI_Name] [Commands]: Add a new AI with the specified name and command sequence.\n";
            output += "  Commands should be in the format COMMAND_1,ARG_1_1,ARG_1_2,COMMAND_2,ARG_2_1,ARG_2_2,...\n";

            output += "\n- remove-ai [AI_Name]: Remove the AI with the specified name.\n";

            output += "\n- set-init-mode [Mode]: Set the initialization mode for the game.\n";
            output += "  Modes can be 'INIT_MODE_STOP' or 'INIT_MODE_RANDOM [Seed]'.\n";

            output += "\n- start-game [AI_List]: Start a game with the specified list of AIs.\n";
            output += "  AIs should be separated by spaces, and optionally followed by '#[Number]'";
            output += " to specify multiple instances of the same AI.\n";
        }

        output += "\n- next [Iterations]: Execute the next specified number of iterations in the game.\n";

        output += "\n- show-ai [AI_Name]: Show the status and next command of the specified AI.\n";

        output += "\n- show-memory [Optional_Index]: Show the current state of memory.\n";
        output += "  Optionally, specify an index to display 10 memory cells starting from that position.\n";

        output += "\n- end-game: End the current game.\n";

        output += "\n- quit: Quit the program.\n";
        System.out.println(output);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Engine engine = new Engine();

        String userInput;
        do {
            System.out.print("> ");
            userInput = sc.nextLine();
            String[] parsed = userInput.split(" ", 2);

            String command = parsed[0];
            if (command.isEmpty()) continue;
            if (command.equals("quit")) break;
            if (!command.equals("show-memory") && !command.equals("end-game")
                    && !command.equals("help") && parsed.length != 2) {
                System.out.println("Missing Commands.");
            } else {
                switch (command) {
                    case "add-ai":
                        if (engine.checkGameStart(true, "Please end this game to add an AI")) break;
                        engine.addAI(parsed[1]);
                        break;
                    case "remove-ai":
                        if (engine.checkGameStart(true, "Please end this game to remove an AI")) break;
                        engine.removeAI(parsed[1].split(" ")[0]);
                        break;
                    case "show-ai":
                        if (engine.checkGameStart(false, "You haven't started a game yet.")) break;
                        engine.showAI(parsed[1]);
                        break;
                    case "show-memory":
                        if (engine.checkGameStart(false, "You haven't started a game yet.")) break;
                        if (parsed.length == 2) {
                            if (AI.isInteger(parsed[1])) {
                                int index = Integer.parseInt(parsed[1].split(" ")[0]);
                                if (index < 0 || index >= AI.memory.length) {
                                    System.out.println("Index should be in the range [0," + (AI.memory.length - 1) + "].");
                                } else {
                                    engine.showMemory(index);
                                }
                            }
                            else {
                                System.out.println("Enter a valid numerical index");
                            }
                        } else {
                            engine.showMemory();
                        }
                        break;
                    case "start-game":
                        if (engine.checkGameStart(true, "You are still currently in a game.")) break;
                        engine.startGame(parsed[1]);
                        break;
                    case "end-game":
                        if (engine.checkGameStart(false, "There is no game running")) break;
                        engine.endGame();
                        break;
                    case "set-init-mode":
                        if (engine.checkGameStart(true, "You are in the middle of a game.")) break;
                        engine.setInitMode(parsed[1]);
                        break;
                    case "next":
                        if (engine.checkGameStart(false, "You haven't started a game yet.")) break;
                        if (AI.isInteger(parsed[1])) {
                            int iterations = Integer.parseInt(parsed[1].split(" ")[0]);
                            engine.next(iterations);
                        } else {
                            System.out.println("Please enter an integer value");
                        }
                        break;
                    case "help":
                        engine.help();
                        break;
                    default:
                        System.out.println("There is no such command as '" + command + "'");
                        break;
                }
            }
        } while (true);
        sc.close();
    }
}

/* AIs
32 # ? _ ^ G g B b
add-ai Dwarf ADD_R,4,3,MOV_I,2,2,JMP,-2,0,STOP,0,0
add-ai Sleepy ADD,10,-1,MOV_I,2,-1,JMP,-2,0,STOP,13,37
set-init-mode INIT_MODE_STOP
start-game Dwarf Sleepy
show-memory
next 50
show-memory 16

end-game

add-ai Caterpillar MOV_R,0,1
add-ai Pogo JMP,0,0
set-init-mode INIT_MODE_RANDOM 28
start-game Pogo Caterpillar
next 181
show-memory 9
show-ai Caterpillar
show-ai Pogo
 */