package yerova.botanicpledge.client.particle;


import javax.annotation.Nonnull;
import java.util.Random;

public class ParticleColor implements Cloneable {
    private final float r;
    private final float g;
    private final float b;
    private final int color;

    public ParticleColor(int r, int g, int b) {
        this.r = r / 255F;
        this.g = g / 255F;
        this.b = b / 255F;
        this.color = (r << 16) | (g << 8) | b;
    }

    public ParticleColor(double red, double green, double blue) {
        this((int) red, (int) green, (int) blue);
    }

    public static ParticleColor makeRandomColor(int r, int g, int b, Random random) {
        return new ParticleColor(random.nextInt(r), random.nextInt(g), random.nextInt(b));
    }

    public ParticleColor(float r, float g, float b) {
        this((int) r, (int) g, (int) b);
    }


    public static ParticleColor fromInt(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        return new ParticleColor(r, g, b);
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public int getColor() {
        return color;
    }

    public String serialize() {
        return "" + this.r + "," + this.g + "," + this.b;
    }

    public IntWrapper toWrapper() {
        return new IntWrapper(this);
    }

    public static ParticleColor deserialize(String string) {
        if (string == null || string.isEmpty())
            return ParticleUtils.defaultParticleColor();
        String[] arr = string.split(",");
        return new ParticleColor(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()));
    }

    @Override
    public ParticleColor clone() {
        try {
            ParticleColor clone = (ParticleColor) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class IntWrapper implements Cloneable {
        public int r;
        public int g;
        public int b;

        public IntWrapper(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public IntWrapper(ParticleColor color) {
            this.r = (int) (color.getRed() * 255.0);
            this.g = (int) (color.getGreen() * 255.0);
            this.b = (int) (color.getBlue() * 255.0);
        }

        public ParticleColor toParticleColor() {
            return new ParticleColor(r, g, b);
        }

        public String serialize() {
            return "" + this.r + "," + this.g + "," + this.b;
        }

        public void makeVisible() {
            if (r + g + b < 20) {
                b += 10;
                g += 10;
                r += 10;
            }
        }

        public static @Nonnull ParticleColor.IntWrapper deserialize(String string) {
            ParticleColor.IntWrapper color = ParticleUtils.defaultParticleColorWrapper();
            if (string == null || string.isEmpty())
                return color;

            try {
                String[] arr = string.split(",");
                color = new ParticleColor.IntWrapper(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()));
                return color;
            } catch (Exception ignored) {
            }
            return color;
        }

        @Override
        public IntWrapper clone() {
            try {
                IntWrapper clone = (IntWrapper) super.clone();
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
