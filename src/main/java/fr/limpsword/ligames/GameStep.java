package fr.limpsword.ligames;

public enum GameStep {

    VOID("void"),
    STARTING("step_starting"),
    WAITING("step_waiting"),
    PREPARING("step_preparing"),
    IN_GAME("step_ingame"),
    FINISH("step_finish");

    private final String transName;

    GameStep(String transName) {
        this.transName = transName;
    }
}
