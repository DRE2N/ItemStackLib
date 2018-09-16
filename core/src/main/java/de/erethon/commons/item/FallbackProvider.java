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

import de.erethon.commons.compatibility.CompatibilityHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
class FallbackProvider extends InternalsProvider {

    /* META */
    private static String INTERNALS_VERSION = CompatibilityHandler.getInstance().getInternals().toString();
    private static String ORG_BUKKIT_CRAFTBUKKIT = "org.bukkit.craftbukkit." + INTERNALS_VERSION;
    private static String NET_MINECRAFT_SERVER = "net.minecraft.server." + INTERNALS_VERSION;

    /* ITEM STACK */
    private static Class ITEMSTACK;
    private static Method ITEMSTACK_GET_TAG;
    private static Method ITEMSTACK_GET_OR_CREATE_TAG;
    private static Method ITEMSTACK_SET_TAG;

    private static Class CRAFTITEMSTACK;
    private static Method CRAFTITEMSTACK_AS_BUKKIT_COPY;
    private static Method CRAFTITEMSTACK_AS_NMS_COPY;

    /* NBT */
    private static Class NBT_BASE;

    private static Class NBT_TAG_COMPOUND;
    private static Method NBT_TAG_COMPOUND_GET_BYTE;
    private static Method NBT_TAG_COMPOUND_GET_COMPOUND;
    private static Method NBT_TAG_COMPOUND_GET_DOUBLE;
    private static Method NBT_TAG_COMPOUND_GET_LIST;
    private static Method NBT_TAG_COMPOUND_GET_STRING;
    private static Method NBT_TAG_COMPOUND_HAS_KEY_OF_TYPE;
    private static Method NBT_TAG_COMPOUND_SET;
    private static Method NBT_TAG_COMPOUND_SET_BYTE;
    private static Method NBT_TAG_COMPOUND_SET_DOUBLE;
    private static Method NBT_TAG_COMPOUND_SET_INT;
    private static Method NBT_TAG_COMPOUND_SET_STRING;

    private static Class NBT_TAG_LIST;
    private static Method NBT_TAG_LIST_ADD;

