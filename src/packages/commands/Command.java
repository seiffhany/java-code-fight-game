package packages.commands;

import packages.AI;

public abstract class Command {
    int source;
    int target;
    public abstract void execute(AI myAI);
}
