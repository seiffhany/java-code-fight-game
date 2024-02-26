package packages;
import packages.commands.Command;
import packages.commands.MOV_R;

import java.util.ArrayList;

enum CommandType {
    MOV_R, MOV_I, ADD
}

public class AI {
    public static Command[] memory;
    private final String name;
    private final ArrayList<Command> commands;
    private int programCounter;

    public AI(String config) {
        // [Sleepy, MOV_R,5,2,ADD,10,-4]
        memory = new Command[32];
        this.commands = new ArrayList<>();
        this.name = config.split(" ")[0];

        String[] commandsTemp = config.split(" ")[1].split(",");
        for (int i = 0; i < commandsTemp.length; i += 3) {
            // MOV_R,5,2
            String typeString = commandsTemp[i];
            try {
                CommandType type = CommandType.valueOf(typeString);
                int source = Integer.parseInt(commandsTemp[i + 1]);
                int target = Integer.parseInt(commandsTemp[i + 2]);
                switch (type) {
                    case MOV_R:
                        commands.add(new MOV_R(source, target));
                        break;
                    case MOV_I:
                        // create MOV_I object
                        // insert into the commands array
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(typeString + " is not a proper command type.");
            }
        }
    }

    public void setMemorySize(int size) {
        if (size < 7 || size > 1337) {
            System.out.println("Size should be between 7 and 1337");
        }
        memory = new Command[size];
    }

    public void execute() {
        memory[programCounter].execute(this);
    }

    public String getName() {
        return name;
    }

    public void setProgramCounter(int value) {
        this.programCounter = value;
    }

    public int getMemoryCellIndex(int shift) {
        int resultIndex = programCounter + shift;
        if (resultIndex < 0) {
            return memory.length + resultIndex;
        }
        if (resultIndex >= memory.length) {
            return resultIndex - memory.length;
        }
        return resultIndex;
    }
}
