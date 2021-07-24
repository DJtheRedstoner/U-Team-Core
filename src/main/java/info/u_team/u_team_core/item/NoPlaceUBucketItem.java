package info.u_team.u_team_core.item;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class NoPlaceUBucketItem extends UBucketItem {
	
	public NoPlaceUBucketItem(CreativeModeTab group, Properties properties, Supplier<? extends Fluid> fluid) {
		super(group, properties, fluid);
	}
	
	public NoPlaceUBucketItem(Properties properties, Supplier<? extends Fluid> fluid) {
		super(properties, fluid);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, player.getItemInHand(hand));
	}
	
}
