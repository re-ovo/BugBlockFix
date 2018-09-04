package net.yourcraft.bugblockfix.config;

import net.yourcraft.bugblockfix.BugBlockFix;
import net.yourcraft.bugblockfix.utils.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private YamlConfiguration configuration;
    private File file;

    public ConfigManager() {
        BugBlockFix plugin = BugBlockFix.getInstance();
        File folder = plugin.getDataFolder();
        if(!folder.exists()){
            folder.mkdirs();
        }
        this.file = new File(folder,"config.yml");
        if(!file.exists()){
            plugin.saveResource("config.yml",false);
        }
        this.configuration = YamlUtil.load(this.file);
        BugBlockFix.log("配置文件读取完成");
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public File getFile() {
        return file;
    }
}
