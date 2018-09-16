/*
 * Written in 2018 by Daniel Saukel
 *
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software
 * to the public domain worldwide.
 *
 * This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package de.erethon.commons.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
class v1_13_R2 extends InternalsProvider {

    @Override
    Collection<AttributeWrapper> getAttributes(ItemStack item) {
        Collection<AttributeWrapper> attributes = new ArrayList<>();
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            return attributes;
        }

        NBTTagList modifiers = tag.getList("AttributeModifiers", 10);
        for (NBTBase attribute : modifiers) {
            if (attribute instanceof NBTTagCompound) {
                attributes.add(getAttribute((NBTTagCompound) attribute));
            }
        }

        return attributes;
    }

    private AttributeWrapper getAttribute(NBTTagCompound tag) {
        InternalAttribute attribute = InternalAttribute.fromInternal(tag.getString("AttributeName"));
        String name = tag.getString("Name");
        double amount = tag.getDouble("Amount");
        InternalOperation operation = InternalOperation.fromInternal(tag.getByte("Operation"));
        InternalSlot slot = InternalSlot.fromInternal(tag.getString("Slot"));
        return new AttributeWrapper(attribute, name, amount, operation, slot);
    }

    @Override
    ItemStack removeAttribute(ItemStack item, String name, boolean type) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            return item.clone();
        }

        NBTTagList modifiers = tag.getList("AttributeModifiers", 10);
        List<NBTBase> rem = new ArrayList<>();
        for (NBTBase attribute : modifiers) {
            if (attribute instanceof NBTTagCompound) {
                if (((NBTTagCompound) attribute).getString(type ? "AttributeName" : "Name").equals(name)) {
                    rem.add(attribute);
                }
            }
        }
        rem.forEach(b -> modifiers.remove(b));

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    ItemStack setAttribute(ItemStack item, String attributeName, String name, double amount, byte operation, String... slots) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getOrCreateTag();

        NBTTagList modifiers = tag.getList("AttributeModifiers", 10);
        if (slots.length == 0) {
            modifiers.add(createModifier(attributeName, name, amount, operation, null));
        } else {
            for (String slot : slots) {
                modifiers.add(createModifier(attributeName, name, amount, operation, slot));
            }
        }

        tag.set("AttributeModifiers", modifiers);
        nmsStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    private NBTTagCompound createModifier(String attributeName, String name, double amount, byte operation, String slot) {
        NBTTagCompound modifier = new NBTTagCompound();
        modifier.setString("AttributeName", attributeName);
        modifier.setString("Name", name);
        modifier.setDouble("Amount", amount);
        modifier.setByte("Operation", operation);
        modifier.setInt("UUIDLeast", new Random().nextInt(50000) + 1);
        modifier.setInt("UUIDMost", new Random().nextInt(100000) + 50001);
        if (slot != null) {
            modifier.setString("Slot", slot);
        }
        return modifier;
    }

    @Override
    String getTextureValue(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            return null;
        }

        NBTTagCompound skullOwner = tag.getCompound("SkullOwner");
        if (skullOwner == null) {
            return null;
        }
        NBTTagCompound properties = skullOwner.getCompound("Properties");
        if (properties == null) {
            return null;
        }
        NBTTagList textures = properties.getList("textures", 10);
        if (textures == null) {
            return null;
        }

        for (NBTBase base : textures) {
            if (base instanceof NBTTagCompound && ((NBTTagCompound) base).hasKeyOfType("Value", 8)) {
                return ((NBTTagCompound) base).getString("Value");
            }
        }
        return null;
    }

    @Override
    ItemStack setSkullOwner(ItemStack item, String id, String textureValue) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getOrCreateTag();

        NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.setString("Id", id);
        NBTTagCompound properties = new NBTTagCompound();
        NBTTagList textures = new NBTTagList();
        NBTTagCompound value = new NBTTagCompound();
        value.setString("Value", textureValue);
        textures.add(value);
        properties.set("textures", textures);
        skullOwner.set("Properties", properties);

        tag.set("SkullOwner", skullOwner);
        nmsStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

}
