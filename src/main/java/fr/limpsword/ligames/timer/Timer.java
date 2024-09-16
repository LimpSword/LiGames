package fr.limpsword.ligames.timer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Timer {

    private static final Map<String, BukkitTask> runnableMap = new HashMap<>();

    private static final Multimap<Plugin, Pair<BukkitTask, String>> tasksPerPlugin = ArrayListMultimap.create();

    public boolean exists(String name) {
        return runnableMap.containsKey(name);
    }

    public static void repeated(Plugin plugin, long delay, long delayBetween, Runnable runnable) {
        repeated(plugin, UUID.randomUUID().toString(), delay, delayBetween, runnable);
    }

    public static void repeated(Plugin plugin, @NotNull String name, long delay, long delayBetween, Runnable runnable) {
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimer((Plugin) plugin, delay, delayBetween);
        runnableMap.put(name, bukkitTask);
        tasksPerPlugin.put((Plugin) plugin, Pair.of(bukkitTask, name));
    }

    public static void later(Plugin plugin, long delay, Runnable runnable) {
        later(plugin, UUID.randomUUID().toString(), delay, runnable);
    }

    public static void later(Plugin plugin, @NotNull String name, long delay, Runnable runnable) {
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (runnableMap.containsKey(name)) {
                    runnable.run();
                }
                runnableMap.remove(name);
            }
        }.runTaskLater((Plugin) plugin, delay);
        runnableMap.put(name, bukkitTask);
        tasksPerPlugin.put((Plugin) plugin, Pair.of(bukkitTask, name));
    }

    public static void cancel(Plugin plugin) {
        for (Pair<BukkitTask, String> pair : tasksPerPlugin.get(plugin)) {
            pair.getKey().cancel();
            runnableMap.remove(pair.getValue());
        }
        tasksPerPlugin.removeAll(plugin);
    }

    public static void cancel(@NotNull String name) {
        if (runnableMap.containsKey(name)) {
            BukkitTask bukkitTask = runnableMap.get(name);
            bukkitTask.cancel();

            runnableMap.remove(name);
        }
    }
}