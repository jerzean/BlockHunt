package nl.steffion.blockhunt;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class PlayerArenaData {
    public GameMode pGameMode;
    public ItemStack[] pInventory;
    public ItemStack[] pArmor;
    public Float pEXP;
    public Integer pEXPL;
    public Double pMaxHealth;
    public Double pHealth;
    public Integer pFood;
    public Collection<PotionEffect> pPotionEffects;
    public boolean pFlying;

    public PlayerArenaData(Location pLocation, GameMode pGameMode, ItemStack[] pInventory, ItemStack[] pArmor, Float pEXP, Integer pEXPL, Double pMaxHealth, Double pHealth, Integer pFood,
                           Collection<PotionEffect> pPotionEffects, boolean pFlying) {
        this.pGameMode = pGameMode;
        this.pInventory = pInventory;
        this.pArmor = pArmor;
        this.pEXP = pEXP;
        this.pEXPL = pEXPL;
        this.pMaxHealth = pMaxHealth;
        this.pHealth = pHealth;
        this.pFood = pFood;
        this.pPotionEffects = pPotionEffects;
        this.pFlying = pFlying;
    }
}
