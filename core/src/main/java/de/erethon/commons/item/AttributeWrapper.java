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

    /**
     * Returns a utility object to build an AttributeWrapper
     *
     * @return a utility object to build an AttributeWrapper
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private InternalAttribute attribute;
        private String name;
        private double amount = Double.MIN_VALUE;
        private InternalOperation operation;
        private List<InternalSlot> slots;

        Builder() {
        }

        public Builder attribute(InternalAttribute attribute) {
            this.attribute = attribute;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder operation(InternalOperation operation) {
            this.operation = operation;
            return this;
        }

        public Builder slots(InternalSlot... slots) {
            this.slots = new ArrayList<>(Arrays.asList(slots));
            return this;
        }

        public AttributeWrapper build() {
            if (attribute == null || amount == Double.MIN_VALUE || operation == null) {
                throw new IllegalStateException("An attribute modifier requires at least an attribute, an amount and an operation");
            }
            return new AttributeWrapper(attribute, name, amount, operation, slots);
        }

    }

    private InternalAttribute attribute;
    private String name;
    private double amount;
    private InternalOperation operation;
    private List<InternalSlot> slots;

    public AttributeWrapper(InternalAttribute attribute, String name, double amount, InternalOperation operation, List<InternalSlot> slots) {
        setAttribute(attribute);
        if (name == null) {
            name = attribute.getInternal();
        }
        setName(name);
        setAmount(amount);
        setOperation(operation);
        setSlots(slots);
    }

    public AttributeWrapper(InternalAttribute attribute, String name, double amount, InternalOperation operation, InternalSlot... slots) {
        this(attribute, name, amount, operation, Arrays.asList(slots));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setSlots(List<InternalSlot> slots) {
        this.slots = slots;
    }

    /**
     * Applies the wrapped attribute to an ItemStack.
     * <p>
     * Returns a copy of the ItemStack with the attribute applied to it.
     *
     * @param item the ItemStack
     * @return a copy of the ItemStack with this attribute applied to it
     */
    public ItemStack applyTo(ItemStack item) {
        return ItemUtil.setAttribute(item, this);
    }

    @Override
    public String toString() {
        return "AttributeWrapper{attribute=" + attribute + "; name=" + name + "; amount=" + amount + "; operation=" + operation + "; slots=" + slots + "}";
    }

}
