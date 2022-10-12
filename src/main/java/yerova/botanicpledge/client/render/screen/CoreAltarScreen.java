package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.setup.BotanicPledge;

public class CoreAltarScreen extends AbstractContainerScreen<CoreAltarMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/core_altar/core_altar_inventory.png");

    public CoreAltarScreen(CoreAltarMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageWidth) / 2 + 7;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            blit(poseStack, x + 108, y + 40, 178, 0, menu.getScaledProgress(), 6);
        }

        if (menu.stackHasMaxStats()) {
            blit(poseStack, x + 104, y + 60, 178, 12, 68, 16);
            drawString(poseStack, font, DivineCoreItem.attributeNameList().get(menu.indexOfMaxedItem()) + " is maxed", x + 108, y + 40, 255);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
