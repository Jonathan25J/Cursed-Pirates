package me.jonaqhan.cursedpirates.models;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.jonaqhan.cursedpirates.utils.Chat;
import net.minecraft.server.v1_16_R3.AttributeModifiable;
import net.minecraft.server.v1_16_R3.BehaviorController;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.Item;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R3.VillagerType;

public class Quartermaster extends EntityVillager {

	private static Field attributeField;

	@SuppressWarnings("deprecation")
	public Quartermaster(Location loc) {
		super(EntityTypes.VILLAGER, ((CraftWorld) loc.getWorld()).getHandle(), VillagerType.SAVANNA);

		this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(createSkull(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQyMWExOTRiYmE1MDIxN2JjYzU3MmI5ZjRkYTk4NGZmNjFlOWEyNmMxNTU1Zjc2NThhZThhMjFhYjVlMjljYiJ9fX0=")),
				true);

		this.setCustomName(new ChatComponentText(Chat.color("&6Quartermaster")));
		this.setPosition(loc.getX(), loc.getY(), loc.getZ());
		this.setHealth(200);
		this.setCustomNameVisible(true);
		this.setCanPickupLoot(false);
		this.getEquipment(EnumItemSlot.MAINHAND).setItem(Item.getById(276));
		this.removeAI();

		initPathfinder();
		try {
			registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_ATTACK_DAMAGE);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initPathfinder() {
		super.initPathfinder();

		this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.ATTACK_DAMAGE, (a) -> {
			a.setValue(1000.0);
		}));
		this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.FOLLOW_RANGE, (a) -> {
			a.setValue(17.5);
		}));

		this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100);
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.30D, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
		this.goalSelector.a(8, new PathfinderGoalRandomStrollLand(this, 0.60D));

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

	static {
		try {
			attributeField = net.minecraft.server.v1_16_R3.AttributeMapBase.class.getDeclaredField("b");
			attributeField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute)
			throws IllegalAccessException {
		net.minecraft.server.v1_16_R3.AttributeMapBase attributeMapBase = ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity) entity)
				.getHandle().getAttributeMap();
		Map<net.minecraft.server.v1_16_R3.AttributeBase, net.minecraft.server.v1_16_R3.AttributeModifiable> map = (Map<net.minecraft.server.v1_16_R3.AttributeBase, net.minecraft.server.v1_16_R3.AttributeModifiable>) attributeField
				.get(attributeMapBase);
		net.minecraft.server.v1_16_R3.AttributeBase attributeBase = org.bukkit.craftbukkit.v1_16_R3.attribute.CraftAttributeMap
				.toMinecraft(attribute);
		net.minecraft.server.v1_16_R3.AttributeModifiable attributeModifiable = new net.minecraft.server.v1_16_R3.AttributeModifiable(
				attributeBase, net.minecraft.server.v1_16_R3.AttributeModifiable::getAttribute);
		map.put(attributeBase, attributeModifiable);
	}

}
