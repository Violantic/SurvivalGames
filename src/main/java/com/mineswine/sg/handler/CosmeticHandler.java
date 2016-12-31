package com.mineswine.sg.handler;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.cosmetic.Cosmetic;
import com.mineswine.sg.game.util.CloakUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 12/23/2016.
 */
public class CosmeticHandler implements Runnable {

    private SurvivalGames instance;

    public CosmeticHandler(SurvivalGames instance) {
        this.instance = instance;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            CloakUtil.displayCloak(player, Cosmetic.LEGENDARY_CAPE);
            CloakUtil.displayHead(player);
        }
    }
}
