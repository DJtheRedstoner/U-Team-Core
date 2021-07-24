package info.u_team.u_team_core.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import info.u_team.u_team_core.container.FluidContainer;
import info.u_team.u_team_core.container.FluidSlot;
import info.u_team.u_team_core.gui.renderer.FluidInventoryRenderer;
import info.u_team.u_team_core.intern.init.UCoreNetwork;
import info.u_team.u_team_core.intern.network.FluidClickContainerMessage;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class FluidContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
	
	private static final FluidInventoryRenderer FLUID_RENDERER = new FluidInventoryRenderer();
	
	protected FluidInventoryRenderer fluidRenderer;
	
	protected FluidSlot hoveredFluidSlot;
	
	public FluidContainerScreen(T container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		setFluidRenderer(FLUID_RENDERER);
	}
	
	protected void setFluidRenderer(FluidInventoryRenderer fluidRenderer) {
		this.fluidRenderer = fluidRenderer;
	}
	
	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		if (menu instanceof FluidContainer) {
			hoveredFluidSlot = null;
			
			final FluidContainer fluidContainer = (FluidContainer) menu;
			for (int index = 0; index < fluidContainer.fluidSlots.size(); index++) {
				
				final FluidSlot fluidSlot = fluidContainer.fluidSlots.get(index);
				
				if (fluidSlot.isEnabled()) {
					drawFluidSlot(matrixStack, fluidSlot);
					
					if (isFluidSlotSelected(fluidSlot, mouseX, mouseY)) {
						hoveredFluidSlot = fluidSlot;
						final int x = fluidSlot.getX();
						final int y = fluidSlot.getY();
						RenderSystem.disableDepthTest();
						RenderSystem.colorMask(true, true, true, false);
						final int slotColor = getFluidSlotColor(index);
						fillGradient(matrixStack, x, y, x + 16, y + 16, slotColor, slotColor);
						RenderSystem.colorMask(true, true, true, true);
						RenderSystem.enableDepthTest();
					}
				}
			}
		}
	}
	
	@Override
	protected void renderTooltip(PoseStack matrixStack, int mouseX, int mouseY) {
		super.renderTooltip(matrixStack, mouseX, mouseY);
		
		if (minecraft.player.inventory.getCarried().isEmpty() && hoveredFluidSlot != null && !hoveredFluidSlot.getStack().isEmpty()) {
			renderComponentTooltip(matrixStack, getTooltipFromFluid(hoveredFluidSlot), mouseX, mouseY);
		}
		
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			final FluidSlot fluidSlot = getSelectedFluidSlot(mouseX, mouseY);
			if (fluidSlot != null) {
				if (!inventory.getCarried().isEmpty()) {
					UCoreNetwork.NETWORK.sendToServer(new FluidClickContainerMessage(menu.containerId, fluidSlot.slotNumber, hasShiftDown(), inventory.getCarried()));
				}
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	protected void drawFluidSlot(PoseStack matrixStack, FluidSlot fluidSlot) {
		fluidRenderer.drawFluid(matrixStack, fluidSlot.getX(), fluidSlot.getY(), fluidSlot.getStack());
	}
	
	protected boolean isFluidSlotSelected(FluidSlot fluidSlot, double mouseX, double mouseY) {
		return isHovering(fluidSlot.getX(), fluidSlot.getY(), 16, 16, mouseX, mouseY);
	}
	
	public int getFluidSlotColor(int index) {
		return super.getSlotColor(index);
	}
	
	public List<Component> getTooltipFromFluid(FluidSlot fluidSlot) {
		final FluidStack stack = fluidSlot.getStack();
		
		final List<Component> list = new ArrayList<>();
		
		list.add(stack.getDisplayName());
		list.add(new TextComponent(stack.getAmount() + " / " + fluidSlot.getSlotCapacity()).withStyle(ChatFormatting.GRAY));
		
		if (minecraft.options.advancedItemTooltips) {
			list.add((new TextComponent(ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString())).withStyle(ChatFormatting.DARK_GRAY));
		}
		
		return list;
	}
	
	private FluidSlot getSelectedFluidSlot(double mouseX, double mouseY) {
		if (menu instanceof FluidContainer) {
			final FluidContainer fluidContainer = (FluidContainer) menu;
			for (int index = 0; index < fluidContainer.fluidSlots.size(); index++) {
				final FluidSlot fluidSlot = fluidContainer.fluidSlots.get(index);
				if (isFluidSlotSelected(fluidSlot, mouseX, mouseY) && fluidSlot.isEnabled()) {
					return fluidSlot;
				}
			}
		}
		return null;
	}
}
