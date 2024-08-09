package yerova.botanicpledge.common.utils;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/*
  I have no clue how it works, but it seems to work... So lets keep it.

  This Noise is for the Mana potency in each chunk. Might upgrade this in the future
 */
public class SimplePerlinNoise {

    private static final int[] PERMUTATION = new int[512];
    private static final int[] GRADIENTS = {1, 1, -1, 1, 1, -1, -1, -1};


    static {
        RandomSource random = RandomSource.create();
        for (int i = 0; i < 256; i++) {
            PERMUTATION[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int swap = PERMUTATION[i];
            PERMUTATION[i] = PERMUTATION[j];
            PERMUTATION[j] = swap;
            PERMUTATION[i + 256] = PERMUTATION[i]; // Duplicate for overflow
        }
    }

    public static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    public static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    public static double grad(int hash, double x, double y) {
        int h = hash & 7;
        double u = h < 4 ? x : y;
        double v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public static double noise(double x, double y) {
        int X = Mth.floor(x) & 255;
        int Y = Mth.floor(y) & 255;

        x -= Mth.floor(x);
        y -= Mth.floor(y);

        double u = fade(x);
        double v = fade(y);

        int a = PERMUTATION[X] + Y;
        int aa = PERMUTATION[a];
        int ab = PERMUTATION[a + 1];
        int b = PERMUTATION[X + 1] + Y;
        int ba = PERMUTATION[b];
        int bb = PERMUTATION[b + 1];

        double gradAA = grad(PERMUTATION[aa], x, y);
        double gradBA = grad(PERMUTATION[ba], x - 1, y);
        double gradAB = grad(PERMUTATION[ab], x, y - 1);
        double gradBB = grad(PERMUTATION[bb], x - 1, y - 1);

        double lerpX1 = lerp(u, gradAA, gradBA);
        double lerpX2 = lerp(u, gradAB, gradBB);
        return lerp(v, lerpX1, lerpX2);
    }
}
