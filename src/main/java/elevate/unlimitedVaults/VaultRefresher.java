package elevate.unlimitedVaults;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultRefresher implements Listener {

    private final JavaPlugin plugin;
    private final long resetDelayTicks;

    public VaultRefresher(JavaPlugin plugin, long resetDelayTicks) {
        this.plugin = plugin;
        this.resetDelayTicks = resetDelayTicks;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Block clicked = e.getClickedBlock();

        if (clicked == null) return;

        Material type = clicked.getType();

        if (!isVaultBlock(type)) return;

        ItemStack held = e.getPlayer().getInventory().getItemInMainHand();

        if (!isTrialKey(held)) return;

        final Location loc = clicked.getLocation();
        final Material originalType = type;
        final BlockData originalData = clicked.getBlockData();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Block b = loc.getBlock();
            b.setType(Material.AIR, false);
            b.setType(originalType, false);
            BlockData fresh = b.getBlockData();

            if (originalData instanceof Directional oldDir && fresh instanceof Directional freshDir) {
                freshDir.setFacing(oldDir.getFacing());
                b.setBlockData(freshDir, false);
            }
        }, resetDelayTicks);
    }

    private boolean isVaultBlock(Material m) {
        if (m == null) return false;
        if (m == Material.VAULT) return true;
        return m.name().equals("OMINOUS_VAULT");
    }

    private boolean isTrialKey(ItemStack item) {
        if (item == null) return false;
        Material m = item.getType();
        if (m == Material.TRIAL_KEY) return true;
        return m.name().equals("OMINOUS_TRIAL_KEY");
    }
}
