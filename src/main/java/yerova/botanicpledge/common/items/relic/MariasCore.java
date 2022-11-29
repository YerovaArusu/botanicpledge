package yerova.botanicpledge.common.items.relic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import yerova.botanicpledge.common.utils.BotanicPledgeConstants;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;


public class MariasCore extends DivineCoreItem implements ICurioRenderer {

    private final int maxShield = 4000;
    private final int defRegenPerTick = 40;
    private final int maxCharge = 1_000_000;


    public MariasCore(Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }


    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity living = slotContext.entity();

        ICurioRenderer.translateIfSneaking(matrixStack, living);
        ICurioRenderer.rotateIfSneaking(matrixStack, living);

        //TODO: Do more Rendering stuff
    }


    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(stack.getTag() == null || !(stack.getTag().contains(BotanicPledgeConstants.TAG_STATS_SUBSTAT))){
            stack.getOrCreateTagElement(BotanicPledgeConstants.TAG_STATS_SUBSTAT).merge(BotanicPledgeConstants.INIT_CORE_SHIELD_TAG(maxCharge, maxShield));
        }
        return super.initCapabilities(stack, nbt);
    }
}
