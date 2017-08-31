package info.u_team.u_team_core.intern.proxy;

import info.u_team.u_team_core.intern.generation.WorldGenerator;
import net.minecraftforge.fml.common.event.*;

/**
 * This class has methods that are only run by the server.
 * 
 * @author HyCraftHD
 * @date 16.08.2017
 *
 */
public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
	}
	
	public void init(FMLInitializationEvent event) {
		new WorldGenerator();
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
