package yerova.botanicpledge.datagen;


import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

public class BPItemModelProvider extends ItemModelProvider {
    public BPItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BotanicPledge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        evenSimplerBlockItem(BPBlocks.YGGDRASIL_STAIRS);
        evenSimplerBlockItem(BPBlocks.YGGDRASIL_SLAB);
        evenSimplerBlockItem(BPBlocks.YGGDRASIL_WOOD);
        evenSimplerBlockItem(BPBlocks.YGGDRASIL_LOG);
        evenSimplerBlockItem(BPBlocks.STRIPPED_YGGDRASIL_LOG);
        evenSimplerBlockItem(BPBlocks.STRIPPED_YGGDRASIL_WOOD);
        evenSimplerBlockItem(BPBlocks.YGGDRASIL_LEAVES);
        evenSimplerBlockItem(BPBlocks.YGGDRASIL_PLANKS);
        evenSimplerBlockItem(BPBlocks.YGGDRASIL_FENCE_GATE);
        simpleBlockItem(BPBlocks.YGGDRASIL_DOOR);

        // Register special cases
        trapdoorItem(BPBlocks.YGGDRASIL_TRAPDOOR);
        fenceItem(BPBlocks.YGGDRASIL_FENCE, BPBlocks.YGGDRASIL_PLANKS);
        wallItem(BPBlocks.YGGDRASIL_WALL, BPBlocks.YGGDRASIL_PLANKS);
        saplingItem(BPBlocks.YGGDRASIL_SAPLING);
    }
    private ItemModelBuilder saplingItem(RegistryObject<? extends Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BotanicPledge.MOD_ID,"block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<? extends Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BotanicPledge.MOD_ID,"item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<? extends Block> block) {
        this.withExistingParent(BotanicPledge.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<? extends Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<? extends Block> block, RegistryObject<? extends Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(BotanicPledge.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<? extends Block> block, RegistryObject<? extends Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  new ResourceLocation(BotanicPledge.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<? extends Block> block, RegistryObject<? extends Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(BotanicPledge.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<? extends Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BotanicPledge.MOD_ID,"item/" + item.getId().getPath()));
    }
}