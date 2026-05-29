package com.yourname.deepslateexpansion.Blocks;   // 1. Must match folder location

// 2. Import what we need from Minecraft/Forge
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;

public class BlockDeepslate extends Block {   // 3. Extends Block, the base for all blocks

    public BlockDeepslate() {                 // 4. Constructor - called when we do "new BlockDeepslate()"
        super(Material.ROCK);                // 5. Material.ROCK: stone-like properties (requires pickaxe, etc.)

        // 6. Block settings
        setHardness(3.0F);                   // How long it takes to break (stone is 1.5, obsidian is 50)
        setResistance(6.0F);                 // Resistance to explosions (stone is 10, but deepslate is tougher)
        setSoundType(SoundType.STONE);       // Walking/breaking sounds (we'll change to deepslate sound later)

        // 7. Naming and registration
        setUnlocalizedName("deepslate");     // The internal translation key (used in lang files)
        setRegistryName("deepslate");        // The unique ID for this block (e.g., "deepslateexpansion:deepslate")

        setCreativeTab(CreativeTabs.BUILDING_BLOCKS); // Puts it in the "Building Blocks" tab in creative
    }
}
