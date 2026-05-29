package com.yourname.deepslateexpansion.Blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "deepslateexpansion")
public class ModBlocks {

    public static Block deepslate;
    public static Block deepslateIronOre;
    public static Block deepslateGoldOre;
    public static Block deepslateCopperOre;
    public static Block deepslateCoalOre;
    public static Block deepslateDiamondOre;
    public static Block deepslateEmeraldOre;
    public static Block deepslateRedstoneOre;
    public static Block deepslateLapisOre;

    public static void init() {
        deepslate = new BlockDeepslate();

        deepslateIronOre   = new BlockDeepslateOre("deepslate_iron_ore",    Item.getItemFromBlock(Blocks.IRON_ORE), 1);
        deepslateGoldOre   = new BlockDeepslateOre("deepslate_gold_ore",    Item.getItemFromBlock(Blocks.GOLD_ORE), 1);
        deepslateCopperOre = new BlockDeepslateOre("deepslate_copper_ore",  null, 1);
        deepslateCoalOre   = new BlockDeepslateOre("deepslate_coal_ore",    Items.COAL, 1);
        deepslateDiamondOre = new BlockDeepslateOre("deepslate_diamond_ore", Items.DIAMOND, 1);
        deepslateEmeraldOre = new BlockDeepslateOre("deepslate_emerald_ore", Items.EMERALD, 1);

        // Redstone: drops 4-5 redstone
        deepslateRedstoneOre = new BlockDeepslateOre("deepslate_redstone_ore", Items.REDSTONE, 4) {
            @Override
            public int quantityDropped(Random random) {
                return 4 + random.nextInt(2);
            }

            @Override
            public int quantityDroppedWithBonus(int fortune, Random random) {
                if (fortune > 0) {
                    return quantityDropped(random) * (random.nextInt(fortune + 2) + 1);
                }
                return quantityDropped(random);
            }
        };

        // Lapis: drops 4-8 lapis lazuli
        deepslateLapisOre = new BlockDeepslateOre("deepslate_lapis_ore",
                new ItemStack(Items.DYE, 1, 4).getItem(), 4) {
            @Override
            public int quantityDropped(Random random) {
                return 4 + random.nextInt(5);
            }

            @Override
            public int quantityDroppedWithBonus(int fortune, Random random) {
                if (fortune > 0) {
                    return quantityDropped(random) * (random.nextInt(fortune + 2) + 1);
                }
                return quantityDropped(random);
            }
        };
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
            deepslate,
            deepslateIronOre,
            deepslateGoldOre,
            deepslateCopperOre,
            deepslateCoalOre,
            deepslateDiamondOre,
            deepslateEmeraldOre,
            deepslateRedstoneOre,
            deepslateLapisOre
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            new ItemBlock(deepslate).setRegistryName(deepslate.getRegistryName()),
            new ItemBlock(deepslateIronOre).setRegistryName(deepslateIronOre.getRegistryName()),
            new ItemBlock(deepslateGoldOre).setRegistryName(deepslateGoldOre.getRegistryName()),
            new ItemBlock(deepslateCopperOre).setRegistryName(deepslateCopperOre.getRegistryName()),
            new ItemBlock(deepslateCoalOre).setRegistryName(deepslateCoalOre.getRegistryName()),
            new ItemBlock(deepslateDiamondOre).setRegistryName(deepslateDiamondOre.getRegistryName()),
            new ItemBlock(deepslateEmeraldOre).setRegistryName(deepslateEmeraldOre.getRegistryName()),
            new ItemBlock(deepslateRedstoneOre).setRegistryName(deepslateRedstoneOre.getRegistryName()),
            new ItemBlock(deepslateLapisOre).setRegistryName(deepslateLapisOre.getRegistryName())
        );
    }
}
