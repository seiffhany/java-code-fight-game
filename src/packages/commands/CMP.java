package packages.commands;
import packages.AI;
import packages.Engine;

public class CMP extends Command {

    public CMP(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public String getName() {
        return "CMP";
    }

    public void execute(AI myAI) {
        int firstIndex = myAI.getMemoryCellIndex(source);
        int secondIndex = myAI.getMemoryCellIndex(target);

        int firstNum = AI.memory[firstIndex].source;
        int secondNum = AI.memory[secondIndex].target;

        Engine.goNext(myAI);
        // myAI.incrementProgramCounter();
        if (firstNum != secondNum) {
            // skip next command
            Engine.goNext(myAI);
            // myAI.incrementProgramCounter();
        }
    }
}