package net.minecraft.server.v1_8_R3;

public class MobEffectAttackDamage extends MobEffectList {

    protected MobEffectAttackDamage(int i, MinecraftKey minecraftkey, boolean flag, int j) {
        super(i, minecraftkey, flag, j);
    }

    public double a(int i, AttributeModifier attributemodifier) {
        return this.id == MobEffectList.WEAKNESS.id ? (double) (-0.5F * (float) (i + 1)) : 1.3D * (double) (i + 1);
    }
}
