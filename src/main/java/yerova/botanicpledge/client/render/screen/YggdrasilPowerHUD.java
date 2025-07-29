package yerova.botanicpledge.client.render.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import yerova.botanicpledge.client.synched.ClientSyncedValues;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.IAuraNode;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;
import yerova.botanicpledge.common.blocks.block_entities.ModificationAltarBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.common.items.YggdrasilMonocle;
import yerova.botanicpledge.common.items.relic.NineRealmGlove;
import yerova.botanicpledge.setup.BotanicPledge;

public class YggdrasilPowerHUD {

    private static final ResourceLocation ESSENCE_SELECTOR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/essence_selection.png");
    private static final ResourceLocation RANK_STAR = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/node_rank_display.png");


    public static final IGuiOverlay YGGDRASIL_POWER_HUD = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        renderOverlays(poseStack, screenWidth, screenHeight, ClientSyncedValues.getYggdrasilPower());
    });

    private static void renderOverlays(GuiGraphics poseStack,int screenWidth, int screenHeight, int yggdrasilPower) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }

        if (mc.player != null && YggdrasilMonocle.hasMonocle(mc.player)) {

            int maxPower = 18;
            float alpha = Math.min(1.0f, yggdrasilPower / (float) maxPower) * 0.44f;

            if (alpha > 0) {
                //renderGreenOverlay(poseStack, screenWidth, screenHeight, alpha);
            }
        }


        ItemStack glove = mc.player.getMainHandItem();
        if (!(glove.getItem() instanceof NineRealmGlove)) {
            glove = mc.player.getOffhandItem();
        }

        if (glove.getItem() instanceof NineRealmGlove) {
            EssenceList essenceList = NineRealmGlove.getEssenceList(glove);
            Essence selectedEssence = NineRealmGlove.getSelectedEssence(glove);

            if (!essenceList.isEmpty()) {
                renderEssenceDisplay(poseStack, screenWidth, screenHeight, essenceList, selectedEssence);
            }

            renderAuraNodeEssenceDisplay(poseStack,screenWidth,screenHeight, selectedEssence);

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

    private static void renderEssenceDisplay(GuiGraphics gui, int screenWidth, int screenHeight, EssenceList list, Essence selectedEssence) {
        Minecraft mc = Minecraft.getInstance();
        int xLeft = 10;
        int yStart = screenHeight - 40;


        int iconSize = 20;
        int spacing = 4;
        int startX = xLeft;


        int i = 0;
        for (var entry : list.getEssenceMap().entrySet()) {
            Essence essence = entry.getKey();
            int amount = entry.getValue();

            int x = startX + i * (iconSize + spacing);

            ItemStack displayStack = new ItemStack(essence.itemBase());

            if (selectedEssence != null && essence == selectedEssence) {
                drawSelectionIcon(gui, x,yStart);
            }

            gui.renderItem(displayStack, x, yStart);

            gui.drawString(mc.font, String.valueOf(amount), x + 10, yStart + 15, 0xFFFFFF, true);

            i++;
        }
    }

    private static void renderAuraNodeEssenceDisplay(GuiGraphics gui, int screenWidth, int screenHeight, Essence selectedEssence) {
        Minecraft mc = Minecraft.getInstance();

        if (!(mc.hitResult instanceof BlockHitResult hit)) return;
        BlockEntity be = mc.level.getBlockEntity(hit.getBlockPos());
        if (!(be instanceof IAuraNode auraNode)) return;

        AuraImplementation impl = auraNode.getImplementation();
        if (impl == null) return;

        EssenceList essenceList = impl.getEssenceList();
        boolean hasBaseEssence = impl.getBaseEssenceAmount() > 0;

        if (essenceList.isEmpty() && !hasBaseEssence) return;

        int iconSize = 20;
        int spacing = 4;
        int displayY = screenHeight / 2 - 40;
        int displayX = screenWidth / 2;

        int totalTypes = essenceList.size() + (hasBaseEssence ? 1 : 0);
        int totalWidth = (iconSize + spacing) * totalTypes - spacing;
        int startX = displayX - totalWidth / 2;

        int i = 0;

        Essence actualEssence = null;
        if (selectedEssence != null) {
            if (essenceList.hasEssence(selectedEssence) || (hasBaseEssence && impl.getBaseEssence().equals(selectedEssence))) {
                actualEssence = selectedEssence;
            }
        }

        if (actualEssence == null) {
            if (!essenceList.isEmpty()) {
                actualEssence = essenceList.getFirstEntry();
            } else if (hasBaseEssence) {
                actualEssence = impl.getBaseEssence();
            }
        }

        // Base-Essenz zuerst rendern, wenn vorhanden
        if (hasBaseEssence) {
            Essence base = impl.getBaseEssence();
            int amount = impl.getBaseEssenceAmount();

            int x = startX + i * (iconSize + spacing);

            gui.pose().pushPose();
            gui.pose().translate(x - 8, displayY - 8, 0); // zentrieren
            gui.pose().scale(2f, 2f, 1f); // doppelte Größe
            gui.renderItem(new ItemStack(base.itemBase()), 0, 0);
            gui.pose().popPose();

            if (base.equals(actualEssence)) {
                drawSelectionIcon(gui, x, displayY, 2f);
            }

            gui.drawString(mc.font, String.valueOf(amount), x + 15, displayY + 20, 0xFFFFFF, true); // Text unter größerem Icon




            i++;
        }

        // Jetzt alle anderen Essenzen rendern
        for (var entry : essenceList.getEssenceMap().entrySet()) {
            Essence essence = entry.getKey();
            int amount = entry.getValue();

            int x = startX + i * (iconSize + spacing);

            if (essence.equals(actualEssence)) {
                drawSelectionIcon(gui, x, displayY);
            }

            gui.renderItem(new ItemStack(essence.itemBase()), x, displayY);
            gui.drawString(mc.font, String.valueOf(amount), x + 10, displayY + 15, 0xFFFFFF, true);
            i++;
        }

        int rank = impl.getNodeRank();
        int starSize = 10;
        int spacingStars = 4;
        int startXStars = startX  + (3*starSize);

        for (int s = 0; s < rank; s++) {
            int sx = startXStars + s * (starSize + spacingStars);
            gui.blit(RANK_STAR, sx, displayY + 26, 0, 0, starSize, starSize, starSize, starSize);
        }


    }


    private static void drawSelectionIcon(GuiGraphics gui, int x, int yStart){
        RenderSystem.setShaderTexture(0, ESSENCE_SELECTOR);
        RenderSystem.enableBlend();

        RenderSystem.defaultBlendFunc();

        gui.blit(ESSENCE_SELECTOR, x, yStart, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    private static void drawSelectionIcon(GuiGraphics gui, int x, int y,float scale) {
        RenderSystem.setShaderTexture(0, ESSENCE_SELECTOR);
        RenderSystem.enableBlend();

        RenderSystem.defaultBlendFunc();

        int iconSize = 16;
        int scaledSize = (int)(iconSize * scale);
        int offset = (scaledSize - iconSize) / 2;

        // Ziehe den Offset ab, damit das kleinere Icon mittig unter dem größeren erscheint
        gui.blit(ESSENCE_SELECTOR, x - offset, y - offset, 0, 0, scaledSize, scaledSize, 32, 32);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

}
