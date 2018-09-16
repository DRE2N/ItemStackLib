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

import org.bukkit.inventory.EquipmentSlot;

/**
 * @author Daniel Saukel
 */
public enum InternalSlot {

    HAND("mainhand"),
    OFF_HAND("offhand"),
    HEAD("head"),
    CHEST("torso"),
    LEGS("legs"),
    FEET("feet");

    private Object bukkit;
    private String internal;

    InternalSlot(String internal) {
        this.internal = internal;
    }

    public EquipmentSlot getBukkit() {
        if (bukkit == null) {
            bukkit = EquipmentSlot.valueOf(name());
        }
        return (EquipmentSlot) bukkit;
    }

    public String getInternal() {
        return internal;
    }

    public static InternalSlot fromBukkit(EquipmentSlot bukkit) {
        for (InternalSlot slot : values()) {
            if (slot.bukkit == bukkit) {
                return slot;
            }
        }
        return null;
    }

    public static InternalSlot fromInternal(String internal) {
        for (InternalSlot slot : values()) {
            if (slot.internal.equals(internal)) {
                return slot;
            }
        }
        return null;
    }

}
