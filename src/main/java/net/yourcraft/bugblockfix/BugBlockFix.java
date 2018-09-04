package net.yourcraft.bugblockfix;

import net.yourcraft.bugblockfix.config.ConfigManager;
import net.yourcraft.bugblockfix.inject.InjectManager;
import net.yourcraft.bugblockfix.inject.PluginListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class BugBlockFix extends JavaPlugin {
    private static BugBlockFix instance; //插件实例
    private static final String prefix = "§2[BugBlockFix]§r %s";

    private ConfigManager configManager;
    private InjectManager injectManager;

    @Override
    public void onLoad() {
        instance = this;
        log("开始加载BugBlockFix...");
        this.configManager = new ConfigManager();
        this.injectManager = new InjectManager();
        log("插件初始化完成");
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PluginListener(),this);//注册监听器
        this.injectManager.startInject(); //注入方法
    }

    @Override
    public void onDisable() {
        //....
    }

    // 彩色打印到控制台
    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage(String.format(prefix,ChatColor.translateAlternateColorCodes('&',message)));
    }

    public static BugBlockFix getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public InjectManager getInjectManager() {
        return injectManager;
    }
}
