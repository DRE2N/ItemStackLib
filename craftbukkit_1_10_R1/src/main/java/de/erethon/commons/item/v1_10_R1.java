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

import java.util.Random;
import net.minecraft.server.v1_10_R1.NBTTagByte;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagDouble;
import net.minecraft.server.v1_10_R1.NBTTagInt;
import net.minecraft.server.v1_10_R1.NBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
class v1_10_R1 extends InternalsProvider {

    @Override
    ItemStack setAttribute(ItemStack itemStack, String attributeName, double amount, byte operation, String... slots) {
        net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }

        NBTTagList modifiers = compound.getList("AttributeModifiers", 10);
        for (String slot : slots) {
            NBTTagCompound attribute = new NBTTagCompound();
            attribute.set("AttributeName", new NBTTagString(attributeName));
            attribute.set("Name", new NBTTagString(attributeName));
            attribute.set("Amount", new NBTTagDouble(amount));
            attribute.set("Operation", new NBTTagByte(operation));
            attribute.set("UUIDLeast", new NBTTagInt(new Random().nextInt(50000) + 1));
            attribute.set("UUIDMost", new NBTTagInt(new Random().nextInt(100000) + 50001));
            attribute.set("Slot", new NBTTagString(slot));
            modifiers.add(attribute);
        }

        compound.set("AttributeModifiers", modifiers);
        nmsStack.setTag(compound);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    ItemStack setSkullOwner(ItemStack itemStack, String id, String textureValue) {
        net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }

        NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.set("Id", new NBTTagString(id));
        NBTTagCompound properties = new NBTTagCompound();
        NBTTagList textures = new NBTTagList();
        NBTTagCompound value = new NBTTagCompound();
        value.set("Value", new NBTTagString(textureValue));
        textures.add(value);
        properties.set("textures", textures);
        skullOwner.set("Properties", properties);

        compound.set("SkullOwner", skullOwner);
        nmsStack.setTag(compound);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

}
