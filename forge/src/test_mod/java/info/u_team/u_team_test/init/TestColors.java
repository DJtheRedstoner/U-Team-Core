package info.u_team.u_team_test.init;

import java.awt.Color;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class TestColors {
	
	private static void colorItem(RegisterColorHandlersEvent.Item event) {
		// This use the awt color class. Might not work but was too lazy to write an own color class.
		event.register((stack, index) -> Color.getHSBColor((float) stack.getDamageValue() / (float) stack.getMaxDamage(), 0.8F, 0.5F).getRGB(), TestItems.BASIC.get());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(TestColors::colorItem);
	}
}
