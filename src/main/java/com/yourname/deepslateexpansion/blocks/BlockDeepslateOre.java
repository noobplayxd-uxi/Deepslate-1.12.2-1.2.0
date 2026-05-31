package com.yourname.deepslateexpansion.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDeepslateOre extends Block {

    private final Item dropItem;
    private final int dropAmount;

    public BlockDeepslateOre(String name, Item drop, int amount) {
        super(Material.ROCK);
        setHardness(4.5F);
        setResistance(3.0F);
        setSoundType(SoundType.STONE);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.dropItem = drop;
        this.dropAmount = amount;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return dropItem != null ? dropItem : Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDropped(Random random) {
        return dropAmount;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && dropItem != null) {
            int multiplier = random.nextInt(fortune + 2) - 1;
            if (multiplier < 0) multiplier = 0;
            return quantityDropped(random) * (multiplier + 1);
        }
        return quantityDropped(random);
    }
}
