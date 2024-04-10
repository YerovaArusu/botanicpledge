package yerova.botanicpledge.common.blocks.block_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.api.block.Wandable;
import yerova.botanicpledge.common.blocks.ModificationAltarBlock;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.AttributeProvider;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nullable;
import java.util.List;

public class ModificationAltarBlockEntity extends RitualBaseBlockEntity implements Wandable {

    public boolean alter = false;

    public ModificationAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BPBlockEntities.MODIFICATION_TABLE.get(), pos, blockState);
    }

    public List<Attribute.Rune> getRunes(ModificationAltarBlockEntity entity) {
        ItemStack stack = entity.getHeldStack();

        if (stack.getCapability(AttributeProvider.ATTRIBUTE).isPresent()) {
            Attribute attribute = stack.getCapability(AttributeProvider.ATTRIBUTE).resolve().get();
            return attribute.getAllRunes();
        } else if (stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).isPresent()) {
            Attribute attribute = stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).resolve().get();
            return attribute.getAllRunes();
        }


        return List.of();
    }


    public static void tick(Level level, BlockPos pos, BlockState blockState, ModificationAltarBlockEntity entity) {
        if (!level.isClientSide) {
            blockState = blockState.setValue(ModificationAltarBlock.ALTER, entity.alter);
            level.setBlock(pos, blockState, 3);
            entity.updateBlock();
        }
    }

    @Override
    public void load(CompoundTag compound) {
        heldStack = ItemStack.of((CompoundTag) compound.get("itemStack"));
        alter = compound.getBoolean("alter");
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (heldStack != null) {
            CompoundTag reagentTag = new CompoundTag();
            heldStack.save(reagentTag);
            tag.put("itemStack", reagentTag);
        }
        tag.putBoolean("alter", alter);


    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("alter", alter);
        this.saveAdditional(tag);
        return tag;
    }


    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        if (level.isClientSide) return true;

        this.alter = !this.alter;


        return true;
    }


    public static class Hud {
        public static void render(ModificationAltarBlockEntity center, GuiGraphics gui, Minecraft mc) {
            PoseStack ms = gui.pose();
            int xc = mc.getWindow().getGuiScaledWidth() / 2;
            int yc = mc.getWindow().getGuiScaledHeight() / 2;


            ItemStack heldStack = center.getHeldStack();
            List<ItemStack> runes = center.getRunes(center).stream().map(Attribute.Rune::getAsStack).toList();
            float angle = -90;
            int radius = 48;

            if (!runes.isEmpty()) {
                float anglePer = 360F / runes.size();

                for (ItemStack rune : runes) {
                    double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
                    double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
                    ms.pushPose();
                    ms.translate(xPos, yPos, 0);
                    gui.renderFakeItem(rune, 0, 0);
                    ms.popPose();

                    angle += anglePer;
                }

                if (!heldStack.isEmpty()) {
                    ms.pushPose();
                    ms.translate(xc - 8, yc - 8, 0);
                    gui.renderFakeItem(heldStack, 0, 0);
                    ms.popPose();
                }
            }
        }
    }
}
