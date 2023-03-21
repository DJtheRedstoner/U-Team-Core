package info.u_team.u_team_core.gui.elements;

import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;

public class ScrollingText extends ScalableText {
	
	protected int width;
	protected float stepSize;
	protected int speedTime;
	protected int waitTime;
	
	protected float moveDifference = 0;
	protected long lastTime = 0;
	protected State state = State.WAITING;
	
	public ScrollingText(Font font, Supplier<String> textSupplier, float x, float y) {
		super(font, textSupplier, x, y);
		width = 100;
		stepSize = 1;
		speedTime = 20;
		waitTime = 4000;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public float getStepSize() {
		return stepSize;
	}
	
	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}
	
	public int getSpeedTime() {
		return speedTime;
	}
	
	public void setSpeedTime(int speedtime) {
		speedTime = speedtime;
	}
	
	public int getWaitTime() {
		return waitTime;
	}
	
	public void setWaitTime(int waittime) {
		waitTime = waittime;
	}
	
	public void copyState(ScrollingText renderer) {
		setText(textSupplier.get());
		state = renderer.state;
		moveDifference = renderer.moveDifference;
		lastTime = renderer.lastTime;
	}
	
	@Override
	protected void updatedText() {
		state = State.WAITING;
		moveDifference = 0;
		lastTime = 0;
		super.updatedText();
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		Gui.enableScissor(Mth.ceil(x), Mth.ceil(y), Mth.ceil(x + width), Mth.ceil(y + ((font.lineHeight + 1) * scale)));
		
		// Uncomment to test scissor
		// poseStack.pushPose();
		// poseStack.last().pose().identity();
		// GuiComponent.fill(poseStack, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), 0x8F00FF00);
		// poseStack.popPose();
		
		setText(textSupplier.get());
		renderFont(poseStack, font, getMovingX(x), y + 2 * scale);
		
		Gui.disableScissor();
	}
	
	protected float getMovingX(float x) {
		final float textWidth = getTextWidth();
		if (width < textWidth) {
			final float maxMove = width - textWidth;
			
			if (lastTime == 0) {
				lastTime = System.currentTimeMillis();
			}
			
			if (state == State.WAITING) {
				if (hasWaitTimePassed()) {
					state = moveDifference >= 0 ? State.LEFT : State.RIGHT;
					lastTime = 0;
				}
			} else {
				if (hasSpeedTimePassed()) {
					if (state == State.LEFT ? moveDifference >= maxMove : moveDifference <= 0) {
						moveDifference += state == State.LEFT ? -stepSize : +stepSize;
					} else {
						state = State.WAITING;
					}
					lastTime = 0;
				}
			}
			return x + moveDifference;
		}
		return x;
	}
	
	protected boolean hasWaitTimePassed() {
		return System.currentTimeMillis() - waitTime >= lastTime;
	}
	
	protected boolean hasSpeedTimePassed() {
		return System.currentTimeMillis() - speedTime >= lastTime;
	}
	
	private enum State {
		WAITING,
		LEFT,
		RIGHT;
	}
	
}
