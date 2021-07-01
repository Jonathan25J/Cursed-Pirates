package me.jonaqhan.cursedpirates.events;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.jonaqhan.cursedpirates.utils.Chat;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class EntityHit implements Listener {

	public List<String> names = Arrays.asList("&c&lCaptain", "&6Quartermaster", "&7First Mate", "&9Cabin Boy");

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager().getType() == EntityType.LIGHTNING) {

			if (e.getEntityType() == EntityType.WITCH) {
				for (String name : names) {
					if (e.getEntity().getName().equals(Chat.color(name))) {
						e.setCancelled(true);
						e.getEntity().remove();

						return;
					}
				}
			}
		}
		if (!e.getDamager().getType().equals(EntityType.VILLAGER))
			return;

		if (e.getDamager().getName().equals(Chat.color("&c&lCaptain"))) {

			if (!(e.getEntity() instanceof Player))
				return;

			Player p = (Player) e.getEntity();

			p.getInventory().clear();

			EntityPlayer cp = ((CraftPlayer) p).getHandle();

			PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(cp, (byte) 35);
			cp.playerConnection.sendPacket(status);

			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2, true, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2, true, false));
			p.getWorld().strikeLightning(p.getLocation());

			Location loc = e.getDamager().getLocation();

			Damageable captain = (Damageable) e.getDamager();
			p.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc.getX(), loc.getY(), loc.getZ(), 10000, 0.001, 1, 0, 1,
					null);
			captain.damage(200);
		}

		if (e.getDamager().getName().equals(Chat.color("&9Cabin Boy"))) {

			if (!(e.getEntity() instanceof Player))
				return;

			Player p = (Player) e.getEntity();

			p.getInventory().addItem(new ItemStack(Material.COBWEB));

			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 100), true);

			if (new Random().nextInt(10) >= 5) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1), true);
			}

			e.setCancelled(true);

		}

	}

	@EventHandler
	public void zombieFix(EntityTargetLivingEntityEvent e) {

		if (!e.getEntity().getType().equals(EntityType.ZOMBIE) && !e.getEntity().getType().equals(EntityType.HUSK))
			return;

		if (e.getTarget() != null) {

			if (!e.getTarget().getType().equals(EntityType.VILLAGER))
				return;

			for (String name : names) {
				if (e.getTarget().getName().equals(Chat.color(name)))
					e.setCancelled(true);

			}

		}
	}

}
