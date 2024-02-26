package packages.commands;
import packages.AI;

public class MOV_R extends Command {

    public MOV_R(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public void execute(AI myAI) {
        int sourceIndex = myAI.getMemoryCellIndex(source);
        int targetIndex = myAI.getMemoryCellIndex(target);

        Command[] memory = AI.memory;
        memory[targetIndex] = memory[sourceIndex];
    }
}