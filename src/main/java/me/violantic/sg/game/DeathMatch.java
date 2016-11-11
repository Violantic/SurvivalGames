package me.violantic.sg.game;

/**
 * Created by Ethan on 11/3/2016.
 */
public class DeathMatch {

    private Map arena;
    private GameState state;

    public DeathMatch(Map arena) {
        this.arena = arena;
        this.state = new GameState("deathmatch");
    }

    public Map getArena() {
        return arena;
    }

    public GameState getState() {
        return state;
    }

    public void setArena(Map arena) {
        this.arena = arena;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void forceStart() {

    }
}
