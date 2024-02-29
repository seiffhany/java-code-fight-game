package packages;
import packages.commands.*;
import java.util.ArrayList;
import java.util.Random;

enum CommandType {
    STOP, MOV_R, MOV_I, ADD, ADD_R, JMP, JMZ, CMP, SWAP
}

public class AI {
    public static Command[] memory = new Command[32];;

    public String status = "INVALID";
    private String[] symbols;
    private String name;
    private ArrayList<Command> commands;

    private int programCounter;
    private int steps;

    public AI(){}

    public AI(String config) {
        this.commands = new ArrayList<>();
        this.steps = 0;
        String[] split = config.split(" ");

        this.name = split[0];
        String[] commandsTemp = split[1].split(",");

        for (int i = 0; i < commandsTemp.length; i += 3) {
            String typeString = commandsTemp[i];
            try {
                CommandType type = CommandType.valueOf(typeString);
                int source = Integer.parseInt(commandsTemp[i + 1]);
                int target = Integer.parseInt(commandsTemp[i + 2]);
                switch (type) {
                    case STOP:
                        commands.add(new STOP(source, target));
                        break;
                    case MOV_R:
                        commands.add(new MOV_R(source, target));
                        break;
                    case MOV_I:
                        commands.add(new MOV_I(source, target));
                        break;
                    case ADD:
                        commands.add(new ADD(source, target));
                        break;
                    case ADD_R:
                        commands.add(new ADD_R(source, target));
                        break;
                    case JMP:
                        commands.add(new JMP(source));
                        break;
                    case JMZ:
                        commands.add(new JMZ(source, target));
                        break;
                    case CMP:
                        commands.add(new CMP(source, target));
                        break;
                    case SWAP:
                        commands.add(new SWAP(source, target));
                        break;
                }
                this.status = "RUNNING";
            } catch (NumberFormatException e2) {
                System.out.println("All command entries should be integers");
            } catch (IllegalArgumentException e1) {
                System.out.println(typeString + " is not a proper command type.");
            }
        }
    }

    public static boolean isValidCommand(String value) {
        try {
            CommandType.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void execute() {
        memory[programCounter].execute(this);
        this.steps++;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSteps() {return this.steps;}

    public String getDefaultSymbol() {
        return symbols[0];
    }

    public String getBombSymbol() {
        return symbols[1];
    }

    public void setSymbols(String[] symbols) {
        this.symbols = symbols;
    }

    public void applySymbol(int cellIndex) {
        Command command = memory[cellIndex];
        if (isBomb(command)) command.setSymbol(getBombSymbol());
        else command.setSymbol(getDefaultSymbol());
    }

    public static void setMemorySize(int size) {
        memory = new Command[size];
    }

    public static boolean isBomb(Command command) {
        boolean isSTOP = command instanceof STOP;
        boolean isJMP = command instanceof JMP && command.source == 0;
        boolean isJMZ = command instanceof JMZ && command.source == 0 && command.target == 0;
        return isSTOP || isJMP || isJMZ;
    }

    public static Command copyMemoryCell(int memoryCellIndex) {
        Command memoryCell = memory[memoryCellIndex];
        Command newCommand;
        if (memoryCell instanceof STOP) newCommand = new STOP(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof MOV_R) newCommand = new MOV_R(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof MOV_I) newCommand = new MOV_I(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof ADD) newCommand = new ADD(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof ADD_R) newCommand = new ADD_R(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof JMP) newCommand = new JMP(memoryCell.source);
        else if (memoryCell instanceof JMZ) newCommand = new JMZ(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof CMP) newCommand = new CMP(memoryCell.source, memoryCell.target);
        else if (memoryCell instanceof SWAP) newCommand = new SWAP(memoryCell.source, memoryCell.target);
        else return null;
        newCommand.setSymbol(memoryCell.getSymbol());
        return newCommand;
    }

    public static Command generateRandomCommand(Random random) {
        int source = random.nextInt(AI.memory.length * 2) - AI.memory.length;
        int commandID = random.nextInt(9);
        int target = random.nextInt(AI.memory.length * 2) - AI.memory.length;
        return switch (commandID) {
            case 0 -> new STOP(source, target);
            case 1 -> new MOV_R(source, target);
            case 2 -> new MOV_I(source, target);
            case 3 -> new ADD(source, target);
            case 4 -> new ADD_R(source, target);
            case 5 -> new JMP(source);
            case 6 -> new JMZ(source, target);
            case 7 -> new CMP(source, target);
            case 8 -> new SWAP(source, target);
            default -> null;
        };
    }

    public ArrayList<Command> getAllCommands() {
        return commands;
    }

    public Command getCurrentCommand() {
        return AI.memory[this.programCounter];
    }

    public Command getCommand(int index) {
        return this.commands.get(index);
    }

    public void setProgramCounter(int value) {
        this.programCounter = value;
    }

    public void incrementProgramCounter() {
        if (this.programCounter + 1 >= memory.length) {
            this.programCounter = 0;
        } else {
            this.programCounter++;
        }
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getMemoryCellIndex(int shift) {
        return getMemoryCellIndex(programCounter, shift);
    }

    public static int getMemoryCellIndex(int startIndex, int shift) {
        int resultIndex = startIndex + shift;

        if (resultIndex < 0) {
            return (resultIndex + memory.length) % memory.length;
        } else if (resultIndex >= memory.length) {
            return resultIndex % memory.length;
        } else {
            return resultIndex;
        }
    }

    public String toString() {
        String output = this.name + "(" + this.status + "@" + this.steps + ")";
        if (this.status.equals("RUNNING")) {
            output += "\nNext Command: " + this.getCurrentCommand() + " @" + this.programCounter;
        }
        return output;
    }
}

/* AI BOMBS
a) STOP Command. (STOP _ _)
b) JMP with entryA = 0. (JMP 0 _)
c) JMZ with both entries = 0. (JMZ 0 0)
*/
