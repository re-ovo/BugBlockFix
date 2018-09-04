package net.yourcraft.bugblockfix.inject;

import net.yourcraft.bugblockfix.BugBlockFix;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class InjectManager {

    public void verbose(String message) {
        BugBlockFix.log(String.format("(BugBlockFix/注入器) %s", message));
    }

    EventPriority eventPriority = EventPriority.MONITOR;

    public void startInject() {
        verbose("开始读取你的注入设置...");
        YamlConfiguration configuration = BugBlockFix.getInstance().getConfigManager().getConfiguration();

        // 监听器优先级
        try {
            eventPriority = EventPriority.valueOf(configuration.getString("inject.priority"));
        } catch (Exception e) {
            verbose("读取优先级失败,检查你的配置文件! 使用默认优先级: MONITOR");
        }

        // 读取注入位置
        List<String> methods = configuration.getStringList("inject.methods");
        List<String> plugins = configuration.getStringList("inject.plugins");

        injectMethods(methods);
        verbose("方法注入工作已经完成");
    }


    public void injectMethods(List<String> methods) {
        methods.stream().forEach(m -> {
            try {
                String clazz = m.split(":")[0];
                String method = m.split(":")[1];
                Class c = Class.forName(clazz);
                Method me = c.getDeclaredMethod(method);
                if (me != null) {
                    me.setAccessible(true);
                    if (Injector.inject(me, eventPriority)) {
                        verbose("成功注入: " + m);
                    } else {
                        verbose("注入失败: " + m);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                verbose("注入方法 " + m + "失败!");
            }
        });
        verbose("注入方法完成");
    }

    private Field field;

    public void injectPlugin(Plugin plugin) {
        if (field == null) {
            try {
                field = RegisteredListener.class.getDeclaredField("priority");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
        }
        try {
            HandlerList handlerList = BlockBreakEvent.getHandlerList();
            for (RegisteredListener registeredListener : handlerList.getRegisteredListeners()) {
                if (registeredListener.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
                    field.set(registeredListener, eventPriority);
                    verbose("成功修改插件" + plugin.getName() + ": " + registeredListener.getListener().getClass().getName());
                }
            }
            handlerList.bake();//重新排序
        }catch (Exception e){
            e.printStackTrace();
        }
        verbose("注入插件" + plugin.getName() + "完成");
    }
}
