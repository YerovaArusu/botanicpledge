package yerova.botanicpledge.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class ClientUtils {

    public static ModelLayerLocation make(String name) {
        return make(name, "main");
    }

    public static ModelLayerLocation make(String name, String layer) {
        return new ModelLayerLocation(new ResourceLocation(BotanicPledge.MOD_ID, name), layer);
    }

    public static void drawManaHUD(GuiGraphics gui,int x, int y, int color, int mana, int maxMana, String name) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();


        gui.drawString(mc.font, name, x, y, color);

        x = mc.getWindow().getGuiScaledWidth() / 2 - 51;
        y += 10;

        HUDHandler.renderManaBar(gui, x, y, color, 1F, mana, maxMana);

        RenderSystem.disableBlend();
    }

}
