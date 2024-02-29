package packages.commands;

import packages.AI;

public abstract class Command {
    public int source;
    public int target;
    public abstract void execute(AI myAI);
    public abstract String getName();

    private String symbol;
    public String getSymbol() {
        return this.symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return this.getName() + " | " + this.source + " | " + this.target;
    }
}
