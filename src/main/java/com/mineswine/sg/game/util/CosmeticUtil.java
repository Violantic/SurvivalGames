package com.mineswine.sg.game.util;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.cosmetic.Cosmetic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Ethan on 12/19/2016.
 */
public class CosmeticUtil {

    private MysqlUtil util;

    public CosmeticUtil(MysqlUtil util) {
        this.util = util;
    }

    public MysqlUtil getUtil() {
        return util;
    }

    public void invokeGameFinishRatingUpdate(String uuid) {
        if(getUtil().getStat(uuid, "rating") >= 2000 && (!getUtil().hasCosmetic(uuid, Cosmetic.LEGENDARY_CAPE))) {
            getUtil().giveCosmeticIfNotExists(uuid, Cosmetic.LEGENDARY_CAPE);

            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
            player.sendMessage("");
            ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
            player.sendMessage("");
            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "You have reached a rating of " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "2000" + ChatColor.RESET);
            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "A " + ChatColor.LIGHT_PURPLE + Cosmetic.LEGENDARY_CAPE.getName() + ChatColor.RESET + "" + ChatColor.YELLOW + " has been put in your cosmetics!");

            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        }
    }

}
