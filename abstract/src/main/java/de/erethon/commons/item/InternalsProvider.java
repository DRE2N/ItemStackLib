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

import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
class InternalsProvider {

    ItemStack setAttribute(ItemStack itemStack, String attributeName, double amount, byte operation, String... slots) {
        return itemStack;
    }

    ItemStack setSkullOwner(ItemStack itemStack, String id, String textureValue) {
        return itemStack;
    }

}
