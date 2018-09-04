package net.yourcraft.bugblockfix.inject;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class Injector {

    //通过动态代理修改Annotation注解
    public static boolean inject(Method method, EventPriority eventPriority) throws NoSuchFieldException, IllegalAccessException {
        EventHandler annotation = method.getAnnotation(EventHandler.class);
        if (annotation != null) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            Field f = invocationHandler.getClass().getDeclaredField("memberValues");
            f.setAccessible(true);
            Map<String, Object> values = (Map<String, Object>) f.get(invocationHandler);
            values.put("priority", eventPriority);
            values.put("ignoreCancelled", true);
            return true;
        }
        return false;
    }
}
