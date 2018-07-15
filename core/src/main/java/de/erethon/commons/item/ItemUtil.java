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
import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class ItemUtil {

    static InternalsProvider internals;

    static {
        switch (CompatibilityHandler.getInstance().getInternals()) {
            case NEW:
                internals = new New();
                break;
            case v1_13_R1:
                internals = new v1_13_R1();
                break;
            case v1_12_R1:
                internals = new v1_12_R1();
                break;
            case v1_11_R1:
                internals = new v1_11_R1();
                break;
            case v1_10_R1:
                internals = new v1_10_R1();
                break;
            case v1_9_R2:
                internals = new v1_9_R2();
                break;
            case v1_9_R1:
                internals = new v1_9_R1();
                break;
            case v1_8_R3:
                internals = new v1_8_R3();
                break;
            case v1_8_R2:
                internals = new v1_8_R2();
                break;
            case v1_8_R1:
                internals = new v1_8_R1();
                break;
            default:
                internals = new InternalsProvider();
        }
    }

    /**
     * @param itemStack
     * a Bukkit ItemStack
     * @param attribute
     * an instance of AttributeWrapper that contains the information of the attribute to add
     */
    public static ItemStack setAttribute(ItemStack itemStack, AttributeWrapper attribute) {
        return setAttribute(itemStack, attribute.getAttribute(), attribute.getAmount(), attribute.getOperation(), attribute.getSlots());
    }

    /**
     * @param itemStack
     * a Bukkit ItemStack
     * @param attribute
     * the Attribute to add
     * @param amount
     * the attribute value
     * @param operation
     * the operation
     * @param slots
     * the slot where the attribute affects the player
     * @return
     * a new Bukkit ItemStack with the attribute
     */
    public static ItemStack setAttribute(ItemStack itemStack, InternalAttribute attribute, double amount, InternalOperation operation, List<InternalSlot> slots) {
        ArrayList<String> slotArrayList = new ArrayList<>();
        slots.forEach(s -> slotArrayList.add(s.getInternal()));
        return setAttribute(itemStack, attribute.getInternal(), amount, operation.getInternal(), slotArrayList.toArray(new String[]{}));
    }

    /**
     * @param itemStack
     * a Bukkit ItemStack
     * @param attributeName
     * the Attribute name
     * @param amount
     * the Attribute amount
     * @param operation
     * the modifier operation
     * @param slots
     * the slot where the attribute affects the player
     * @return
     * a new Bukkit ItemStack with the attribute
     */
    public static ItemStack setAttribute(ItemStack itemStack, String attributeName, double amount, byte operation, String... slots) {
        return internals.setAttribute(itemStack, attributeName, amount, operation, slots);
    }

    /**
     * @param itemStack
     * a Bukkit ItemStack
     * @param id
     * the UUID of the SkullOwner
     * @param textureValue
     * the texture value
     * @return
     */
    public static ItemStack setSkullOwner(ItemStack itemStack, UUID id, String textureValue) {
        return setSkullOwner(itemStack, id.toString(), textureValue);
    }

    /**
     * @param itemStack
     * a Bukkit ItemStack
     * @param id
     * the UUID of the SkullOwner
     * @param textureValue
     * the texture value
     * @return
     */
    public static ItemStack setSkullOwner(ItemStack itemStack, String id, String textureValue) {
        return internals.setSkullOwner(itemStack, id, textureValue);
    }

}
