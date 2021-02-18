package me.lilac.teagarden.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import java.util.List;

public class TeaCupItem extends Item {

    public TeaCupItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerEntity = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
        }

        if (!worldIn.isRemote) {
            for (EffectInstance effectInstance : PotionUtils.getEffectsFromStack(stack)) {
                if (effectInstance.getPotion().isInstant()) {
                    effectInstance.getPotion().affectEntity(playerEntity, playerEntity, entityLiving, effectInstance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addPotionEffect(new EffectInstance(effectInstance));
                }
            }
        }

        if (playerEntity != null) {
            playerEntity.addStat(Stats.ITEM_USED.get(this));
            if (!playerEntity.abilities.isCreativeMode) stack.shrink(1);
        }

        if (playerEntity == null || !playerEntity.abilities.isCreativeMode) {
            if (stack.isEmpty()) return new ItemStack(ItemRegistry.TEA_CUP.get());
            if (playerEntity != null) {
                playerEntity.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.TEA_CUP.get()));
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return (stack.hasTag() && stack.getTag().contains("Speed")) ? stack.getTag().getInt("Speed") : 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return PotionUtils.getEffectsFromStack(stack).isEmpty() ? UseAction.NONE : UseAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return PotionUtils.getEffectsFromStack(playerIn.getHeldItem(handIn)).isEmpty() ? ActionResult.resultFail(playerIn.getHeldItem(handIn)) : DrinkHelper.startDrinking(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        for (EffectInstance effect : PotionUtils.getEffectsFromStack(stack)) {
            tooltip.add(
                            new TranslationTextComponent(effect.getEffectName()).mergeStyle(TextFormatting.AQUA)
                            .append(new StringTextComponent(" "))
                            .append(new TranslationTextComponent("potion.potency." + effect.getAmplifier()).mergeStyle(TextFormatting.WHITE))
                            .append(new StringTextComponent(effect.getAmplifier() == 0 ? "" : " "))
                            .append(new StringTextComponent("[").mergeStyle(TextFormatting.DARK_PURPLE))
                            .append(new StringTextComponent("" + EffectUtils.getPotionDurationString(effect.getEffectInstance(), 1.0F)).mergeStyle(TextFormatting.LIGHT_PURPLE))
                            .append(new StringTextComponent("]").mergeStyle(TextFormatting.DARK_PURPLE))
            );
        }

        if (!PotionUtils.getEffectsFromStack(stack).isEmpty()) {
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new TranslationTextComponent("teacup.drinkingTime", (stack.getOrCreateTag().getInt("Speed") * 0.05)));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
