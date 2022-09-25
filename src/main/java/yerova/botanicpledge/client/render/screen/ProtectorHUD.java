package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.IIngameOverlay;
import vazkii.botania.client.core.handler.ClientTickHandler;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;

public class ProtectorHUD {

    private static final ResourceLocation EMPTY_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/empty_bar.png");
    private static final ResourceLocation FULL_CHARGE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_charge_bar.png");
    private static final ResourceLocation FULL_DEFENSE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_defense_bar.png");

    public static final IIngameOverlay PROTECTOR_HUD = ((gui, poseStack, partialTick, width, height) -> {
        int x = width /3;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if(ClientSyncedProtector.getDefense() > 0 && ClientSyncedProtector.getCharge() > 0) {



            int chargeBarWidth  = (int) Math.ceil(100 * (((1/(double) ClientSyncedProtector.getMaxCharge()) * (double) ClientSyncedProtector.getCharge())));
            int defenseBarWidth  = (int) Math.ceil(100 * (((1/(double) ClientSyncedProtector.getMaxDefense()) * (double) ClientSyncedProtector.getDefense())));

            RenderSystem.setShaderTexture(0, EMPTY_BAR);
            for (int i = 0; i <= 100; i++) {
                GuiComponent.blit(poseStack, x - 98 + i, y - 20, 1, 0, 1, 8, 1, 8);
                GuiComponent.blit(poseStack, x - 98 + i, y - 10, 1, 0, 1, 8, 1, 8);
            }

            RenderSystem.setShaderTexture(0, FULL_CHARGE_BAR);
            for (int i = 0; i <= 100; i++) {
                if(chargeBarWidth>i) {
                    GuiComponent.blit(poseStack, x -98 +i, y-20, 1, 0, 1, 8, 1,8);
                }
            }

            RenderSystem.setShaderTexture(0, FULL_DEFENSE_BAR);
            for (int i = 0; i <= 100; i++) {
                if(defenseBarWidth>i) {
                    GuiComponent.blit(poseStack, x -98 +i, y-10, 1, 0, 1, 8, 1,8);
                }
            }
        } else {
            return;
        }

    });

    public static void drawBar(PoseStack ps, int value, int maxValue, int mouseX, int mouseY, int width, int height, float hue) {
        int BarWidth = (int) Math.ceil(width * (((1 / (double) maxValue) * (double) value)));

        RenderSystem.disableDepthTest();
        Gui.fill(ps, mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
        Gui.fill(ps, mouseX, mouseY - height, mouseX + BarWidth, mouseY, 0xFF000000 | Mth.hsvToRgb(hue, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
        Gui.fill(ps, mouseX + BarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);
    }
}

