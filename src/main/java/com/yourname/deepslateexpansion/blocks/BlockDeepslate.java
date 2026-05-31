package com.yourname.deepslateexpansion.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;

public class BlockDeepslate extends Block {

    public BlockDeepslate() {
        super(Material.ROCK);
        setHardness(3.0F);
        setResistance(6.0F);
        setSoundType(SoundType.STONE);
        setTranslationKey("deepslate");          // correct method name
        setRegistryName("deepslate");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
