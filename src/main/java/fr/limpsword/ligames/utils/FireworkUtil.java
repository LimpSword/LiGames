package fr.limpsword.ligames.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FireworkUtil {

    public static void sendFirework(Plugin plugin, Location location, int power, Color color, long explodeAfter) {
        sendFirework(plugin, location, power, color, explodeAfter, false);
    }

    public static void sendFirework(Plugin plugin, Location location, int power, Color color, long explodeAfter, boolean noDamage) {
        Random r = new Random();
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);

        if (noDamage) {
            // TODO
        }

        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(power);

        if (color == null) {
            color = Color.fromBGR(r.nextInt(254), r.nextInt(254), r.nextInt(254));
        }
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).trail(false).build());
        fw.setFireworkMeta(fwm);

        long explode = explodeAfter * 20L;
        if (explodeAfter < 1) {
            explode = 1;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                fw.detonate();
            }
        }.runTaskLater(plugin, explode);
    }
}