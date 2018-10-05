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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class ItemUtil {

    static InternalsProvider internals;

    static {
        String packageName = ItemUtil.class.getPackage().getName();
        String internalsName = CompatibilityHandler.getInstance().getInternals().toString();
        try {
            internals = (InternalsProvider) Class.forName(packageName + "." + internalsName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "ItemUtil could not find a valid implementation for " + internalsName + ".");
            internals = new InternalsProvider();
        }
    }

    private ItemUtil() {
    }

    /**
     * Returns a Collection of the attributes the ItemStack has
     *
     * @param item a Bukkit ItemStack
     * @return a Collection of the attributes the ItemStack has
     */
    public static Collection<AttributeWrapper> getAttributes(ItemStack item) {
        return internals.getAttributes(item);
    }

    /**
     * Removes all attributes that have a specific name from the ItemStack.
     * <p>
     * Use {@link #removeAttributeType(ItemStack, InternalAttribute)} or {@link #removeAttributeType(ItemStack, String)} to remove attributes of a specific
     * type.
     * <p>
     * Returns a copy of the ItemStack with the attributes removed from it
     *
     * @param item a Bukkit ItemStack
     * @param name the name that causes no impact on the attribute behavior
     * @return a copy of the ItemStack with the attributes removed from it
     */
    public static ItemStack removeAttribute(ItemStack item, String name) {
        return internals.removeAttribute(item, name, false);
    }

    /**
     * Removes all attributes of a specific type from the ItemStack.
     * <p>
     * Use {@link #removeAttribute(ItemStack, String)} to remove attributes that have a specific name.
     * <p>
     * Returns a copy of the ItemStack with the attributes removed from it
     *
     * @param item      a Bukkit ItemStack
     * @param attribute the attribute type
     * @return a copy of the ItemStack with the attributes removed from it
     */
    public static ItemStack removeAttributeType(ItemStack item, InternalAttribute attribute) {
        return removeAttributeType(item, attribute.getInternal());
    }

    /**
     * Removes all attributes of a specific type from the ItemStack.
     * <p>
     * Use {@link #removeAttribute(ItemStack, String)} to remove attributes that have a specific name.
     * <p>
     * Returns a copy of the ItemStack with the attributes removed from it
     *
     * @param item          a Bukkit ItemStack
     * @param attributeName the attribute name that defines the attribute type
     * @return a copy of the ItemStack with the attributes removed from it
     */
    public static ItemStack removeAttributeType(ItemStack item, String attributeName) {
        return internals.removeAttribute(item, attributeName, true);
    }

    /**
     * Sets an attribute to the ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the attribute applied to it
     *
     * @param item      a Bukkit ItemStack
     * @param attribute an instance of AttributeWrapper that contains the information of the attribute to add
     * @return a copy of the ItemStack that has the attribute applied to it
     */
    public static ItemStack setAttribute(ItemStack item, AttributeWrapper attribute) {
        return setAttribute(item, attribute.getAttribute(), attribute.getName(), attribute.getAmount(), attribute.getOperation(), attribute.getSlots());
    }

    /**
     * Sets an attribute to the ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the attribute applied to it
     *
     * @param item      a Bukkit ItemStack
     * @param attribute the Attribute to add
     * @param amount    the attribute value
     * @param operation the operation
     * @param slots     the slot where the attribute affects the player
     * @return a copy of the Bukkit ItemStack with the attribute applied to it
     */
    public static ItemStack setAttribute(ItemStack item, InternalAttribute attribute, double amount, InternalOperation operation, List<InternalSlot> slots) {
        ArrayList<String> slotArrayList = new ArrayList<>();
        if (slots != null) {
            slots.forEach(s -> slotArrayList.add(s.getInternal()));
        }
        return setAttribute(item, attribute.getInternal(), amount, operation.getInternal(), slotArrayList.toArray(new String[]{}));
    }

    /**
     * Sets an attribute to the ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the attribute applied to it
     *
     * @param item      a Bukkit ItemStack
     * @param attribute the Attribute to add
     * @param name      an identifier that has no impact on how the attribute behaves
     * @param amount    the attribute value
     * @param operation the operation
     * @param slots     the slot where the attribute affects the player
     * @return a copy of the Bukkit ItemStack with the attribute applied to it
     */
    public static ItemStack setAttribute(ItemStack item, InternalAttribute attribute, String name, double amount, InternalOperation operation, List<InternalSlot> slots) {
        ArrayList<String> slotArrayList = new ArrayList<>();
        if (slots != null) {
            slots.forEach(s -> slotArrayList.add(s.getInternal()));
        }
        return setAttribute(item, attribute.getInternal(), name, amount, operation.getInternal(), slotArrayList.toArray(new String[]{}));
    }

    /**
     * Sets an attribute to the ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the attribute applied to it
     *
     * @param item          a Bukkit ItemStack
     * @param attributeName the Attribute name
     * @param amount        the Attribute amount
     * @param operation     the modifier operation
     * @param slots         the slot where the attribute affects the player
     * @return a copy of the ItemStack with the attribute applied to it
     */
    public static ItemStack setAttribute(ItemStack item, String attributeName, double amount, byte operation, String... slots) {
        return setAttribute(item, attributeName, attributeName, amount, operation, slots);
    }

    /**
     * Sets an attribute to the ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the attribute applied to it
     *
     * @param item          a Bukkit ItemStack
     * @param attributeName the Attribute name
     * @param name          an identifier that has no impact on how the attribute behaves
     * @param amount        the Attribute amount
     * @param operation     the modifier operation
     * @param slots         the slot where the attribute affects the player
     * @return a copy of the ItemStack with the attribute applied to it
     */
    public static ItemStack setAttribute(ItemStack item, String attributeName, String name, double amount, byte operation, String... slots) {
        return internals.setAttribute(item, attributeName, name, amount, operation, slots);
    }

    /**
     * Returns the Base64 encoded texture value if the ItemStack has one, or null if it does not
     *
     * @param item a Bukkit ItemStack, must be a head
     * @return the Base64 encoded texture value if the ItemStack has one, or null if it does not
     */
    public static String getTextureValue(ItemStack item) {
        return internals.getTextureValue(item);
    }

    /**
     * Sets a Base64 encoded texture value to an ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the UUID and texture value applied to it
     *
     * @param item         a Bukkit ItemStack
     * @param id           the UUID of the SkullOwner
     * @param textureValue the texture value
     * @return a copy of the ItemStack with the UUID and texture value applied to it
     */
    public static ItemStack setSkullOwner(ItemStack item, UUID id, String textureValue) {
        return setSkullOwner(item, id.toString(), textureValue);
    }

    /**
     * Sets a Base64 encoded texture value to an ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the UUID and texture value applied to it
     *
     * @param item         a Bukkit ItemStack
     * @param id           the UUID of the SkullOwner
     * @param textureValue the texture value
     * @return a copy of the ItemStack with the UUID and texture value applied to it
     */
    public static ItemStack setSkullOwner(ItemStack item, String id, String textureValue) {
        return internals.setSkullOwner(item, id, textureValue);
    }

}
