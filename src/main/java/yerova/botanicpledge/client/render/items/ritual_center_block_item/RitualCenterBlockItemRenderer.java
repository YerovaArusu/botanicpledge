package yerova.botanicpledge.client.render.items.ritual_center_block_item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import yerova.botanicpledge.common.items.block_items.RitualCenterBlockItem;

public class RitualCenterBlockItemRenderer extends GeoItemRenderer<RitualCenterBlockItem> {


    public RitualCenterBlockItemRenderer() {
        super(new RitualCenterBlockItemModel());
    }


    @Override
    public RenderType getRenderType(RitualCenterBlockItem animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
