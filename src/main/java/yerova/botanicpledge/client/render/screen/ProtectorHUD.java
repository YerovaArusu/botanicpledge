package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;
import yerova.botanicpledge.setup.BotanicPledge;

public class ProtectorHUD {

    private static final ResourceLocation EMPTY_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/empty_bar.png");
    private static final ResourceLocation FULL_CHARGE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_charge_bar.png");
    private static final ResourceLocation FULL_DEFENSE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_defense_bar.png");

    public static final IGuiOverlay PROTECTOR_HUD = ((gui, poseStack, partialTick, width, height) -> {


        int x = width / 3;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (ClientSyncedProtector.getDefense() > 0 && ClientSyncedProtector.getCharge() > 0) {


            int chargeBarWidth = (int) Math.ceil(100 * (((1 / (double) ClientSyncedProtector.getMaxCharge()) * (double) ClientSyncedProtector.getCharge())));
            int defenseBarWidth = (int) Math.ceil(100 * (((1 / (double) ClientSyncedProtector.getMaxDefense()) * (double) ClientSyncedProtector.getDefense())));

            RenderSystem.setShaderTexture(0, EMPTY_BAR);
            for (int i = 0; i <= 100; i++) {

                poseStack.blit(EMPTY_BAR, x - 98 + i, y - 20, 1, 0, 1, 8, 1, 8);
                poseStack.blit(EMPTY_BAR, x - 98 + i, y - 10, 1, 0, 1, 8, 1, 8);
            }

            RenderSystem.setShaderTexture(0, FULL_CHARGE_BAR);
            for (int i = 0; i <= 100; i++) {
                if (chargeBarWidth > i) {
                    poseStack.blit(FULL_CHARGE_BAR, x - 98 + i, y - 20, 1, 0, 1, 8, 1, 8);
                }
            }

            RenderSystem.setShaderTexture(0, FULL_DEFENSE_BAR);
            for (int i = 0; i <= 100; i++) {
                if (defenseBarWidth > i) {
                    poseStack.blit(FULL_DEFENSE_BAR, x - 98 + i, y - 10, 1, 0, 1, 8, 1, 8);
                }
            }
        } else {
            return;
        }

    });


}

