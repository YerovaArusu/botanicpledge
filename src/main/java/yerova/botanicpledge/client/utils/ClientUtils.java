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

    public static void renderPoolRecipeHUD(GuiGraphics gui, ManaPoolBlockEntity tile, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        ProfilerFiller profiler = mc.getProfiler();

        profiler.push("poolRecipe");
        ManaInfusionRecipe recipe = tile.getMatchingRecipe(stack, tile.getLevel().getBlockState(tile.getBlockPos().below()));
        if (recipe != null) {
            int x = mc.getWindow().getGuiScaledWidth() / 2 - 11;
            int y = mc.getWindow().getGuiScaledHeight() / 2 + 10;

            int u = tile.getCurrentMana() >= recipe.getManaToConsume() ? 0 : 22;
            int v = mc.player.getName().getString().equals("haighyorkie") && mc.player.isShiftKeyDown() ? 23 : 8;

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            RenderHelper.drawTexturedModalRect(gui, HUDHandler.manaBar, x, y, u, v, 22, 15);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

            gui.renderItem(stack, x - 20, y);
            ItemStack result = recipe.getResultItem(mc.level.registryAccess());
            gui.renderItem(result, x + 26, y);
            gui.renderItemDecorations(mc.font, result, x + 26, y);

            RenderSystem.disableBlend();
        }
        profiler.pop();
    }

}
