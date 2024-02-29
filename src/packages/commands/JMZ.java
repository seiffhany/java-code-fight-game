package packages.commands;
import packages.AI;

public class JMZ extends Command {
    public JMZ(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "JMZ";
    }

    public void execute(AI myAI) {
        int targetIndex = myAI.getMemoryCellIndex(source);

        int checkCellIndex = myAI.getMemoryCellIndex(target);
        Command checkCell = AI.memory[checkCellIndex];

        if (checkCell.target == 0) {
            myAI.setProgramCounter(targetIndex);
        } else {
            myAI.incrementProgramCounter();
        }
    }
}