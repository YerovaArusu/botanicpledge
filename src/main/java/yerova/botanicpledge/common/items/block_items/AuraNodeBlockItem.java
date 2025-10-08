package yerova.botanicpledge.common.items.block_items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import vazkii.botania.common.item.CustomCreativeTabContents;
import yerova.botanicpledge.client.render.items.AuraNodeItemRenderer;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BPEssences;

import java.util.List;
import java.util.function.Consumer;

public class AuraNodeBlockItem extends BlockItem implements CustomCreativeTabContents {

    public AuraNodeBlockItem(Properties props) {
        super(BPBlocks.AURA_NODE.get(), props);
    }

    @Override
    public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
        List<Essence> essences = Essence.getRegisteredEssences().stream().filter(e -> !e.equals(BPEssences.EMPTY_ESSENCE.get())).toList();

        int nodeCount = AuraNodeType.values().length;

        for (int i = 0; i < nodeCount; i++) {
            ItemStack stack = new ItemStack(me);
            AuraImplementation auraData = new AuraImplementation();

            Essence base = essences.get(i % essences.size());
            auraData.setBaseEssence(base, 10);

            auraData.setNodeRank(i);

            for (int j = 0; j <= auraData.getNodeRank(); j++) {
                Essence extra = essences.get((essences.size() - 1 - i) % essences.size());
                auraData.addEssence(extra, 10);
            }

            auraData.setType(AuraNodeType.values()[i]);
            CompoundTag beTag = new CompoundTag();
            beTag.put("Aura", auraData.toNBT());

            stack.getOrCreateTag().put("BlockEntityTag", beTag);
            output.accept(stack);
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AuraNodeItemRenderer.instance;
            }
        });
        super.initializeClient(consumer);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains("BlockEntityTag")) return super.getName(stack);

        CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");

        if (blockEntityTag.contains("Aura")) {
            AuraImplementation auraData = AuraImplementation.fromNBT(new AuraImplementation(), blockEntityTag.getCompound("Aura"));
            AuraNodeType type = auraData.getType();

            if (type != null) {
                String typeName = type.name().substring(0, 1).toUpperCase() + type.name().substring(1).toLowerCase();
                return Component.literal(typeName + " Node");
            }
        }

        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!stack.hasTag() || !stack.getTag().contains("BlockEntityTag")) return;
        CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");

        if (!blockEntityTag.contains("Aura")) return;

        AuraImplementation auraData = new AuraImplementation();
        auraData = AuraImplementation.fromNBT(auraData, blockEntityTag.getCompound("Aura"));
        if (auraData == null) return;

        Essence baseEssence = auraData.getBaseEssence();
        int rank = auraData.getNodeRank();
        List<Essence> contained = auraData.getEssenceList().getEssenceTypes().stream().toList();

        tooltip.add(
                Component.literal("")
                        .append(baseEssence.itemBase().getDefaultInstance().getHoverName())
                        .append(Component.literal(" x" + auraData.getBaseEssenceAmount())
                                .withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY)))
                        .withStyle(style -> style.withColor(baseEssence.color())));


        if (Screen.hasShiftDown()) {
            if (!contained.isEmpty()) {
                for (Essence e : contained) {
                    tooltip.add(
                            Component.literal(" - ")
                                    .append(e.itemBase().getDefaultInstance().getHoverName())
                                    .append(Component.literal(" x" + auraData.getEssenceAmount(e))
                                            .withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY)))
                                    .withStyle(style -> style.withColor(e.color())));
                }
            }

            tooltip.add(Component.translatable("tooltip.botanicpledge.aura_node.rank")
                    .append(": ")
                    .append(Component.literal(String.valueOf(rank)).withStyle(ChatFormatting.GRAY)));
        } else {
            tooltip.add(Component.literal("Hold §eSHIFT§r to show contents").withStyle(ChatFormatting.GRAY));
        }
    }


}
