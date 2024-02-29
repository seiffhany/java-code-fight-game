package packages.commands;
import packages.AI;

public class STOP extends Command {
    public STOP(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "STOP";
    }

    public void execute(AI myAI) {
        myAI.status = "STOPPED";
    }
}