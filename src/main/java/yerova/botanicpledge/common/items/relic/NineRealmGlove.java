package yerova.botanicpledge.common.items.relic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.common.item.relic.RelicItem;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.IAuraNode;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;
import yerova.botanicpledge.setup.BPEssences;
import yerova.botanicpledge.setup.BPItems;

import java.util.ArrayList;
import java.util.List;

public class NineRealmGlove extends RelicItem {


    private static final String ESSENCE_TAG = "BotanicPledgeEssences";
    private static final String SELECTED_ESSENCE_INDEX_TAG = "SelectedEssenceIndex";

    public NineRealmGlove(Properties props) {
        super(props);

    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }


    public static EssenceList getEssenceList(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag essenceTag = tag.getCompound(ESSENCE_TAG);
        return EssenceList.fromNBT(essenceTag);
    }

    public static void saveEssenceList(ItemStack stack, EssenceList list) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(ESSENCE_TAG, list.toNBT());
    }

    public static void addEssence(ItemStack stack, Essence essence, int essenceAmount) {
        EssenceList list = getEssenceList(stack);
        list.addEssence(essence, essenceAmount);
        saveEssenceList(stack, list);
    }

    public static void removeEssence(ItemStack stack, Essence essence, int essenceAmount) {
        EssenceList list = getEssenceList(stack);
        list.removeEssence(essence, essenceAmount);
        saveEssenceList(stack, list);
    }

    public static int getSelectedIndex(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt(SELECTED_ESSENCE_INDEX_TAG);
    }

    public static void setSelectedIndex(ItemStack stack, int index) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(SELECTED_ESSENCE_INDEX_TAG, index);
    }

    public static Essence getSelectedEssence(ItemStack stack) {
        int selectedIndex = getSelectedIndex(stack);
        List<Essence> essences = new ArrayList<>(getEssenceList(stack).getEssenceMap().keySet());

        if (selectedIndex < 0 || selectedIndex >= essences.size()) return null;

        return essences.get(selectedIndex);
    }


    public static int cycleSelected(ItemStack stack, boolean forward) {
        if (!(stack.getItem() instanceof NineRealmGlove)) return -1;

        EssenceList list = getEssenceList(stack);

        if (list.size() <= 0) return -1;

        int selectedIndex = getSelectedIndex(stack);
        selectedIndex += forward ? -1 : 1;

        if (selectedIndex < 0) selectedIndex = list.size() - 1;
        if (selectedIndex >= list.size()) selectedIndex = 0;

        setSelectedIndex(stack, selectedIndex);
        return selectedIndex;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        Level world = pContext.getLevel();
        Player player = pContext.getPlayer();
        ItemStack stack = pContext.getItemInHand();
        BlockEntity blockEntity = world.getBlockEntity(pContext.getClickedPos());


        if (stack.is(BPItems.NINE_REALMS_GLOVE.get())) {


            if (blockEntity instanceof IAuraNode node) {

                if (player != null && player.isShiftKeyDown()) {
                    if (node.getImplementation() == null) {

                        AuraImplementation implementation = new AuraImplementation();
                        implementation.setType(AuraNodeType.getRandomType());
                        node.setImplementation(implementation);

                    }

                    AuraImplementation imp = node.getImplementation();

                    int essenceAmount = 1;
                    Essence essence = getSelectedEssence(stack);
                    if (essence == null) return InteractionResult.FAIL;
                    if(imp.addEssence(essence, essenceAmount)) {
                        removeEssence(stack, essence, essenceAmount);
                    }

                    blockEntity.setChanged();
                    world.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);

                    return InteractionResult.SUCCESS;

                } else {
                    if (node.getImplementation() == null) return InteractionResult.PASS;

                    AuraImplementation imp = node.getImplementation();
                    int amountToGet = 1;
                    Essence toGet = null;

                    Essence selected = getSelectedEssence(stack);
                    if (selected != null) {
                        int selectedAmount = imp.getEssenceAmount(selected);
                        if (selected == imp.getBaseEssence() && imp.getBaseEssenceAmount() >= amountToGet) {
                            imp.setBaseEssenceAmount(imp.getBaseEssenceAmount() - amountToGet);
                            toGet = selected;
                        } else if (selectedAmount >= amountToGet) {
                            imp.removeEssence(selected, amountToGet);
                            toGet = selected;
                        }
                    }

                    if (toGet == null || toGet == BPEssences.EMPTY_ESSENCE.get()) {
                        toGet = imp.removeFirstEssence(amountToGet);

                        if ((toGet == null || toGet == BPEssences.EMPTY_ESSENCE.get()) && imp.getBaseEssenceAmount() >= amountToGet) {
                            imp.setBaseEssenceAmount(imp.getBaseEssenceAmount() - amountToGet);
                            toGet = imp.getBaseEssence();
                        }
                    }

                    if (toGet != null && toGet != BPEssences.EMPTY_ESSENCE.get()) {
                        if (player != null) {
                            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        }
                        addEssence(stack, toGet, amountToGet);
                    }

                    if (toGet == BPEssences.EMPTY_ESSENCE.get() || (imp.getBaseEssenceAmount() <= 0 && imp.getEssenceList().isEmpty())) {
                        node.setImplementation(null);
                        blockEntity.setChanged();
                        world.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }

                }
            }


        }

        return super.useOn(pContext);
    }
}
