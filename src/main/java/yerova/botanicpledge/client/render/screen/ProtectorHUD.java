package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;
import yerova.botanicpledge.setup.BotanicPledge;

public class ProtectorHUD {

    private static final ResourceLocation EMPTY_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/empty_bar.png");
    private static final ResourceLocation FULL_CHARGE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_charge_bar.png");
    private static final ResourceLocation FULL_DEFENSE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_defense_bar.png");

    public static final IIngameOverlay PROTECTOR_HUD = ((gui, poseStack, partialTick, width, height) -> {


        int x = width / 3;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (ClientSyncedProtector.getDefense() > 0 && ClientSyncedProtector.getCharge() > 0) {


            int chargeBarWidth = (int) Math.ceil(100 * (((1 / (double) ClientSyncedProtector.getMaxCharge()) * (double) ClientSyncedProtector.getCharge())));
            int defenseBarWidth = (int) Math.ceil(100 * (((1 / (double) ClientSyncedProtector.getMaxDefense()) * (double) ClientSyncedProtector.getDefense())));

            RenderSystem.setShaderTexture(0, EMPTY_BAR);
            for (int i = 0; i <= 100; i++) {
                GuiComponent.blit(poseStack, x - 98 + i, y - 20, 1, 0, 1, 8, 1, 8);
                GuiComponent.blit(poseStack, x - 98 + i, y - 10, 1, 0, 1, 8, 1, 8);
            }

            RenderSystem.setShaderTexture(0, FULL_CHARGE_BAR);
            for (int i = 0; i <= 100; i++) {
                if (chargeBarWidth > i) {
                    GuiComponent.blit(poseStack, x - 98 + i, y - 20, 1, 0, 1, 8, 1, 8);
                }
            }

            RenderSystem.setShaderTexture(0, FULL_DEFENSE_BAR);
            for (int i = 0; i <= 100; i++) {
                if (defenseBarWidth > i) {
                    GuiComponent.blit(poseStack, x - 98 + i, y - 10, 1, 0, 1, 8, 1, 8);
                }
            }
        } else {
            return;
        }

    });

    //Test Rework of Mana display

    public static void drawBar(PoseStack ps, int value, int maxValue, int mouseX, int mouseY, int width, int height, int barIdentifier) {
        int barWidth = (int) Math.ceil(width * (((1 / (double) maxValue) * (double) value)));

        drawSingleBar(EMPTY_BAR, ps, mouseX - 1, mouseY - height - 1, width, true);
        if (barIdentifier == 1) {
            drawSingleBar(FULL_CHARGE_BAR, ps, mouseX - 1, mouseY - height - 1, barWidth, true);
        }
        if (barIdentifier == 2) {
            drawSingleBar(FULL_DEFENSE_BAR, ps, mouseX - 1, mouseY - height - 1, barWidth, true);
        }

    }

    public static void drawSingleBar(ResourceLocation source, PoseStack pose, int x, int y, int barWidth, boolean disableDepthTest) {
        if (disableDepthTest) RenderSystem.disableDepthTest();

        RenderSystem.setShaderTexture(0, source);
        for (int i = 0; i <= 101; i++) {
            if (barWidth + 2 > i && i > 1 && i < 101) {
                if (barWidth >= i) {
                    GuiComponent.blit(pose, x + i, y, -1, 0, 1, 5, 1, 8);
                }
            }
        }


    }


}

