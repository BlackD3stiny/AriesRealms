package com.ariesrealms.listener;

import com.ariesrealms.AriesRealms;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getBlock().getType() == Material.AIR) return;

        // Logs the last time a player damaged a block
        AriesRealms.getInstance().getLastMined().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
}
