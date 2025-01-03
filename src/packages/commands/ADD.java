package packages.commands;
import packages.AI;

public class ADD extends Command {

    public ADD(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "ADD";
    }

    public void execute(AI myAI) {
        this.target += this.source;
        myAI.incrementProgramCounter();
    }
}