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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R1.NBTBase;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
class v1_8_R1 extends InternalsProvider {

    private static Field LIST;

    static {
        try {
            LIST = NBTTagList.class.getDeclaredField("list");
            LIST.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException exception) {
            exception.printStackTrace();
        }
    }

    private static List getList(NBTTagList nbt) {
        try {
            return (List) LIST.get(nbt);
        } catch (IllegalArgumentException | IllegalAccessException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    Collection<AttributeWrapper> getAttributes(ItemStack item) {
        Collection<AttributeWrapper> attributes = new ArrayList<>();
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            return attributes;
        }

        NBTTagList modifiers = tag.getList("AttributeModifiers", 10);
        for (int i = 0; i < modifiers.size(); i++) {
            NBTBase attribute = modifiers.get(i);
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
        return new AttributeWrapper(attribute, name, amount, operation);
    }

    @Override
    ItemStack removeAttribute(ItemStack item, String name, boolean type) {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            return item.clone();
        }

        List modifiers = getList(tag.getList("AttributeModifiers", 10));
        List<NBTBase> rem = new ArrayList<>();
        for (Object attribute : modifiers) {
            if (attribute instanceof NBTTagCompound) {
                if (((NBTTagCompound) attribute).getString(type ? "AttributeName" : "Name").equals(name)) {
                    rem.add((NBTBase) attribute);
                }
            }
        }
        rem.forEach(b -> modifiers.remove(b));

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    ItemStack setAttribute(ItemStack item, String attributeName, String name, double amount, byte operation, String... slots) {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        NBTTagList modifiers = tag.getList("AttributeModifiers", 10);
        NBTTagCompound attribute = new NBTTagCompound();
        attribute.setString("AttributeName", attributeName);
        attribute.setString("Name", name);
        attribute.setDouble("Amount", amount);
        attribute.setByte("Operation", operation);
        attribute.setInt("UUIDLeast", new Random().nextInt(50000) + 1);
        attribute.setInt("UUIDMost", new Random().nextInt(100000) + 50001);
        modifiers.add(attribute);

        tag.set("AttributeModifiers", modifiers);
        nmsStack.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    String getTextureValue(ItemStack item) {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

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

        for (int i = 0; i < textures.size(); i++) {
            NBTBase base = textures.get(i);
            if (base instanceof NBTTagCompound && ((NBTTagCompound) base).hasKeyOfType("Value", 8)) {
                return ((NBTTagCompound) base).getString("Value");
            }
        }
        return null;
    }

    @Override
    ItemStack setSkullOwner(ItemStack item, String id, String textureValue) {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

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
