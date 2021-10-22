package info.u_team.u_team_core.util;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.u_team_core.api.gui.IBackgroundColorProvider;
import info.u_team.u_team_core.api.gui.IScaleProvider;
import info.u_team.u_team_core.api.gui.TextProvider;
import info.u_team.u_team_core.api.gui.PerspectiveRenderable;
import info.u_team.u_team_core.api.gui.TextureProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

public class WidgetUtil {
	
	public static boolean isHovered(AbstractWidget widget) {
		return widget.isHovered;
	}
	
	public static <T extends AbstractWidget & PerspectiveRenderable & IBackgroundColorProvider> void renderButtonLikeWidget(T widget, TextureProvider textureProvider, PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderUtil.drawContinuousTexturedBox(poseStack, widget.x, widget.y, textureProvider.getU(), textureProvider.getV(), widget.width, widget.height, textureProvider.getWidth(), textureProvider.getHeight(), 2, 3, 2, 2, widget.getBlitOffset(), textureProvider.getTexture(), widget.getCurrentBackgroundColor(poseStack, mouseY, mouseY, partialTicks));
		
		widget.renderBackground(poseStack, mouseX, mouseY, partialTicks);
		widget.renderForeground(poseStack, mouseX, mouseY, partialTicks);
	}
	
	public static <T extends AbstractWidget & TextProvider> void renderText(T widget, PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		final var minecraft = Minecraft.getInstance(); // TODO replace with font provider
		final var fontRenderer = minecraft.font;
		
		var message = widget.getCurrentText();
		if (message != TextComponent.EMPTY) {
			final var messageWidth = fontRenderer.width(message);
			final var ellipsisWidth = fontRenderer.width("...");
			
			if (messageWidth > widget.width - 6 && messageWidth > ellipsisWidth) {
				message = new TextComponent(fontRenderer.substrByWidth(message, widget.width - 6 - ellipsisWidth).getString() + "...");
			}
			
			final float xStart = (widget.x + (widget.width / 2) - messageWidth / 2);
			final float yStart = (widget.y + (widget.height - 8) / 2);
			
			fontRenderer.drawShadow(poseStack, message, xStart, yStart, widget.getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
		}
	}
	
	public static <T extends AbstractWidget & TextProvider & IScaleProvider> void renderScaledText(T widget, PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		final var scale = widget.getCurrentScale(poseStack, mouseX, mouseY, partialTicks);
		
		if (scale == 1) {
			renderText(widget, poseStack, mouseX, mouseY, partialTicks);
		} else {
			final var minecraft = Minecraft.getInstance(); // TODO replace with font provider
			final var fontRenderer = minecraft.font;
			
			var message = widget.getCurrentText();
			if (message != TextComponent.EMPTY) {
				final var messageWidth = Mth.ceil(scale * fontRenderer.width(message));
				final var ellipsisWidth = Mth.ceil(scale * fontRenderer.width("..."));
				
				if (messageWidth > widget.width - 6 && messageWidth > ellipsisWidth) {
					message = new TextComponent(fontRenderer.substrByWidth(message, widget.width - 6 - ellipsisWidth).getString() + "...");
				}
				
				final var positionFactor = 1 / scale;
				
				final var xStart = (widget.x + (widget.width / 2) - messageWidth / 2) * positionFactor;
				final var yStart = (widget.y + ((int) (widget.height - 8 * scale)) / 2) * positionFactor;
				
				poseStack.pushPose();
				poseStack.scale(scale, scale, 0);
				fontRenderer.drawShadow(poseStack, message, xStart, yStart, widget.getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
				poseStack.popPose();
			}
		}
	}
	
	public static void renderTooltips(List<Widget> widgets, PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		widgets.forEach(widget -> {
			if (widget instanceof PerspectiveRenderable perspectiveRenderable) {
				perspectiveRenderable.renderToolTip(poseStack, mouseX, mouseY, partialTicks);
			} else if (widget instanceof AbstractWidget abstractWidget) {
				abstractWidget.renderToolTip(poseStack, mouseX, mouseY); // TODO probably renders stuff twice when implementation like in Button is used. Just do not call this?
			}
		});
	}
}
