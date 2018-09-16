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

import org.bukkit.attribute.AttributeModifier.Operation;

/**
 * @author Daniel Saukel
 */
public enum InternalOperation {

    ADD_NUMBER((byte) 0),
    ADD_SCALAR((byte) 1),
    MULTIPLY_SCALAR_1((byte) 2);

    private Object bukkit;
    private byte internal;

    InternalOperation(byte internal) {
        this.internal = internal;
    }

    public Operation getBukkit() {
        if (bukkit == null) {
            bukkit = Operation.valueOf(name());
        }
        return (Operation) bukkit;
    }

    public byte getInternal() {
        return internal;
    }

    public static InternalOperation fromBukkit(Operation bukkit) {
        for (InternalOperation op : values()) {
            if (op.bukkit == bukkit) {
                return op;
            }
        }
        return null;
    }

    public static InternalOperation fromInternal(byte internal) {
        for (InternalOperation op : values()) {
            if (op.internal == internal) {
                return op;
            }
        }
        return null;
    }

}
