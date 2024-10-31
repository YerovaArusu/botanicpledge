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


        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }

        ProfilerFiller profiler = mc.getProfiler();
        if (YggdrasilMonocle.hasMonocle(mc.player)) {
            profiler.push("yggdrasil_monocle");


            if (mc.player != null && mc.player.getMainHandItem().getItem() instanceof YggdrasilMonocle) {
                int maxPower = 15;
                int currentPower = ClientSyncedValues.getYggdrasilPower();

                // Determine the green overlay's transparency based on the power level
                float alpha = Math.min(1.0f, currentPower / (float) maxPower) * 0.5f; // Alpha between 0 and 0.5 for subtler effect

                if (alpha > 0) {
                    renderGreenOverlay(poseStack, screenWidth, screenHeight, alpha);
                }
            }
            profiler.pop();
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


    });

    private static void renderGreenOverlay(GuiGraphics gui, int screenWidth, int screenHeight, float alpha) {
        // Set up render system for translucent green overlay
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(0.0f, 1.0f, 0.0f, alpha); // Green with dynamic transparency

        // Draw the full-screen overlay
        gui.fillGradient(0, 0, screenWidth, screenHeight, 0x0032CD32, 0x0032CD32); // ARGB green with specified alpha

        // Reset render state
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
