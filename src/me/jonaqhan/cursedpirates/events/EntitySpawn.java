package me.jonaqhan.cursedpirates.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import me.jonaqhan.cursedpirates.models.CabinBoy;
import me.jonaqhan.cursedpirates.models.Captain;
import me.jonaqhan.cursedpirates.models.FirstMate;
import me.jonaqhan.cursedpirates.models.Quartermaster;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class EntitySpawn implements Listener {

	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {

		if (e.getEntityType() == EntityType.ARROW || e.getEntityType() == EntityType.DROPPED_ITEM)
			return;

		WorldServer world = ((CraftWorld) e.getLocation().getWorld()).getHandle();

		List<EntityVillager> entities = new ArrayList<EntityVillager>();
		try {

			if (potentialCaptain(e) != null)
				entities.add(potentialCaptain(e));

			if (potentialQuartermaster(e) != null)
				entities.add(potentialQuartermaster(e));

			if (potentialFirstMate(e) != null)
				entities.add(potentialFirstMate(e));

			if (potentialCabinBoy(e) != null)
				entities.add(potentialCabinBoy(e));
		} finally {

			if (entities != null) {

				for (EntityVillager entity : entities) {

					if (entity != null) {

						LivingEntity entit = (LivingEntity) entity.getBukkitEntity();
						entit.setRemoveWhenFarAway(true);

						try {
							world.addEntity(entity);
							e.setCancelled(true);
						} catch (Exception er) {
							System.out.println(er);
						}
					}
				}
			}
		}

	}

	public Captain potentialCaptain(EntitySpawnEvent e) {

		if (!e.getEntity().getWorld().hasStorm())
			return null;

		if (e.getLocation().getBlock().isLiquid())
			return null;

		if (!(new Random().nextInt(100 - 0) - 0 < 25))
			return null;

		Captain captain = new Captain(e.getLocation());

		return captain;

	}

	public Quartermaster potentialQuartermaster(EntitySpawnEvent e) {

		if (e.getLocation().getBlock().isLiquid())
			return null;

		if (!(new Random().nextInt(100 - 0) - 0 < 8))
			return null;

		if (e.getLocation().getBlock().isLiquid())
			return null;

		Quartermaster navigator = new Quartermaster(e.getLocation());

		return navigator;

	}

	public FirstMate potentialFirstMate(EntitySpawnEvent e) {

		if (e.getLocation().getBlock().isLiquid())
			return null;

		if (!(new Random().nextInt(100 - 0) - 0 < 15))
			return null;

		if (e.getLocation().getBlock().isLiquid())
			return null;

		FirstMate mate = new FirstMate(e.getLocation());

		return mate;

	}

	public CabinBoy potentialCabinBoy(EntitySpawnEvent e) {

		if (e.getLocation().getBlock().isLiquid())
			return null;

		if (!(new Random().nextInt(100 - 0) - 0 < 12))
			return null;

		if (e.getLocation().getBlock().isLiquid())
			return null;

		CabinBoy boy = new CabinBoy(e.getLocation());

		return boy;

	}

}
