package net.minecraft.server.v1_8_R3;

class BlockCocoa$1 {

    static final int[] a = new int[EnumDirection.values().length];

    static {
        try {
            BlockCocoa$1.a[EnumDirection.SOUTH.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror) {
            ;
        }

        try {
            BlockCocoa$1.a[EnumDirection.NORTH.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror1) {
            ;
        }

        try {
            BlockCocoa$1.a[EnumDirection.WEST.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror2) {
            ;
        }

        try {
            BlockCocoa$1.a[EnumDirection.EAST.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror3) {
            ;
        }

    }
}