    static {
        try {
            NBT_BASE = Class.forName(NET_MINECRAFT_SERVER + ".NBTBase");

            NBT_TAG_COMPOUND = Class.forName(NET_MINECRAFT_SERVER + ".NBTTagCompound");
            NBT_TAG_COMPOUND_GET_BYTE = NBT_TAG_COMPOUND.getDeclaredMethod("getByte", String.class);
            NBT_TAG_COMPOUND_GET_COMPOUND = NBT_TAG_COMPOUND.getDeclaredMethod("getCompound", String.class);
            NBT_TAG_COMPOUND_GET_DOUBLE = NBT_TAG_COMPOUND.getDeclaredMethod("getDouble", String.class);
            NBT_TAG_COMPOUND_GET_LIST = NBT_TAG_COMPOUND.getDeclaredMethod("getList", String.class, int.class);
            NBT_TAG_COMPOUND_GET_STRING = NBT_TAG_COMPOUND.getDeclaredMethod("getString", String.class);
            NBT_TAG_COMPOUND_HAS_KEY_OF_TYPE = NBT_TAG_COMPOUND.getDeclaredMethod("hasKeyOfType", String.class, int.class);
            NBT_TAG_COMPOUND_SET = NBT_TAG_COMPOUND.getDeclaredMethod("set", String.class, NBT_BASE);
            NBT_TAG_COMPOUND_SET_BYTE = NBT_TAG_COMPOUND.getDeclaredMethod("setByte", String.class, byte.class);
            NBT_TAG_COMPOUND_SET_DOUBLE = NBT_TAG_COMPOUND.getDeclaredMethod("setDouble", String.class, double.class);
            NBT_TAG_COMPOUND_SET_INT = NBT_TAG_COMPOUND.getDeclaredMethod("setInt", String.class, int.class);
            NBT_TAG_COMPOUND_SET_STRING = NBT_TAG_COMPOUND.getDeclaredMethod("setString", String.class, String.class);

            NBT_TAG_LIST = Class.forName(NET_MINECRAFT_SERVER + ".NBTTagList");
            NBT_TAG_LIST_ADD = NBT_TAG_LIST.getDeclaredMethod("add", NBT_BASE);

            ITEMSTACK = Class.forName(NET_MINECRAFT_SERVER + ".ItemStack");
            ITEMSTACK_GET_TAG = ITEMSTACK.getDeclaredMethod("getTag");
            ITEMSTACK_GET_OR_CREATE_TAG = ITEMSTACK.getDeclaredMethod("getOrCreateTag");
            ITEMSTACK_SET_TAG = ITEMSTACK.getDeclaredMethod("setTag", NBT_TAG_COMPOUND);

            CRAFTITEMSTACK = Class.forName(ORG_BUKKIT_CRAFTBUKKIT + ".inventory.CraftItemStack");
            CRAFTITEMSTACK_AS_BUKKIT_COPY = CRAFTITEMSTACK.getDeclaredMethod("asBukkitCopy", ITEMSTACK);
            CRAFTITEMSTACK_AS_NMS_COPY = CRAFTITEMSTACK.getDeclaredMethod("asNMSCopy", ItemStack.class);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    Collection<AttributeWrapper> getAttributes(ItemStack item) {
        Collection<AttributeWrapper> attributes = new ArrayList<>();

        try {
            Object nmsStack = CRAFTITEMSTACK_AS_NMS_COPY.invoke(null, item);

            Object tag = ITEMSTACK_GET_TAG.invoke(nmsStack);
            if (tag == null) {
                return attributes;
            }

            Iterable modifiers = (Iterable) NBT_TAG_COMPOUND_GET_LIST.invoke(tag, "AttributeModifiers", 10);
            for (Object attribute : modifiers) {
                if (NBT_TAG_COMPOUND.isInstance(attribute)) {
                    attributes.add(getAttribute(attribute));
                }
            }

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return attributes;
    }

    private AttributeWrapper getAttribute(Object tag) {
        try {
            InternalAttribute attribute = InternalAttribute.fromInternal((String) NBT_TAG_COMPOUND_GET_STRING.invoke(tag, "AttributeName"));
            String name = (String) NBT_TAG_COMPOUND_GET_STRING.invoke(tag, "Name");
            double amount = (double) NBT_TAG_COMPOUND_GET_DOUBLE.invoke(tag, "Amount");
            InternalOperation operation = InternalOperation.fromInternal((byte) NBT_TAG_COMPOUND_GET_BYTE.invoke(tag, "Operation"));
            InternalSlot slot = InternalSlot.fromInternal((String) NBT_TAG_COMPOUND_GET_STRING.invoke(tag, "Slot"));
            return new AttributeWrapper(attribute, name, amount, operation, slot);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    ItemStack removeAttribute(ItemStack item, String name, boolean type) {
        try {
            Object nmsStack = CRAFTITEMSTACK_AS_NMS_COPY.invoke(null, item);

            Object tag = ITEMSTACK_GET_TAG.invoke(nmsStack);
            if (tag == null) {
                return item.clone();
            }

            AbstractList modifiers = (AbstractList) NBT_TAG_COMPOUND_GET_LIST.invoke(tag, "AttributeModifiers", 10);
            List<Object> rem = new ArrayList<>();
            for (Object attribute : modifiers) {
                if (NBT_TAG_COMPOUND.isInstance(attribute)) {
                    if (NBT_TAG_COMPOUND_GET_STRING.invoke(attribute, type ? "AttributeName" : "Name").equals(name)) {
                        rem.add(attribute);
                    }
                }
            }
            rem.forEach(b -> modifiers.remove(b));

            return (ItemStack) CRAFTITEMSTACK_AS_BUKKIT_COPY.invoke(null, nmsStack);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
            return item;
        }
    }

    @Override
    ItemStack setAttribute(ItemStack item, String attributeName, String name, double amount, byte operation, String... slots) {
        try {
            Object nmsStack = CRAFTITEMSTACK_AS_NMS_COPY.invoke(null, item);

            Object tag = ITEMSTACK_GET_OR_CREATE_TAG.invoke(nmsStack);

            Object modifiers = NBT_TAG_COMPOUND_GET_LIST.invoke(tag, "AttributeModifiers", 10);
            if (slots.length == 0) {
                NBT_TAG_LIST_ADD.invoke(modifiers, createModifier(attributeName, name, amount, operation, null));
            } else {
                for (String slot : slots) {
                    NBT_TAG_LIST_ADD.invoke(modifiers, createModifier(attributeName, name, amount, operation, slot));
                }
            }

            NBT_TAG_COMPOUND_SET.invoke(tag, "AttributeModifiers", modifiers);
            ITEMSTACK_SET_TAG.invoke(nmsStack, tag);

            return (ItemStack) CRAFTITEMSTACK_AS_BUKKIT_COPY.invoke(null, nmsStack);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
            return item;
        }
    }

    private Object createModifier(String attributeName, String name, double amount, byte operation, String slot)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Object modifier = NBT_TAG_COMPOUND.newInstance();
        NBT_TAG_COMPOUND_SET_STRING.invoke(modifier, "AttributeName", attributeName);
        NBT_TAG_COMPOUND_SET_STRING.invoke(modifier, "Name", name);
        NBT_TAG_COMPOUND_SET_DOUBLE.invoke(modifier, "Amount", amount);
        NBT_TAG_COMPOUND_SET_BYTE.invoke(modifier, "Operation", operation);
        NBT_TAG_COMPOUND_SET_INT.invoke(modifier, "UUIDLeast", new Random().nextInt(50000) + 1);
        NBT_TAG_COMPOUND_SET_INT.invoke(modifier, "UUIDMost", new Random().nextInt(100000) + 50001);
        if (slot != null) {
            NBT_TAG_COMPOUND_SET_STRING.invoke(modifier, "Slot", slot);
        }
        return modifier;
    }

    @Override
    String getTextureValue(ItemStack item) {
        try {
            Object nmsStack = CRAFTITEMSTACK_AS_NMS_COPY.invoke(null, item);

            Object tag = ITEMSTACK_GET_TAG.invoke(nmsStack);
            if (tag == null) {
                return null;
            }

            Object skullOwner = NBT_TAG_COMPOUND_GET_COMPOUND.invoke(tag, "SkullOwner");
            if (skullOwner == null) {
                return null;
            }
            Object properties = NBT_TAG_COMPOUND_GET_COMPOUND.invoke(skullOwner, "Properties");
            if (properties == null) {
                return null;
            }
            Iterable textures = (Iterable) NBT_TAG_COMPOUND_GET_LIST.invoke(properties, "textures", 10);
            if (textures == null) {
                return null;
            }

            for (Object base : textures) {
                if (NBT_TAG_COMPOUND.isInstance(base) && (boolean) NBT_TAG_COMPOUND_HAS_KEY_OF_TYPE.invoke(base, "Value", 8)) {
                    return (String) NBT_TAG_COMPOUND_GET_STRING.invoke(base, "Value");
                }
            }
            return null;

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    ItemStack setSkullOwner(ItemStack item, String id, String textureValue) {
        try {
            Object nmsStack = CRAFTITEMSTACK_AS_NMS_COPY.invoke(null, item);

            Object tag = ITEMSTACK_GET_OR_CREATE_TAG.invoke(nmsStack);

            Object skullOwner = NBT_TAG_COMPOUND.newInstance();
            NBT_TAG_COMPOUND_SET_STRING.invoke(skullOwner, "Id", id);
            Object properties = NBT_TAG_COMPOUND.newInstance();
            Object textures = NBT_TAG_LIST.newInstance();
            Object value = NBT_TAG_COMPOUND.newInstance();
            NBT_TAG_COMPOUND_SET_STRING.invoke(value, "Value", textureValue);
            NBT_TAG_LIST_ADD.invoke(textures, value);
            NBT_TAG_COMPOUND_SET.invoke(properties, "textures", textures);
            NBT_TAG_COMPOUND_SET.invoke(skullOwner, "Properties", properties);

            NBT_TAG_COMPOUND_SET.invoke(tag, "SkullOwner", skullOwner);
            ITEMSTACK_SET_TAG.invoke(nmsStack, tag);

            return (ItemStack) CRAFTITEMSTACK_AS_BUKKIT_COPY.invoke(null, nmsStack);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
            return item;
        }
    }

}
