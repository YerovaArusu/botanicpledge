package yerova.botanicpledge.common.items.armor;

import com.google.common.base.Suppliers;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.BotaniaTags;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class YggdrasilsteelArmor extends TerrasteelArmorItem {

    public static final MobEffect[] potions = {
            MobEffects.BLINDNESS,
            MobEffects.WITHER,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.WEAKNESS,
            MobEffects.POISON,
            MobEffects.CONFUSION
    };

    private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
            new ItemStack(BPItems.YGGDRASIL_HELMET.get()),
            new ItemStack(BPItems.YGGDRASIL_CHESTPLATE.get()),
            new ItemStack(BPItems.YGGDRASIL_LEGGINGS.get()),
            new ItemStack(BPItems.YGGDRASIL_BOOTS.get())
    });
    public YggdrasilsteelArmor(ArmorItem.Type type, Properties props) {
        super(type, props.durability(6400));
        MinecraftForge.EVENT_BUS.addListener(this::onJump);
    }

    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return BotanicPledge.MOD_ID + ":textures/model/armor_yggdrasilsteel.png";
    }


    @Override
    public MutableComponent getArmorSetName() {
        return Component.translatable("botanicpledge.armorset.terrasteel.name");
    }

    private void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() == BPItems.YGGDRASIL_BOOTS.get()) {
            LivingEntity entity = event.getEntity();

            float rot = entity.getYRot() * ((float)Math.PI / 180F);
            float xzFactor = entity.isSprinting() ? 0.2f : 0;
            entity.setDeltaMovement(entity.getDeltaMovement().add(-Mth.sin(rot) * xzFactor, 0.2, Mth.cos(rot) * xzFactor));
        }
    }

    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
        return CommonYggdrasilsteelArmor.applyModifiers(this, super.getDefaultAttributeModifiers(slot), slot);
    }

    public int getManaPerDamage() {
        return 25;
    }

    @SuppressWarnings({"deprecation","removal"})
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.isClientSide)return;

        if (stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, this.getManaPerDamage(), true)) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 2));
        }

        ManaItemHandler.instance().dispatchManaExact(stack, player, 250, true);
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
        return repair.getItem() == BPItems.YGGDRALIUM_INGOT.get() || (!Ingredient.of(BotaniaTags.Items.INGOTS_TERRASTEEL).test(repair) && super.isValidRepairItem(toRepair, repair));
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
