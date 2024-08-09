package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.xplat.BotaniaConfig;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;
import yerova.botanicpledge.setup.BotanicPledge;

public class CoreHUD {

    private static final ResourceLocation EMPTY_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/empty_bar.png");
    private static final ResourceLocation FULL_CHARGE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_charge_bar.png");
    private static final ResourceLocation FULL_DEFENSE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/full_defense_bar.png");

    private static final ResourceLocation NEW_CORE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/core_bar.png");

    public static final IGuiOverlay PROTECTOR_HUD = ((gui, poseStack, partialTick, width, height) -> {

        /*

        Prototype:
        renderManaInvBar(poseStack, ClientSyncedProtector.getDefense(), ClientSyncedProtector.getMaxDefense());
         */


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


    private static void renderManaInvBar(GuiGraphics gui, int totalMana, int totalMaxMana) {
        Minecraft mc = Minecraft.getInstance();
        int width = 182;
        int x = mc.getWindow().getGuiScaledWidth() / 2 - width / 2;
        int y = mc.getWindow().getGuiScaledHeight() - BotaniaConfig.client().manaBarHeight();

        if (totalMaxMana == 0) {
            width = 0;
        } else {
            width *= (double) totalMana / (double) totalMaxMana;
        }

        if (width == 0) {
            if (totalMana > 0) {
                width = 1;
            } else {
                return;
            }
        }

        int color = 0x9803fc; //Mth.hsvToRgb(0.55F, (float) Math.min(1F, Math.sin(Util.getMillis() / 200D) * 0.5 + 1F), 1F);
        int r = (color >> 16 & 0xFF);
        int g = (color >> 8 & 0xFF);
        int b = color & 0xFF;
        RenderSystem.setShaderColor(r / 255F, g / 255F, b / 255F, 1 - (r / 255F));

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.drawTexturedModalRect(gui, NEW_CORE_BAR, x, y, 0, 251, width, 5);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

}

