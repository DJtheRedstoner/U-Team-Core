package info.u_team.u_team_core.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

public class UBootsItem extends UArmorItem {
	
	public UBootsItem(String textureName, Properties properties, ArmorMaterial material) {
		super(textureName, properties, material, EquipmentSlot.FEET);
	}
	
}
