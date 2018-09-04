package net.yourcraft.bugblockfix.inject;

import net.yourcraft.bugblockfix.BugBlockFix;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PluginListener implements Listener {
    @EventHandler
    public void onPluginLoad(PluginEnableEvent enableEvent){
        Plugin plugin = enableEvent.getPlugin();
        String name = plugin.getName();
        YamlConfiguration configuration = BugBlockFix.getInstance().getConfigManager().getConfiguration();
        List<String> plugins = configuration.getStringList("inject.plugins");
        if(plugins.contains(name)){
            BugBlockFix.getInstance().getInjectManager().injectPlugin(plugin);
        }
    }
}
