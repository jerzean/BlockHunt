package nl.steffion.blockhunt.Taunt;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SimpleSoundTaunt implements TauntSupplier {
    private final Sound sound;
    private final float pitch;
    private final float volume;

    public SimpleSoundTaunt(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    @Override
    public void accept(Player player) {
        World world = player.getWorld();
        world.playSound(player.getLocation(), sound, SoundCategory.PLAYERS, volume, pitch);
    }
}
