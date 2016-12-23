package com.mineswine.sg.game.util;

import com.mineswine.sg.game.loot.ChestContent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ethan on 11/23/2016.
 */
public class LootUtil {

    public static List<ChestContent> getRandomContents(ChestContent.Tier t) {
        List<ChestContent> contents = new ArrayList<ChestContent>();
        switch (t) {
            case I:
                // Add ten items no matter the percentage. //
                for (int i = 0; i < 5; i++) {
                    int p = ThreadLocalRandom.current().nextInt(ChestContent.values().length) + 1;
                    ChestContent item = ChestContent.get(p);

                    int chance = ThreadLocalRandom.current().nextInt(100) + 1;
                    if (item != null) {
                        if (chance <= item.getChance()) {
                            contents.add(item);
                        }
                    }
                }
                break;
            case II:
                // Add 10 items for all items 40% or rarer. //
                List<ChestContent> possible = new ArrayList<ChestContent>();
                for (ChestContent rare : ChestContent.values()) {
                    if (rare.getChance() <= 40.0D) {
                        possible.add(rare);
                    }
                }

                // Harder to get rarer items (amt/100 < amt/40). //
                for (ChestContent rare : possible) {
                        int p = ThreadLocalRandom.current().nextInt(100) + 1;
                        if(rare.getChance() >= p) {
                            contents.add(rare);
                        }
                }
                break;
        }

        return contents;
    }

    public static int[][] p(int f, int i) {
        return new int[][]{
        };
    }

}
