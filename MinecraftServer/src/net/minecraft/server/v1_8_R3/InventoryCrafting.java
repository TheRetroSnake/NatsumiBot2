package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class InventoryCrafting implements IInventory {

    private final ItemStack[] items;
    private final int b;
    private final int c;
    private final Container d;
    public List<HumanEntity> transaction;
    public IRecipe currentRecipe;
    public IInventory resultInventory;
    private EntityHuman owner;
    private int maxStack;

    public ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity crafthumanentity) {
        this.transaction.add(crafthumanentity);
    }

    public InventoryType getInvType() {
        return this.items.length == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
    }

    public void onClose(CraftHumanEntity crafthumanentity) {
        this.transaction.remove(crafthumanentity);
    }

    public List<HumanEntity> getViewers() {
        return this.transaction;
    }

    public InventoryHolder getOwner() {
        return this.owner == null ? null : this.owner.getBukkitEntity();
    }

    public void setMaxStackSize(int i) {
        this.maxStack = i;
        this.resultInventory.setMaxStackSize(i);
    }

    public InventoryCrafting(Container container, int i, int j, EntityHuman entityhuman) {
        this(container, i, j);
        this.owner = entityhuman;
    }

    public InventoryCrafting(Container container, int i, int j) {
        this.transaction = new ArrayList();
        this.maxStack = 64;
        int k = i * j;

        this.items = new ItemStack[k];
        this.d = container;
        this.b = i;
        this.c = j;
    }

    public int getSize() {
        return this.items.length;
    }

    public ItemStack getItem(int i) {
        return i >= this.getSize() ? null : this.items[i];
    }

    public ItemStack c(int i, int j) {
        return i >= 0 && i < this.b && j >= 0 && j <= this.c ? this.getItem(i + j * this.b) : null;
    }

    public String getName() {
        return "container.crafting";
    }

    public boolean hasCustomName() {
        return false;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return (IChatBaseComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName(), new Object[0]));
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                this.d.a((IInventory) this);
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                this.d.a((IInventory) this);
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        this.d.a((IInventory) this);
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void update() {}

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void startOpen(EntityHuman entityhuman) {}

    public void closeContainer(EntityHuman entityhuman) {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public int getProperty(int i) {
        return 0;
    }

    public void b(int i, int j) {}

    public int g() {
        return 0;
    }

    public void l() {
        for (int i = 0; i < this.items.length; ++i) {
            this.items[i] = null;
        }

    }

    public int h() {
        return this.c;
    }

    public int i() {
        return this.b;
    }
}
