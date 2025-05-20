package com.ariesrealms;

import com.ariesrealms.listener.DamageListener;
import com.ariesrealms.taks.MomentumTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class AriesRealms extends JavaPlugin {

    @Getter
    public static AriesRealms instance;
    private final Map<UUID, Long> lastMined = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        new MomentumTask().startMomentum();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DamageListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
