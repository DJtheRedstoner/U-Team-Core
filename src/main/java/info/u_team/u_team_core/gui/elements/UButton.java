package info.u_team.u_team_core.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.u_team_core.api.gui.IBackgroundColorProvider;
import info.u_team.u_team_core.api.gui.IPerspectiveRenderable;
import info.u_team.u_team_core.api.gui.ITextProvider;
import info.u_team.u_team_core.api.gui.ITextureProvider;
import info.u_team.u_team_core.util.RGBA;
import info.u_team.u_team_core.util.WidgetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;

/**
 * A button that fixes vanilla not drawing the continuous border if the button is smaller than 20. Also adds utility
 * methods to add an IPressable and ITooltip
 * 
 * @author HyCraftHD
 */
public class UButton extends Button implements IPerspectiveRenderable, IBackgroundColorProvider, ITextProvider {
	
	protected static final OnPress EMTPY_PRESSABLE = button -> {
	};
	
	protected static final OnTooltip EMPTY_TOOLTIP = Button.NO_TOOLTIP;
	
	protected static final RGBA WHITE = RGBA.WHITE;
	protected static final RGBA LIGHT_GRAY = new RGBA(0xA0A0A0FF);
	
	protected ITextureProvider buttonTextureProvider;
	
	protected RGBA buttonColor;
	
	protected RGBA textColor;
	protected RGBA disabledTextColor;
	
	public UButton(int x, int y, int width, int height, Component text) {
		this(x, y, width, height, text, EMTPY_PRESSABLE);
	}
	
	public UButton(int x, int y, int width, int height, Component text, OnPress pessable) {
		this(x, y, width, height, text, pessable, EMPTY_TOOLTIP);
	}
	
	public UButton(int x, int y, int width, int height, Component text, OnTooltip tooltip) {
		this(x, y, width, height, text, EMTPY_PRESSABLE, tooltip);
	}
	
	public UButton(int x, int y, int width, int height, Component text, OnPress pessable, OnTooltip tooltip) {
		super(x, y, width, height, text, pessable);
		onTooltip = tooltip;
		buttonTextureProvider = new WidgetTextureProvider(this, this::getYImage);
		buttonColor = WHITE;
		textColor = WHITE;
		disabledTextColor = LIGHT_GRAY;
	}
	
	public void setPressable(OnPress pressable) {
		onPress = pressable;
	}
	
	public void setPressable(Runnable runnable) {
		onPress = button -> runnable.run();
	}
	
	public void setTooltip(OnTooltip tooltip) {
		onTooltip = tooltip;
	}
	
	public RGBA getButtonColor() {
		return buttonColor;
	}
	
	public void setButtonColor(RGBA buttonColor) {
		this.buttonColor = buttonColor;
	}
	
	public RGBA getTextColor() {
		return textColor;
	}
	
	public void setTextColor(RGBA textColor) {
		this.textColor = textColor;
	}
	
	public RGBA getDisabledTextColor() {
		return disabledTextColor;
	}
	
	public void setDisabledTextColor(RGBA disabledTextColor) {
		this.disabledTextColor = disabledTextColor;
	}
	
	@Override
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		WidgetUtil.renderButtonLikeWidget(this, buttonTextureProvider, matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderBackground(PoseStack matrixStack, Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		renderBg(matrixStack, minecraft, mouseX, mouseY);
	}
	
	@Override
	public void renderForeground(PoseStack matrixStack, Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		WidgetUtil.renderText(this, matrixStack, minecraft, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderToolTip(PoseStack matrixStack, Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		renderToolTip(matrixStack, mouseX, mouseY);
	}
	
	@Override
	public RGBA getCurrentBackgroundColor(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		return buttonColor;
	}
	
	@Override
	public Component getCurrentText() {
		return getMessage();
	}
	
	@Override
	public RGBA getCurrentTextColor(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		return active ? textColor : disabledTextColor;
	}
	
}
