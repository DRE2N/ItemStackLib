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
 * along with this software. If not, see <http,//creativecommons.org/publicdomain/zero/1.0/>.
 */
package de.erethon.commons.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Daniel Saukel
 */
class InternalsProvider {

    Collection<AttributeWrapper> getAttributes(ItemStack itemStack) {
        Collection<AttributeWrapper> wrappers = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.hasAttributeModifiers()) {
            for (Entry<Attribute, Collection<AttributeModifier>> entry : meta.getAttributeModifiers().asMap().entrySet()) {
                entry.getValue().forEach(mod
                        -> wrappers.add(AttributeWrapper.builder()
                                .attribute(InternalAttribute.fromBukkit(entry.getKey()))
                                .name(mod.getName())
                                .amount(mod.getAmount())
                                .operation(InternalOperation.fromBukkit(mod.getOperation()))
                                .slots(InternalSlot.fromBukkit(mod.getSlot()))
                                .build()));
            }
        }
        return wrappers;
    }

    ItemStack removeAttribute(ItemStack itemStack, String name, boolean type) {
        ItemMeta meta = itemStack.getItemMeta();
        if (type) {
            meta.removeAttributeModifier(InternalAttribute.fromInternal(name).getBukkit());
        } else {
            Multimap<Attribute, AttributeModifier> rem = HashMultimap.create();
            for (Entry<Attribute, Collection<AttributeModifier>> entry : meta.getAttributeModifiers().asMap().entrySet()) {
                for (AttributeModifier mod : entry.getValue()) {
                    if (mod.getName().equals(name)) {
                        rem.put(entry.getKey(), mod);
                    }
                }
            }
            rem.asMap().entrySet().forEach(e -> e.getValue().forEach(m -> meta.removeAttributeModifier(e.getKey(), m)));
        }
        ItemStack clone = itemStack.clone();
        clone.setItemMeta(meta);
        return clone;
    }

    ItemStack setAttribute(ItemStack itemStack, String attributeName, String name, double amount, byte operation, String... slots) {
        ItemMeta meta = itemStack.getItemMeta();
        Attribute bAttr = InternalAttribute.fromInternal(attributeName).getBukkit();
        Operation bOp = InternalOperation.fromInternal(operation).getBukkit();
        if (slots.length > 0) {
            for (String slot : slots) {
                meta.addAttributeModifier(bAttr, new AttributeModifier(UUID.randomUUID(), name, amount, bOp, InternalSlot.fromInternal(slot).getBukkit()));
            }
        } else {
            meta.addAttributeModifier(bAttr, new AttributeModifier(UUID.randomUUID(), name, amount, bOp));
        }
        ItemStack clone = itemStack.clone();
        clone.setItemMeta(meta);
        return clone;
    }

    String getTextureValue(ItemStack itemStack) {
        return "";
    }

    ItemStack setSkullOwner(ItemStack itemStack, String id, String textureValue) {
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(id)));
        ItemStack clone = itemStack.clone();
        clone.setItemMeta(meta);
        return clone;
    }

}
