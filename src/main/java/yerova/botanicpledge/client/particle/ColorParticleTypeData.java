package yerova.botanicpledge.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import yerova.botanicpledge.setup.ParticleSetup;

public class ColorParticleTypeData implements ParticleOptions {

    private ParticleType<ColorParticleTypeData> type;
    public static final Codec<ColorParticleTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest),
                    Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
                    Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age)
            )
            .apply(instance, ColorParticleTypeData::new));

    public ParticleColor color;
    public boolean disableDepthTest;
    public float size = .25f;
    public float alpha = 1.0f;
    public int age = 36;

    public static final ParticleOptions.Deserializer<ColorParticleTypeData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Override
        public ColorParticleTypeData fromCommand(ParticleType<ColorParticleTypeData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ColorParticleTypeData(type, ParticleColor.deserialize(reader.readString()), reader.readBoolean());
        }

        @Override
        public ColorParticleTypeData fromNetwork(ParticleType<ColorParticleTypeData> type, FriendlyByteBuf buffer) {
            return new ColorParticleTypeData(type, ParticleColor.deserialize(buffer.readUtf()), buffer.readBoolean());
        }
    };

    public ColorParticleTypeData(float r, float g, float b, boolean disableDepthTest, float size, float alpha, int age) {
        this(ParticleSetup.YGGDRAL_TYPE, new ParticleColor(r, g, b), disableDepthTest, size, alpha, age);
    }

    public ColorParticleTypeData(ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        this(ParticleSetup.YGGDRAL_TYPE, color, disableDepthTest, size, alpha, age);
    }

    public ColorParticleTypeData(ParticleType<ColorParticleTypeData> particleTypeData, ParticleColor color, boolean disableDepthTest) {
        this(particleTypeData, color, disableDepthTest, 0.25f, 1.0f, 36);
    }

    public ColorParticleTypeData(ParticleType<ColorParticleTypeData> particleTypeData, ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        this.type = particleTypeData;
        this.color = color;
        this.disableDepthTest = disableDepthTest;
        this.size = size;
        this.alpha = alpha;
        this.age = age;
    }


    @Override
    public ParticleType<ColorParticleTypeData> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUtf(color.serialize());
    }

    @Override
    public String writeToString() {
        return type.getRegistryName().toString() + " " + color.serialize();
    }
}
