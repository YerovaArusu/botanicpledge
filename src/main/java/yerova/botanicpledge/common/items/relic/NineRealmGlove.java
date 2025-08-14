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
import yerova.botanicpledge.common.aura_node.essence.*;
import yerova.botanicpledge.setup.BPEssences;
import yerova.botanicpledge.setup.BPItems;

import java.util.ArrayList;
import java.util.List;

public class NineRealmGlove extends RelicItem {


    private static final String ESSENCE_TAG = "BotanicPledgeEssences";
    private static final String SELECTED_ESSENCE_INDEX_TAG = "SelectedEssenceIndex";
    private static final int ESSENCE_AMOUNT_TO_GET_PER_INTERACTION = 1;


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

            if (player != null && player.isShiftKeyDown()) {
                return handleShiftRightClickOnBlock(world, blockEntity,stack,player);
            } else {
                return rightClickOnBLock(world,blockEntity,stack,player);
            }
        }

        return super.useOn(pContext);
    }


    public InteractionResult handleShiftRightClickOnBlock(Level world, BlockEntity be, ItemStack stack, Player player) {
        if (!(be instanceof IEssenceHolder<?>)) return InteractionResult.FAIL;
        if (player == null || !player.isShiftKeyDown()) return InteractionResult.FAIL;


        Essence essence = getSelectedEssence(stack);

        if (be instanceof IAuraNode node) {
            if (node.getImplementation() == null) {
                AuraImplementation implementation = new AuraImplementation();
                implementation.setType(AuraNodeType.getRandomType());
                node.setImplementation(implementation);
            }

            AuraImplementation imp = node.getImplementation();


            if (essence == null) return InteractionResult.FAIL;
            if (imp.addEssence(essence, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION)) {
                removeEssence(stack, essence, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
            }

            be.setChanged();
            world.sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);

            return InteractionResult.SUCCESS;
        }
        if (be instanceof IEssenceCapacitor node) {
            if (node.getImplementation() == null) {
                EssenceCapacitorImplementation implementation = new EssenceCapacitorImplementation();
                node.setImplementation(implementation);
            }


            EssenceCapacitorImplementation imp = node.getImplementation();

            if (essence == null) return InteractionResult.FAIL;
            if (imp.addEssence(essence, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION)) {
                removeEssence(stack, essence, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
            }

            be.setChanged();
            world.sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);

            return InteractionResult.SUCCESS;

        }
        return InteractionResult.FAIL;
    }

    public InteractionResult rightClickOnBLock(Level world, BlockEntity be, ItemStack stack, Player player) {
        if (!(be instanceof IEssenceHolder<?>)) return InteractionResult.FAIL;
        if (player == null || player.isShiftKeyDown()) return InteractionResult.FAIL;

        if (((IEssenceHolder<?>) be).getImplementation() == null) return InteractionResult.FAIL;
        Essence selected = getSelectedEssence(stack);



        if (be instanceof IAuraNode node) {
            AuraImplementation imp = node.getImplementation();

            Essence toGet = null;

            if (selected != null) {
                int selectedAmount = imp.getEssenceAmount(selected);
                if (selected == imp.getBaseEssence() && imp.getBaseEssenceAmount() >= ESSENCE_AMOUNT_TO_GET_PER_INTERACTION) {
                    imp.setBaseEssenceAmount(imp.getBaseEssenceAmount() - ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                    toGet = selected;
                } else if (selectedAmount >= ESSENCE_AMOUNT_TO_GET_PER_INTERACTION) {
                    imp.removeEssence(selected, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                    toGet = selected;
                }
            }

            if (toGet == null || toGet == BPEssences.EMPTY_ESSENCE.get()) {
                toGet = imp.removeFirstEssence(ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);

                if ((toGet == null || toGet == BPEssences.EMPTY_ESSENCE.get()) && imp.getBaseEssenceAmount() >= ESSENCE_AMOUNT_TO_GET_PER_INTERACTION) {
                    imp.setBaseEssenceAmount(imp.getBaseEssenceAmount() - ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                    toGet = imp.getBaseEssence();
                }
            }

            if (toGet != null && toGet != BPEssences.EMPTY_ESSENCE.get()) {
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                addEssence(stack, toGet, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                return InteractionResult.SUCCESS;
            }

            if (toGet == BPEssences.EMPTY_ESSENCE.get() || (imp.getBaseEssenceAmount() <= 0 && imp.getEssenceList().isEmpty())) {
                node.setImplementation(null);
                be.setChanged();
                world.sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }

        } else if (be instanceof IEssenceCapacitor node) {
            EssenceCapacitorImplementation imp = node.getImplementation();

            Essence toGet = null;

            if (selected != null) {
                int selectedAmount = imp.getEssenceAmount(selected);

                if (selectedAmount >= ESSENCE_AMOUNT_TO_GET_PER_INTERACTION) {
                    imp.removeEssence(selected, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                    toGet = selected;
                }
            }

            if (toGet == null || toGet == BPEssences.EMPTY_ESSENCE.get()) {
                toGet = imp.removeFirstEssence(ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);

                if ((toGet == null || toGet == BPEssences.EMPTY_ESSENCE.get()) && imp.getEssenceAmount(imp.getFirstEssence()) >= ESSENCE_AMOUNT_TO_GET_PER_INTERACTION) {
                    imp.removeEssence(imp.getFirstEssence(), ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                    toGet = imp.getFirstEssence();

                }
            }

            if (toGet != null && toGet != BPEssences.EMPTY_ESSENCE.get()) {
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                addEssence(stack, toGet, ESSENCE_AMOUNT_TO_GET_PER_INTERACTION);
                return InteractionResult.SUCCESS;
            }

            if (toGet == BPEssences.EMPTY_ESSENCE.get() || (imp.getEssenceCount() <= 0 && imp.getEssenceList().isEmpty())) {
                node.setImplementation(null);
                be.setChanged();
                world.sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }


        return InteractionResult.FAIL;
    }
}