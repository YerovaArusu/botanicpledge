package yerova.botanicpledge.common.items.armor;

import com.google.common.base.Suppliers;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.BotaniaTags;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class YggdrasilsteelHelmet extends TerrasteelHelmItem {

    public static final String PIXIE_COUNT_TAG = "pixies";
    public static final int MAX_PIXIES = 8;


    private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
            new ItemStack(BPItems.YGGDRASIL_HELMET.get()),
            new ItemStack(BPItems.YGGDRASIL_CHESTPLATE.get()),
            new ItemStack(BPItems.YGGDRASIL_LEGGINGS.get()),
            new ItemStack(BPItems.YGGDRASIL_BOOTS.get())
    });
    public YggdrasilsteelHelmet(Properties props) {
        super(props);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClientSide && entity instanceof Player player
                && player.getInventory().armor.contains(stack)
                && hasArmorSet(player)) {
            int food = player.getFoodData().getFoodLevel();
            if (food > 0 && food < 18 && player.isHurt() && player.tickCount % 80 == 0) {
                player.heal(3F);
            }
            if (player.tickCount % 10 == 0) {
                ManaItemHandler.instance().dispatchManaExact(stack, player, 30, true);
            }
        }
    }

    @Override
    public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
        return hasArmorSet(player) ? 0.8F : 0F;
    }

    public static void setPixieCount(ItemStack stack, int count) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(PIXIE_COUNT_TAG, count);
    }

    public static void incrementPixieCount(ItemStack stack) {
        int count = getPixieCount(stack);
        setPixieCount(stack, Math.min(count+1, MAX_PIXIES));
    }

    public static void decrementPixieCount(ItemStack stack) {
        int count = getPixieCount(stack);
        setPixieCount(stack, Math.max(0, count-1));
    }

    public static int getPixieCount(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt(PIXIE_COUNT_TAG) : 0;
    }


    @Override
    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return BotanicPledge.MOD_ID + ":textures/model/armor_yggdrasilsteel.png";
    }


    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
        return CommonYggdrasilsteelArmor.applyModifiers(this, super.getDefaultAttributeModifiers(slot), slot);
    }

    public int getManaPerDamage() {
        return 10;
    }

    @SuppressWarnings({"deprecation","removal"})
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            if (stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, this.getManaPerDamage() * 2, true)) {
                stack.setDamageValue(Math.max(0, stack.getDamageValue() - 2));
            }
            int food = player.getFoodData().getFoodLevel();
            if (food > 0 && food < 18 && player.isHurt() && player.tickCount % 80 == 0) {
                player.heal(1);
            }
            if (player.tickCount % 10 == 0) {
                ManaItemHandler.instance().dispatchManaExact(stack, player, 10, true);
            }
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, this.getManaPerDamage());
    }

    @Override
    public ItemStack[] getArmorSetStacks() {
        return armorSet.get();
    }


    @Override
    public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
        return CommonYggdrasilsteelArmor.hasArmorSetItem(player, slot);
    }


    @OnlyIn(Dist.CLIENT)
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("botanicpledge.armorset.terrasteel.desc0").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botanicpledge.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("botanicpledge.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
        CommonYggdrasilsteelArmor.addArmorSetDescription(stack, list);
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == BPItems.YGGDRALIUM_INGOT.get()|| (!Ingredient.of(BotaniaTags.Items.INGOTS_TERRASTEEL).test(repair) && super.isValidRepairItem(toRepair, repair));
    }


    @Override
    public MutableComponent getArmorSetName() {
        return Component.translatable("botanicpledge.armorset.terrasteel.name");
    }

    @Override
    public int getDefense() {
        return CommonYggdrasilsteelArmor.getDefense(this.getType());
    }

    @Override
    public float getToughness() {
        return CommonYggdrasilsteelArmor.getToughness(this.getType());
    }
}
