package yerova.botanicpledge.common.items.relic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.common.item.relic.ItemRelic;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;

import java.util.ArrayList;
import java.util.UUID;


public class MariasCore extends ItemRelic implements ICurioItem, ICurioRenderer {

    private static final int maxShield = 4000;
    private static final int defRegenPerTick = 40;
    private static final int maxCharge = 1_000_000;

    private static final String TAG_CORE_UUID = "coreUUID";
    private static final String TAG_STATS_SUBSTAT = BotanicPledge.MOD_ID + ".stats";

    public static final DamageSource HEALTH_SET_DMG_SRC = new DamageSource("health_set");

    private static ArrayList<Attribute> attributeList() {
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        list.add(Attributes.ARMOR);
        list.add(Attributes.ARMOR_TOUGHNESS);
        list.add(Attributes.MAX_HEALTH);
        list.add(Attributes.ATTACK_DAMAGE);
        list.add(Attributes.KNOCKBACK_RESISTANCE);
        list.add(Attributes.MOVEMENT_SPEED);
        list.add(Attributes.ATTACK_SPEED);


        return list;
    }

    private static ArrayList<String> attributeNameList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("armor");
        list.add("armor_toughness");
        list.add("max_health");
        list.add("attack_damage");
        list.add("knockback_resistance");
        list.add("movement_speed");
        list.add("attack_speed");

        return list;
    }

    public MariasCore(Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }


    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity living = slotContext.entity();

        ICurioRenderer.translateIfSneaking(matrixStack, living);
        ICurioRenderer.rotateIfSneaking(matrixStack, living);

        //TODO: Do more Rendering stuff
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {

        for (int i = 0; i < attributeList().size(); i++) {
            double addValue = stack.getOrCreateTagElement(TAG_STATS_SUBSTAT).getInt(attributeNameList().get(i));
            AttributeModifier statModifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", addValue, AttributeModifier.Operation.ADDITION);

            if (!slotContext.entity().getAttribute(attributeList().get(i)).hasModifier(statModifier)) {
                slotContext.entity().getAttribute(attributeList().get(i)).addPermanentModifier(statModifier);
            }
        }
    }


    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {


        for (int i = 0; i < attributeList().size(); i++) {
            double reducerValue = stack.getOrCreateTagElement(TAG_STATS_SUBSTAT).getInt(attributeNameList().get(i));
            AttributeModifier statModifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", reducerValue, AttributeModifier.Operation.ADDITION);

            if (slotContext.entity().getAttribute(attributeList().get(i)).hasModifier(statModifier)) {
                slotContext.entity().getAttribute(attributeList().get(i)).removeModifier(statModifier);

                if (attributeNameList().get(i).equals(attributeNameList().get(2))) {
/*                    BotanicPledge.LOGGER.info("doing stuff");
                    if(slotContext.entity().getHealth() > slotContext.entity().getMaxHealth()) {
                        slotContext.entity().hurt(HEALTH_SET_DMG_SRC, slotContext.entity().getAbsorptionAmount() + (float) reducerValue);
                    }*/
                }
            }


        }


        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public static UUID getCoreUUID(ItemStack stack) {
        var tag = stack.getOrCreateTagElement(TAG_STATS_SUBSTAT);

        // Legacy handling
        String tagCoreUuidMostLegacy = "coreUUIDMost";
        String tagCoreUuidLeastLegacy = "coreUUIDLeast";
        if (tag.contains(tagCoreUuidMostLegacy) && tag.contains(tagCoreUuidLeastLegacy)) {
            UUID uuid = new UUID(tag.getLong(tagCoreUuidMostLegacy), tag.getLong(tagCoreUuidLeastLegacy));
            tag.putUUID(TAG_CORE_UUID, uuid);
        }

        if (!tag.hasUUID(TAG_CORE_UUID)) {
            UUID uuid = UUID.randomUUID();
            tag.putUUID(TAG_CORE_UUID, uuid);
        }

        return tag.getUUID(TAG_CORE_UUID);
    }
}
