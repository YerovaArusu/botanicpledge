package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import yerova.botanicpledge.client.synched.ClientSyncedValues;
import yerova.botanicpledge.common.blocks.block_entities.ModificationAltarBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.common.items.YggdrasilMonocle;

public class YggdrasilPowerHUD {

    public static final IGuiOverlay YGGDRASIL_POWER_HUD = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        renderOverlays(poseStack, screenWidth, screenHeight, ClientSyncedValues.getYggdrasilPower());
    });

    private static void renderOverlays(GuiGraphics poseStack,int screenWidth, int screenHeight, int yggdrasilPower) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }

        ProfilerFiller profiler = mc.getProfiler();
        if (mc.player != null && YggdrasilMonocle.hasMonocle(mc.player)) {

            int maxPower = 18;
            float alpha = Math.min(1.0f, yggdrasilPower / (float) maxPower) * 0.44f;

            if (alpha > 0) {
                renderGreenOverlay(poseStack, screenWidth, screenHeight, alpha);
            }
        }

        if (mc.hitResult instanceof BlockHitResult result) {
            BlockPos bpos = result.getBlockPos();
            BlockEntity tile = mc.level.getBlockEntity(bpos);

            if (!PlayerHelper.hasHeldItem(mc.player, BotaniaItems.lexicon)) {
                if (tile instanceof RitualCenterBlockEntity altar) {
                    RitualCenterBlockEntity.Hud.render(altar, poseStack, mc);
                }
                if (tile instanceof ModificationAltarBlockEntity altar) {
                    ModificationAltarBlockEntity.Hud.render(altar, poseStack, mc);
                }
            }
        }


    }

    private static void renderGreenOverlay(GuiGraphics gui, int screenWidth, int screenHeight, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(0.0f, 1.0f, 0.0f, alpha);

        int overlayColor = ((int) (alpha * 255) << 24) | 0x32CD32;

        gui.fillGradient(0, 0, screenWidth, screenHeight, overlayColor, overlayColor);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

}
