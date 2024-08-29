package yerova.botanicpledge.datagen;

import com.userofbricks.expanded_combat.item.ECItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import yerova.botanicpledge.integration.expanded_combat.ExpandedCombatPlugin;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.concurrent.CompletableFuture;

import static com.userofbricks.expanded_combat.item.ECItemTags.bindForgeSword;

public class ItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> YGGDRASILSTEEL_SWORD = bindForgeSword("yggdrasilsteel");


    public ItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, BotanicPledge.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.SWORDS).add(BPItems.ASGARD_FRACTAL.get());
        this.tag(ItemTags.SWORDS).add(BPItems.YGGD_RAMUS.get());
        this.tag(Tags.Items.INGOTS).add(BPItems.YGGDRALIUM_INGOT.get());
        this.tag(Tags.Items.NUGGETS).add(BPItems.YGGDRALIUM_NUGGET.get());

        this.tag(YGGDRASILSTEEL_SWORD).add(BotaniaItems.terraSword);

        this.tag(ECItemTags.bindForgeStorageBlock(ExpandedCombatPlugin.YGGDRASILSTEEL.getLocationName().getPath())).add(BlockItem.byBlock(BPBlocks.YGGDRALIUM_BLOCK.get()));
    }
}
