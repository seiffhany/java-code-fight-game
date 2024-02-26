package commands;

public class MOV_R implements Command {
    int argumentA;
    int argumentB;

    public MOV_R(int argumentA, int argumentB) {
        this.argumentA = argumentA;
        this.argumentB = argumentB;
    }

    public Integer execute(AI myAI) {
        int sourceIndex = myAI.getMemoryCellIndex(this.argumentA);
        int targetIndex = myAI.getMemoryCellIndex(this.argumentB);

        Command[] memory = AI.memory;
        memory[sourceIndex] = memory[targetIndex];
        return null;
    }
}
