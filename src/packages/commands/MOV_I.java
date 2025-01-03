package packages.commands;
import packages.AI;

public class MOV_I extends Command {

    public MOV_I(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "MOV_I";
    }

    public void execute(AI myAI) {
        int sourceIndex = myAI.getMemoryCellIndex(source);
        int intermediateIndex = myAI.getMemoryCellIndex(target);

        Command[] memory = AI.memory;
        Command intermediate = memory[intermediateIndex];
        int targetIndex = AI.getMemoryCellIndex(intermediateIndex, intermediate.target);

        memory[targetIndex] = AI.copyMemoryCell(sourceIndex);

        myAI.applySymbol(targetIndex);
        myAI.incrementProgramCounter();
    }
}