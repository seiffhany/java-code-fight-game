package commands;

import java.util.ArrayList;

enum CommandType {
    MOV_R, MOV_I, ADD
}

public class AI {
    static Command[] memory;
    private final String name;
    private final ArrayList<Command> commands;
    private int programCounter;

    public AI(String configuration) {
        this.commands = new ArrayList<Command>();
        memory = new Command[30];

        this.name = configuration.split(" ")[0];
        String[] commandsTemp  = configuration.split(" ")[1].split(",");

        for (int i = 0; i < commandsTemp.length; i += 3) {
            String typeString = commandsTemp[i];
            try {
                CommandType type = CommandType.valueOf(typeString);
                int argumentA;
                int argumentB;
                switch (type) {
                    case MOV_R:
                        // create a MOV_R command
                        argumentA = Integer.parseInt(commandsTemp[i + 1]);
                        argumentB = Integer.parseInt(commandsTemp[i + 2]);
                        commands.add(new MOV_R(argumentA, argumentB));
                        break;
                    default:
                        break;
                }
                System.out.println(typeString + " is an enum value.");
            } catch (IllegalArgumentException e) {
                System.out.println(typeString + " is not a proper command type.");
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public static void setMemorySize(int size) {
        memory = new Command[size];
    }

    public void setProgramCounter(int value) {
        this.programCounter = value;
    }

    public Integer execute() {
        return commands.get(programCounter).execute(this);
    }

    public int getMemoryCellIndex(int shift) {
        int resultIndex = programCounter + shift;
        if (resultIndex < 0) {
            return commands.size() + resultIndex;
        }
        if (resultIndex >= commands.size()) {
            return resultIndex - commands.size();
        }
        return resultIndex;
    }
}
