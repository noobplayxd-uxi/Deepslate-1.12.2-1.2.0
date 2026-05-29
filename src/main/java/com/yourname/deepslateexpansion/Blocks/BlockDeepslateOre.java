package com.yourname.deepslateexpansion.blocks;

// Import all needed Minecraft classes
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

    // These fields store what the block drops
    private final Item dropItem;
    private final int dropAmount;

    /**
     * @param name   The registry name (e.g. "deepslate_iron_ore")
     * @param drop   The item to drop when broken (null = drops the block itself)
     * @param amount How many of the item to drop (without fortune)
     */
    public BlockDeepslateOre(String name, Item drop, int amount) {
        super(Material.ROCK);

        // Block settings (a bit harder than stone)
        setHardness(4.5F);
        setResistance(3.0F);
        setSoundType(SoundType.STONE);  // will be replaced with deepslate sound later

        // Naming and registration
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        this.dropItem = drop;
        this.dropAmount = amount;
    }

    // --- DROP LOGIC ---

    // 1. Which item drops? If no custom item, the block drops itself (useful for copper ore).
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return dropItem != null ? dropItem : Item.getItemFromBlock(this);
    }

    // 2. How many of that item drop? (base amount, before fortune)
    @Override
    public int quantityDropped(Random random) {
        return dropAmount;
    }

    // 3. Fortune effect: multiplies the drop amount, similar to vanilla ores
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && dropItem != null) {
            int multiplier = random.nextInt(fortune + 2) - 1;
            if (multiplier < 0) multiplier = 0;
            return quantityDropped(random) * (multiplier + 1);
        }
        return quantityDropped(random);
    }

    // 4. (Optional) If we need to drop something different when using Silk Touch, we can override
    // getSilkTouchDrop(), but the default already drops the block itself.
}