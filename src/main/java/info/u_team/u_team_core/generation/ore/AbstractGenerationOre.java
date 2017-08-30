package info.u_team.u_team_core.generation.ore;

import com.google.common.base.Predicate;

import info.u_team.u_team_core.generation.IGeneration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;

public abstract class AbstractGenerationOre implements IGeneration {
	
	protected int veinsize, count;
	protected Predicate<IBlockState> predicate;
	
	public AbstractGenerationOre(int veinsize, int count, Predicate<IBlockState> predicate) {
		this.veinsize = veinsize;
		this.count = count;
		this.predicate = predicate;
	}
	
	public AbstractGenerationOre(int veinsize, int count, Block block) {
		this(veinsize, count, BlockMatcher.forBlock(block));
	}
	
	public AbstractGenerationOre(int veinsize, int count) {
		this(veinsize, count, Blocks.STONE);
	}
	
	public void setPredicate(Predicate<IBlockState> predicate) { // maybe useful for changes in generation without creating a new instance
		this.predicate = predicate;
	}
	
}
