package info.u_team.u_team_core.sub.metadata;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.ImmutableMap;

import info.u_team.u_team_core.intern.UCoreConstants;
import net.minecraftforge.fml.common.*;

public class MetadataFetcher {
	
	private ModMetadata modmeta;
	
	public MetadataFetcher(String modid) {
		Map<String, Object> map = ImmutableMap.<String, Object> builder().put("name", modid).put("version", "1.0").build();
		modmeta = MetadataCollection.from(MetadataFetcher.class.getResourceAsStream("/" + modid + ".json"), modid).getMetadataForId(modid, map);
		if (modmeta.name == null || modmeta.name.isEmpty()) {
			modmeta.name = map.get("name").toString();
		}
		if (modmeta.version == null || modmeta.version.isEmpty()) {
			modmeta.version = map.get("version").toString();
		}
		
	}
	
	public MetadataFetcher setName(String name) {
		if (name != null) {
			modmeta.name = name;
		}
		return this;
	}
	
	public MetadataFetcher setVersion(String version) {
		if (version != null) {
			modmeta.version = version;
		}
		return this;
	}
	
	public ModMetadata getModmeta() {
		return modmeta;
	}
	
	public void applyMetadata(ModMetadata modmetatoapply) {
		try {
			Class<ModMetadata> modmetadata = ModMetadata.class;
			for (Field field : modmetadata.getDeclaredFields()) {
				field.set(modmetatoapply, field.get(modmeta));
			}
		} catch (Exception ex) {
			UCoreConstants.LOGGER.error("Couldn't apply metadata.", ex);
		}
	}
	
}
