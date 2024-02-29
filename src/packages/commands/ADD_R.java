package packages.commands;
import packages.AI;

public class ADD_R extends Command {

    public ADD_R(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "ADD_R";
    }

    public void execute(AI myAI) {
        int targetIndex = myAI.getMemoryCellIndex(target);

        Command targetCommand = AI.memory[targetIndex];
        targetCommand.target += source;

        myAI.applySymbol(targetIndex);
        myAI.incrementProgramCounter();
    }
}