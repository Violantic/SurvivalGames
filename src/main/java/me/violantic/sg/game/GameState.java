package me.violantic.sg.game;

/**
 * Created by Ethan on 11/3/2016.
 */
public class GameState {

    private String name;
    private boolean canTalk;
    private boolean canPVP;
    private boolean canPVE;
    private boolean canBreak;
    private boolean canPlace;
    private boolean canOpen;
    private boolean canMove;

    public GameState(String name) {
        this.name = name;
        this.canTalk = true;
        this.canPVP = false;
        this.canPVE = false;
        this.canBreak = false;
        this.canPlace = false;
        this.canOpen = false;
        this.canMove = false;
    }

    public String getName() {
        return name;
    }

    public boolean isCanTalk() {
        return canTalk;
    }

    public boolean isCanPVP() {
        return canPVP;
    }

    public boolean isCanPVE() {
        return canPVE;
    }

    public boolean isCanBreak() {
        return canBreak;
    }

    public boolean isCanPlace() {
        return canPlace;
    }

    public boolean isCanOpen() {
        return canOpen;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanTalk(boolean canTalk) {
        this.canTalk = canTalk;
    }

    public void setCanPVP(boolean canPVP) {
        this.canPVP = canPVP;
    }

    public void setCanPVE(boolean canPVE) {
        this.canPVE = canPVE;
    }

    public void setCanBreak(boolean canBreak) {
        this.canBreak = canBreak;
    }

    public void setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
    }

    public void setCanOpen(boolean canOpen) {
        this.canOpen = canOpen;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
