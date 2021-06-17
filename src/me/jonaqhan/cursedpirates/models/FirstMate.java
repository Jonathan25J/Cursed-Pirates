package me.jonaqhan.cursedpirates.models;

import java.lang.reflect.Field;

import java.util.Collections;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.jonaqhan.cursedpirates.utils.Chat;
import net.minecraft.server.v1_16_R3.BehaviorController;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R3.VillagerType;

public class FirstMate extends EntityVillager {

	public FirstMate(Location loc) {
		super(EntityTypes.VILLAGER, ((CraftWorld) loc.getWorld()).getHandle(), VillagerType.TAIGA);

		this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(createSkull(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1ZDdkMzllZmM5ZTRkYWQ5MmJkYmM3YmVhMGJmNmRjMWE5ZGEzZDYxZmZiZGI0MTljYWRjYmQxMTdlMTMifX19")),
				true);

		this.setCustomName(new ChatComponentText(Chat.color("&7First Mate")));
		this.setPosition(loc.getX(), loc.getY(), loc.getZ());
		this.setHealth(200);
		this.setCustomNameVisible(true);
		this.setCanPickupLoot(false);
		this.removeAI();

		initPathfinder();
	}

	@Override
	public void initPathfinder() {
		super.initPathfinder();
		this.goalSelector.a(8, new PathfinderGoalRandomStrollLand(this, 0.80D));

	}

	public ItemStack createSkull(String texture) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);

		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", new String(texture)));

		Field profileField = null;

		try {
			profileField = skullMeta.getClass().getDeclaredField("profile");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		profileField.setAccessible(true);

		try {
			profileField.set(skullMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		item.setItemMeta(skullMeta);

		return item;

	}

	private void removeAI() {
		try {
			Field availableGoalsField = PathfinderGoalSelector.class.getDeclaredField("d"); // linked hash set type
			Field priorityBehaviorsField = BehaviorController.class.getDeclaredField("e"); // map type
			Field coreActivitysField = BehaviorController.class.getDeclaredField("i"); // hash set type

			availableGoalsField.setAccessible(true);
			priorityBehaviorsField.setAccessible(true);
			coreActivitysField.setAccessible(true);

			availableGoalsField.set(this.goalSelector, Sets.newLinkedHashSet());
			availableGoalsField.set(this.targetSelector, Sets.newLinkedHashSet());
			priorityBehaviorsField.set(this.getBehaviorController(), Collections.emptyMap());
			coreActivitysField.set(this.getBehaviorController(), Sets.newHashSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
