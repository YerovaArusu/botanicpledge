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

    private static final ResourceLocation NEW_CORE_BAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/protector_hud/core_bar.png");

    public static final IGuiOverlay PROTECTOR_HUD = ((gui, poseStack, partialTick, width, height) -> {
        renderManaInvBar(poseStack, ClientSyncedProtector.getDefense(), ClientSyncedProtector.getMaxDefense());
    });


    private static void renderManaInvBar(GuiGraphics gui, int totalMana, int totalMaxMana) {
        Minecraft mc = Minecraft.getInstance();
        int width = 182;
        int x = mc.getWindow().getGuiScaledWidth() / 2 - width / 2;
        int y = mc.getWindow().getGuiScaledHeight() - 32;

        if (totalMaxMana == 0) {
            width = 0;
        } else {
            width = (int) (width * ((double) totalMana / totalMaxMana));
        }

        if (width == 0) {
            if (totalMana > 0) {
                width = 1;
            } else {
                return;
            }
        }

        //color in HEX #9803fc
        float saturation = 0.988F * (float) Math.min(1F, Math.sin(Util.getMillis() / 200D) * 0.1 + 1F);
        int color = Mth.hsvToRgb(0.766F, saturation, 1.0F);

        int r = (color >> 16 & 0xFF);
        int g = (color >> 8 & 0xFF);
        int b = color & 0xFF;
        RenderSystem.setShaderColor(r / 255F, g / 255F, b / 255F, 1 - (r*0.5f / 255F));

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        gui.blit(NEW_CORE_BAR, x, y, 0, 0, width, 6, 182, 6);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

}

