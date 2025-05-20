package com.ariesrealms.taks;

import com.ariesrealms.AriesRealms;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MomentumTask {


    /**
     * This function has to be called at the start of the server to check for player momentums.
     */
    public void startMomentum() {
        Map<UUID, Integer> miningDuration = new HashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(AriesRealms.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!AriesRealms.getInstance().getLastMined().containsKey(all.getUniqueId())) continue;

                    //If the player didn't damage a block in 2 seconds they lose the effect
                    if ((AriesRealms.getInstance().getLastMined().get(all.getUniqueId()) + 2000) < System.currentTimeMillis()) {
                        AriesRealms.getInstance().getLastMined().remove(all.getUniqueId());
                        miningDuration.remove(all.getUniqueId());
                        all.getAttribute(Attribute.PLAYER_MINING_EFFICIENCY).setBaseValue(1.0);
                        continue;
                    }

                    /*
                     * Check if the player is using a pickaxe for mining
                     * This could be removed if you want to include it to other tools, but I would recommend
                     * having some sort of filter so the calculations are not done everytime a player hits a block
                     */
                    if (!all.getInventory().getItemInMainHand().getType().toString().endsWith("_PICKAXE")) continue;
                    if (!all.getInventory().getItemInMainHand().hasItemMeta()) continue;

                    /*
                     * This checks if the item has the enchantment.
                     * Note: The function is deprecated and should not be used. But since I am not familiar with datapacks
                     * and can't use Enchantment.MOMENTUM this is a sort of way that would be possible
                     *
                     * For testing purposes comment out this line
                     */
                    if (!all.getInventory().getItemInMainHand().getItemMeta().getEnchants().
                            containsKey(Enchantment.getByKey(NamespacedKey.fromString("momentum")))) continue;


                    int duration = miningDuration.getOrDefault(all.getUniqueId(), 0);
                    duration++;
                    miningDuration.put(all.getUniqueId(), duration);

                    if (duration % 10 != 0) continue;
                    int level = Math.min(duration / 10, 6);

                    double multiply = switch (level) {
                        case 1 -> 1.05;
                        case 2 -> 1.15;
                        case 3 -> 1.25;
                        case 4 -> 1.5;
                        case 5 -> 2;
                        case 6 -> 3;
                        default -> 1;
                    };
                    all.getAttribute(Attribute.PLAYER_MINING_EFFICIENCY).setBaseValue(multiply);
                    Bukkit.broadcast(Component.text(all.getAttribute(Attribute.PLAYER_MINING_EFFICIENCY).getBaseValue()));
                    miningDuration.put(all.getUniqueId(), duration);
                }
            }
        }, 1, 20);
    }
}
