package me.jonaqhan.cursedpirates.events;

import java.util.Arrays;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.jonaqhan.cursedpirates.utils.Chat;

public class EntityDeath implements Listener {

	@EventHandler
	public void onDeath(EntityDeathEvent e) {

		if (e.getEntityType() != EntityType.VILLAGER)
			return;

		if (e.getEntity().getKiller() == null)
			return;

		if (e.getEntity().getName().equals(Chat.color("&c&lCaptain"))) {

			e.getDrops().clear();
			e.getDrops()
					.addAll(Arrays.asList(new ItemStack(Material.GOLD_INGOT, new Random().nextInt(6 - 1)),
							new ItemStack(Material.NAUTILUS_SHELL, 1),
							new ItemStack(Material.DIAMOND, new Random().nextInt(3 - 1))));

		}

		if (e.getEntity().getName().equals(Chat.color("&6Quartermaster"))) {

			e.getDrops().clear();
			e.getDrops().addAll(Arrays.asList(new ItemStack(Material.GOLD_INGOT, new Random().nextInt(4 - 1)),
					new ItemStack(Material.PAPER, 2)));

			if (new Random().nextInt(4 - 0) - 0 == 3) {
				e.getDrops().add(new ItemStack(Material.EMERALD, 1));
			}

		}

		if (e.getEntity().getName().equals(Chat.color("&7First Mate"))) {

			e.getDrops().clear();
			e.getDrops().addAll(Arrays.asList(new ItemStack(Material.GOLD_INGOT, 3), new ItemStack(Material.COAL, 2),
					new ItemStack(Material.LAPIS_LAZULI, 5), new ItemStack(Material.REDSTONE, 3)));

			if (new Random().nextInt(9) == 0) {
				e.getDrops().add(new ItemStack(Material.DIAMOND, new Random().nextInt(1) + 1));
			}
		}

		if (e.getEntity().getName().equals(Chat.color("&9Cabin Boy"))) {

			Random r = new Random();
			Boolean drop = false;

			drop = r.nextBoolean();

			e.getDrops().clear();

			e.getDrops().add(drop ? new ItemStack(Material.WATER_BUCKET) : new ItemStack(Material.LAVA_BUCKET));

		}

	}
}
