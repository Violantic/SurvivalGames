package com.mineswine.sg.game.cosmetic;

/**
 * Created by Ethan on 12/19/2016.
 */
public enum Cosmetic {

    LEGENDARY_CAPE("Legendary Cape", new String[]{"Cape for everyone who has ever reached 2000 rating!"}, 1);

    private String name;
    private String[] desc;
    private int id;

    Cosmetic(String name, String[] desc, int id) {
        this.name = name;
        this.desc = desc;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String[] getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public static Cosmetic valueOf(int id) {
        for(Cosmetic c : values()) {
            if(c.getId() == id) {
                return c;
            }
        }

        return null;
    }
}
