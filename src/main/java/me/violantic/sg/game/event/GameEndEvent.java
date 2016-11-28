package me.violantic.sg.game.event;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.Game;
import me.violantic.sg.game.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ethan on 11/10/2016.
 */
public class GameEndEvent extends Event{

    private static final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private String winner;
    private Game game;
    public GameEndEvent(Game game, String winner) {
        this.winner = winner;
        this.game = game;
        for (Player online : SurvivalGames.getInstance().getServer().getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            //TitleUtil.sendTitle(online, 10, 20 * 5, 10, ChatColor.YELLOW + SurvivalGames.getInstance().getPrefix(), ChatColor.RESET + Arrays.asList(SurvivalGames.getInstance().getMap().getCreators()).toString());
            online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
            online.sendMessage("");
            ChatUtil.sendCenteredMessage(online, SurvivalGames.getInstance().getPrefix());
            online.sendMessage("");
            ChatUtil.sendCenteredMessage(online, ChatColor.YELLOW + "The winner is " + winner);
            online.sendMessage("");
            online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        }
    }

    public String getWinner() {
        return winner;
    }

    public Game getGame() {
        return game;
    }

}
