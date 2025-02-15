package info.u_team.u_team_core.util.registry;

import java.util.Iterator;
import java.util.function.Supplier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeDeferredRegister implements Iterable<RegistryObject<EntityType<?>>> {
	
	public static EntityTypeDeferredRegister create(String modid) {
		return new EntityTypeDeferredRegister(modid);
	}
	
	private final CommonDeferredRegister<EntityType<?>> register;
	
	protected EntityTypeDeferredRegister(String modid) {
		register = CommonDeferredRegister.create(ForgeRegistries.ENTITY_TYPES, modid);
	}
	
	public <E extends Entity> RegistryObject<EntityType<E>> register(String name, Supplier<Builder<E>> supplier) {
		return register.register(name, () -> supplier.get().build("egg")); // Pass a vanilla minecraft entity here, so we don't get the complain about a missing data fixer
	}
	
	public void register(IEventBus bus) {
		register.register(bus);
	}
	
	@Override
	public Iterator<RegistryObject<EntityType<?>>> iterator() {
		return register.iterator();
	}
	
	public Iterable<EntityType<?>> entityTypeIterable() {
		return register.entryIterable();
	}
	
	public CommonDeferredRegister<EntityType<?>> getRegister() {
		return register;
	}
	
}
