package me.jonaqhan.cursedpirates;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.jonaqhan.cursedpirates.events.EntityDeath;
import me.jonaqhan.cursedpirates.events.EntityHit;
import me.jonaqhan.cursedpirates.events.EntitySpawn;
import me.jonaqhan.cursedpirates.utils.Chat;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EntitySpawn(), this);
		getServer().getPluginManager().registerEvents(new EntityHit(), this);
		getServer().getPluginManager().registerEvents(new EntityDeath(), this);
		timeTracker();
	}

	@Override
	public void onDisable() {

		List<Entity> list = Bukkit.getWorlds().get(0).getEntities();
		List<String> names = Arrays.asList("&c&lCaptain", "&6Quartermaster", "&7First Mate", "&9Cabin Boy");

		for (Entity entity : list) {
			if (entity.getType() != EntityType.VILLAGER)
				return;

			for (String name : names) {
				if (entity.getName().equals(Chat.color(name))) {

					Damageable ent = (Damageable) entity;
					ent.damage(400);

				}

			}
		}

	}

	public void timeTracker() {
		new BukkitRunnable() {
			@Override
			public void run() {

				if (getServer().getWorlds().get(0).getTime() / 1000 >= 13
						&& getServer().getWorlds().get(0).getTime() / 1000 <= 14) {
					Random r = new Random();
					if (r.nextInt(6 - 1) - 1 == 0) {
						getServer().getWorlds().get(0).setStorm(true);
						getServer().getWorlds().get(0).setThundering(true);
					}
				}

			}
		}.runTaskTimer(this, 1, 1000);

	}
}
