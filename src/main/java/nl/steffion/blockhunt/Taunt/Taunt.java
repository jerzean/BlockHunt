package nl.steffion.blockhunt.Taunt;

import org.bukkit.inventory.ItemStack;

public class Taunt {
    public ItemStack itemStack;
    public int coins;
    public String name;
    public TauntSupplier tauntSupplier;
    public long delayTime = 1;

    protected Taunt(ItemStack itemStack, int coins, String name, long delayTime, TauntSupplier supplier) {
        this.coins = coins;
        this.itemStack = itemStack;
        this.name = name;
        this.delayTime = delayTime;
        this.tauntSupplier = supplier;
    }

    public long nextAllowedTaunt() {
        return System.currentTimeMillis() + delayTime;
    }

    public long toSecondDelay() {
        return delayTime / 1000L;
    }

    public static Builder builder(String name, ItemStack itemStack) {
        return new Builder(itemStack, name);
    }

    public static class Builder {
        private ItemStack itemStack;
        private int winsTokens = 0;
        private final String name;
        public long delayTime = 0;
        private TauntSupplier tauntSupplier = player -> player.sendMessage("Default Taunt Supplier");

        protected Builder(ItemStack stack, String name) {
            this.name = name;
            this.itemStack = stack;
        }

        public Taunt build() {
            return new Taunt(itemStack, winsTokens, name, delayTime, tauntSupplier);
        }

        public Builder setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public Builder setWinsTokens(int winsTokens) {
            this.winsTokens = winsTokens;
            return this;
        }

        public Builder setTauntSupplier(TauntSupplier tauntSupplier) {
            this.tauntSupplier = tauntSupplier;
            return this;
        }

        public Builder setDelayTime(int second) {
            this.delayTime = second * 1000L;
            return this;
        }
    }
}
