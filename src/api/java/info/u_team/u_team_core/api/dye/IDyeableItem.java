package info.u_team.u_team_core.api.dye;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Attach this to any item that can be colors. Automatically adds dye color recipes. Call addColoredItem(Item) to also
 * register the item to the item color manager.
 * 
 * @author HyCraftHD
 */
public interface IDyeableItem {
	
	default <T extends Item & IDyeableItem> void addColoredItem(T item) {
		DyeableItemsRegistry.addItem(item);
	}
	
	default boolean hasColor(ItemStack stack) {
		final CompoundTag compound = stack.getTagElement("display");
		return compound != null && compound.contains("color", 99);
	}
	
	default int getColor(ItemStack stack) {
		final CompoundTag compound = stack.getTagElement("display");
		return compound != null && compound.contains("color", 99) ? compound.getInt("color") : getDefaultColor();
	}
	
	default void removeColor(ItemStack stack) {
		final CompoundTag compound = stack.getTagElement("display");
		if (compound != null && compound.contains("color")) {
			compound.remove("color");
		}
	}
	
	default void setColor(ItemStack stack, int color) {
		stack.getOrCreateTagElement("display").putInt("color", color);
	}
	
	default int getDefaultColor() {
		return 10511680;
	}
	
	public static ItemStack colorStack(ItemStack stack, List<DyeColor> dyeList) {
		if (!(stack.getItem() instanceof IDyeableItem)) {
			return ItemStack.EMPTY;
		}
		final IDyeableItem dyeableItem = (IDyeableItem) stack.getItem();
		final ItemStack dyedStack = stack.copy();
		dyedStack.setCount(1);
		
		final int[] rbgItemSum = new int[3];
		int mostIntenseChannelSum = 0;
		int colorItemSum = 0;
		
		if (dyeableItem.hasColor(dyedStack)) {
			final int color = dyeableItem.getColor(dyedStack);
			
			final float red = (color >> 16 & 255) / 255.0F;
			final float green = (color >> 8 & 255) / 255.0F;
			final float blue = (color & 255) / 255.0F;
			
			mostIntenseChannelSum = (int) (mostIntenseChannelSum + Math.max(red, Math.max(green, blue)) * 255.0F);
			
			rbgItemSum[0] = (int) (rbgItemSum[0] + red * 255.0F);
			rbgItemSum[1] = (int) (rbgItemSum[1] + green * 255.0F);
			rbgItemSum[2] = (int) (rbgItemSum[2] + blue * 255.0F);
			
			++colorItemSum;
		}
		
		for (final DyeColor dye : dyeList) {
			final float[] colorComponents = dye.getTextureDiffuseColors();
			
			final int red = (int) (colorComponents[0] * 255.0F);
			final int green = (int) (colorComponents[1] * 255.0F);
			final int blue = (int) (colorComponents[2] * 255.0F);
			
			mostIntenseChannelSum += Math.max(red, Math.max(green, blue));
			
			rbgItemSum[0] += red;
			rbgItemSum[1] += green;
			rbgItemSum[2] += blue;
			
			++colorItemSum;
		}
		
		int red = rbgItemSum[0] / colorItemSum;
		int green = rbgItemSum[1] / colorItemSum;
		int blue = rbgItemSum[2] / colorItemSum;
		
		final float averageChannel = mostIntenseChannelSum / (float) colorItemSum; // float division
		final float mostIntenseChannel = Math.max(red, Math.max(green, blue));
		
		red = (int) (red * averageChannel / mostIntenseChannel);
		green = (int) (green * averageChannel / mostIntenseChannel);
		blue = (int) (blue * averageChannel / mostIntenseChannel);
		
		int finalColor = (red << 8) + green;
		finalColor = (finalColor << 8) + blue;
		
		dyeableItem.setColor(dyedStack, finalColor);
		return dyedStack;
	}
	
	public static ItemStack colorStackDyeItem(ItemStack stack, List<DyeItem> dyeItemList) {
		return colorStack(stack, dyeItemList.parallelStream().map(dyeItem -> dyeItem.getDyeColor()).collect(Collectors.toList()));
	}
}
