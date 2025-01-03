package packages.commands;
import packages.AI;

public class SWAP extends Command {

    public SWAP(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "SWAP";
    }

    public void execute(AI myAI) {
        int firstIndex = myAI.getMemoryCellIndex(source);
        int secondIndex = myAI.getMemoryCellIndex(target);

        Command firstCommand = AI.memory[firstIndex];
        Command secondCommand = AI.memory[secondIndex];

        int temp = firstCommand.source;
        firstCommand.source = secondCommand.target;
        secondCommand.target = temp;

        myAI.applySymbol(firstIndex);
        myAI.applySymbol(secondIndex);
        myAI.incrementProgramCounter();
    }
}