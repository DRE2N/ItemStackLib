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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class AttributeWrapper {

    private InternalAttribute attribute;
    private double amount;
    private InternalOperation operation;
    private List<InternalSlot> slots;

    public AttributeWrapper(InternalAttribute attribute, double amount, InternalOperation operation, InternalSlot... slots) {
        setAttribute(attribute);
        setAmount(amount);
        setOperation(operation);
        this.slots = new ArrayList<>();
        addSlots(slots);
    }

    public AttributeWrapper(Attribute attribute, double amount, Operation operation, EquipmentSlot... slots) {
        setAttribute(attribute);
        setAmount(amount);
        setOperation(operation);
        this.slots = new ArrayList<>();
        addSlots(slots);
    }

    public InternalAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(InternalAttribute attribute) {
        this.attribute = attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = InternalAttribute.fromBukkit(attribute);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public InternalOperation getOperation() {
        return operation;
    }

    public void setOperation(InternalOperation operation) {
        this.operation = operation;
    }

    public void setOperation(Operation operation) {
        this.operation = InternalOperation.fromBukkit(operation);
    }

    public List<InternalSlot> getSlots() {
        return slots;
    }

    public void addSlots(InternalSlot... slots) {
        this.slots.addAll(Arrays.asList(slots));
    }

    public void addSlots(EquipmentSlot... slots) {
        for (EquipmentSlot slot : slots) {
            this.slots.add(InternalSlot.fromBukkit(slot));
        }
    }

    public void removeSlots(InternalSlot... slots) {
        this.slots.removeAll(Arrays.asList(slots));
    }

    public void removeSlots(EquipmentSlot... slots) {
        for (EquipmentSlot slot : slots) {
            this.slots.remove(InternalSlot.fromBukkit(slot));
        }
    }

    /**
     * @param item
     * applies the wrapped attribute to the provided ItemStack
     */
    public ItemStack applyTo(ItemStack item) {
        return ItemUtil.setAttribute(item, this);
    }

}
