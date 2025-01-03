package packages.commands;
import packages.AI;

public class JMP extends Command {
    public JMP(int source) {
        this.source = source;
        this.target = 0;
    }

    public String getName() {
        return "JMP";
    }

    public void execute(AI myAI) {
        int targetIndex = myAI.getMemoryCellIndex(source);
        myAI.setProgramCounter(targetIndex);
    }
}