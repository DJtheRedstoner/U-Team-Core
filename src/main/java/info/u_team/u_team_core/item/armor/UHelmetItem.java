package info.u_team.u_team_core.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class UHelmetItem extends UArmorItem {
	
	public UHelmetItem(String textureName, Properties properties, ArmorMaterial material) {
		super(textureName, properties, material, ArmorItem.Type.HELMET);
	}
	
}
