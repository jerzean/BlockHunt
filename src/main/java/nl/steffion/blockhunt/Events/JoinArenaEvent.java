package nl.steffion.blockhunt.Events;

import nl.steffion.blockhunt.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JoinArenaEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player = null;
    private Arena arena = null;

    public JoinArenaEvent(Player player, Arena arena) {
        this.player = player;
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
