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

import org.bukkit.attribute.Attribute;

/**
 * @author Daniel Saukel
 */
public enum InternalAttribute {

    GENERIC_ARMOR("generic.armor"),
    GENERIC_ARMOR_TOUGHNESS("generic.armorToughness"),
    GENERIC_ATTACK_DAMAGE("generic.attackDamage"),
    GENERIC_ATTACK_SPEED("generic.attackSpeed"),
    GENERIC_FLYING_SPEED("generic.flyingSpeed"),
    GENERIC_FOLLOW_RANGE("generic.followRange"),
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    GENERIC_LUCK("generic.luck"),
    GENERIC_MAX_HEALTH("generic.maxHealth"),
    GENERIC_MOVEMENT_SPEED("generic.movementSpeed"),
    HORSE_JUMP_STRENGTH("horse.jumpStrength"),
    ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

    private String internal;
    private Object bukkit;

    InternalAttribute(String internal) {
        this.internal = internal;
    }

    public Attribute getBukkit() {
        if (bukkit == null) {
            bukkit = Attribute.valueOf(name());
        }
        return (Attribute) bukkit;
    }

    public String getInternal() {
        return internal;
    }

    public static InternalAttribute fromBukkit(Attribute bukkit) {
        for (InternalAttribute attribute : values()) {
            if (attribute.bukkit == bukkit) {
                return attribute;
            }
        }
        return null;
    }

    public static InternalAttribute fromInternal(String internal) {
        for (InternalAttribute attribute : values()) {
            if (attribute.internal.equals(internal)) {
                return attribute;
            }
        }
        return null;
    }

}
