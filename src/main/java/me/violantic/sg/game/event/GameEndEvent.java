package me.violantic.sg.game.event;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.Game;
import me.violantic.sg.game.util.ChatUtil;
import me.violantic.sg.game.util.TitleUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Arrays;

/**
 * Created by Ethan on 11/10/2016.
 */
public class GameEndEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Game game;
    public GameEndEvent(Game game, String winner) {
        this.game = game;
        for (Player online : SurvivalGames.getInstance().getServer().getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            TitleUtil.sendTitle(online, 10, 20 * 5, 10, ChatColor.YELLOW + SurvivalGames.getInstance().getPrefix(), ChatColor.RESET + Arrays.asList(SurvivalGames.getInstance().getMap().getCreators()).toString());
            online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
            online.sendMessage("");
            ChatUtil.sendCenteredMessage(online, SurvivalGames.getInstance().getPrefix());
            online.sendMessage("");
            online.sendMessage(ChatColor.YELLOW + "The winner is " + winner);
            online.sendMessage("");
            online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        }
    }

    public Game getGame() {
        return game;
    }

}
