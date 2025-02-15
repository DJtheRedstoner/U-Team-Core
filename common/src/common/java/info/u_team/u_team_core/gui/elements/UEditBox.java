package info.u_team.u_team_core.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.u_team_core.api.gui.BackgroundColorProvider;
import info.u_team.u_team_core.api.gui.PerspectiveRenderable;
import info.u_team.u_team_core.api.gui.RenderTickable;
import info.u_team.u_team_core.api.gui.TextSettingsProvider;
import info.u_team.u_team_core.util.RGBA;
import info.u_team.u_team_core.util.WidgetUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class UEditBox extends EditBox implements RenderTickable, PerspectiveRenderable, BackgroundColorProvider, TextSettingsProvider {
	
	protected static final RGBA BLACK = RGBA.BLACK;
	protected static final RGBA WHITE = RGBA.WHITE;
	
	protected static final RGBA LIGHT_GRAY = new RGBA(0xE0E0E0FF);
	protected static final RGBA LIGHTER_GRAY = new RGBA(0xD0D0D0FF);
	protected static final RGBA GRAY = new RGBA(0xA0A0A0FF);
	protected static final RGBA DARKER_GRAY = new RGBA(0x808080FF);
	protected static final RGBA DARK_GRAY = new RGBA(0x707070FF);
	
	protected RGBA backgroundFrameColor;
	protected RGBA unfocusedBackgroundFrameColor;
	protected RGBA backgroundColor;
	
	protected RGBA textColor;
	protected RGBA disabledTextColor;
	protected RGBA hintTextColor;
	protected RGBA suggestionTextColor;
	
	protected RGBA cursorColor;
	
	public UEditBox(Font font, int x, int y, int width, int height, UEditBox previousEditBox, Component title) {
		super(font, x, y, width, height, title);
		setPreviousText(previousEditBox);
		backgroundFrameColor = WHITE;
		unfocusedBackgroundFrameColor = GRAY;
		backgroundColor = BLACK;
		textColor = LIGHT_GRAY;
		disabledTextColor = DARK_GRAY;
		hintTextColor = LIGHT_GRAY;
		suggestionTextColor = DARKER_GRAY;
		cursorColor = LIGHTER_GRAY;
	}
	
	public RGBA getBackgroundFrameColor() {
		return backgroundFrameColor;
	}
	
	public void setBackgroundFrameColor(RGBA backgroundFrameColor) {
		this.backgroundFrameColor = backgroundFrameColor;
	}
	
	public RGBA getUnfocusedBackgroundFrameColor() {
		return unfocusedBackgroundFrameColor;
	}
	
	public void setUnfocusedBackgroundFrameColor(RGBA unfocusedBackgroundFrameColor) {
		this.unfocusedBackgroundFrameColor = unfocusedBackgroundFrameColor;
	}
	
	public RGBA getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(RGBA backgroundColor) {
		this.backgroundColor = backgroundColor;
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
	
	public RGBA getHintTextColor() {
		return hintTextColor;
	}
	
	public void setHintTextColor(RGBA hintTextColor) {
		this.hintTextColor = hintTextColor;
	}
	
	public RGBA getSuggestionTextColor() {
		return suggestionTextColor;
	}
	
	public void setSuggestionTextColor(RGBA suggestionTextColor) {
		this.suggestionTextColor = suggestionTextColor;
	}
	
	public RGBA getCursorColor() {
		return cursorColor;
	}
	
	public void setCursorColor(RGBA cursorColor) {
		this.cursorColor = cursorColor;
	}
	
	@Override
	public void setTextColor(int color) {
		super.setTextColor(color);
		setTextColor(RGBA.fromARGB(color));
	}
	
	@Override
	public void setTextColorUneditable(int color) {
		super.setTextColorUneditable(color);
		setDisabledTextColor(RGBA.fromARGB(color));
	}
	
	public void setPreviousText(UEditBox textField) {
		if (textField != null) {
			value = textField.value;
			maxLength = textField.maxLength;
			displayPos = textField.displayPos;
			cursorPos = textField.cursorPos;
			highlightPos = textField.highlightPos;
		}
	}
	
	@Override
	public void renderTick() {
		tick();
	}
	
	@Override
	public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		renderBackground(poseStack, mouseX, mouseY, partialTick);
		renderForeground(poseStack, mouseX, mouseY, partialTick);
	}
	
	@Override
	public void renderBackground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		if (bordered) {
			fill(poseStack, x - 1, y - 1, x + width + 1, y + height + 1, WidgetUtil.respectWidgetAlpha(this, getCurrentBackgroundFrameColor(poseStack, mouseX, mouseY, partialTick)).getColorARGB());
			fill(poseStack, x, y, x + width, y + height, WidgetUtil.respectWidgetAlpha(this, getCurrentBackgroundColor(poseStack, mouseX, mouseY, partialTick)).getColorARGB());
		}
	}
	
	@Override
	public void renderForeground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		final RGBA currentTextColor = WidgetUtil.respectWidgetAlpha(this, getCurrentTextColor(poseStack, mouseX, mouseY, partialTick));
		
		final String currentText = font.plainSubstrByWidth(value.substring(displayPos), getInnerWidth());
		
		final int cursorOffset = cursorPos - displayPos;
		final int selectionOffset = Math.min(highlightPos - displayPos, currentText.length());
		
		final boolean isCursorInText = cursorOffset >= 0 && cursorOffset <= currentText.length();
		final boolean shouldCursorBlink = isFocused() && frame / 6 % 2 == 0 && isCursorInText;
		final boolean isCursorInTheMiddle = cursorPos < value.length() || value.length() >= maxLength;
		
		final int xOffset = bordered ? x + 4 : x;
		final int yOffset = bordered ? y + (height - 8) / 2 : y;
		
		int leftRenderedTextX = xOffset;
		
		if (!currentText.isEmpty()) {
			final String firstTextPart = isCursorInText ? currentText.substring(0, cursorOffset) : currentText;
			leftRenderedTextX = font.drawShadow(poseStack, formatter.apply(firstTextPart, displayPos), xOffset, yOffset, currentTextColor.getColorARGB());
		}
		
		int rightRenderedTextX = leftRenderedTextX;
		
		if (!isCursorInText) {
			rightRenderedTextX = cursorOffset > 0 ? xOffset + width : xOffset;
		} else if (isCursorInTheMiddle) {
			rightRenderedTextX = leftRenderedTextX - 1;
			--leftRenderedTextX;
		}
		
		if (!currentText.isEmpty() && isCursorInText && cursorOffset < currentText.length()) {
			font.drawShadow(poseStack, formatter.apply(currentText.substring(cursorOffset), cursorPos), leftRenderedTextX, yOffset, currentTextColor.getColorARGB());
		}
		
		if (hint != null && currentText.isEmpty() && !isFocused()) {
			font.drawShadow(poseStack, hint, leftRenderedTextX, yOffset, WidgetUtil.respectWidgetAlpha(this, getCurrentHintTextColor(poseStack, mouseX, mouseY, partialTick)).getColorARGB());
		}
		
		if (!isCursorInTheMiddle && suggestion != null) {
			font.drawShadow(poseStack, suggestion, rightRenderedTextX - 1, yOffset, WidgetUtil.respectWidgetAlpha(this, getCurrentSuggestionTextColor(poseStack, mouseX, mouseY, partialTick)).getColorARGB());
		}
		
		if (shouldCursorBlink) {
			if (isCursorInTheMiddle) {
				GuiComponent.fill(poseStack, rightRenderedTextX, yOffset - 1, rightRenderedTextX + 1, yOffset + 1 + 9, WidgetUtil.respectWidgetAlpha(this, getCurrentCursorColor(poseStack, mouseX, mouseY, partialTick)).getColorARGB());
			} else {
				font.drawShadow(poseStack, "_", rightRenderedTextX, yOffset, currentTextColor.getColorARGB());
			}
		}
		
		if (selectionOffset != cursorOffset) {
			final int selectedX = xOffset + font.width(currentText.substring(0, selectionOffset));
			renderHighlight(poseStack, rightRenderedTextX, yOffset - 1, selectedX - 1, yOffset + 1 + 9);
		}
	}
	
	@Override
	public RGBA getCurrentBackgroundColor(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		return backgroundColor;
	}
	
	public RGBA getCurrentBackgroundFrameColor(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		return isFocused() ? backgroundFrameColor : unfocusedBackgroundFrameColor;
	}
	
	@Override
	public RGBA getCurrentTextColor(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		return isEditable ? textColor : disabledTextColor;
	}
	
	public RGBA getCurrentHintTextColor(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		return hintTextColor;
	}
	
	public RGBA getCurrentSuggestionTextColor(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		return suggestionTextColor;
	}
	
	public RGBA getCurrentCursorColor(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		return cursorColor;
	}
}
