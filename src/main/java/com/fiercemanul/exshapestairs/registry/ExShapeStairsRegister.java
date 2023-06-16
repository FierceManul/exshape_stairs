package com.fiercemanul.exshapestairs.registry;

import com.fiercemanul.exshapestairs.block.TallStairBlock;
import com.fiercemanul.exshapestairs.block.ThinStairBlock;
import com.fiercemanul.exshapestairs.block.TinyStairBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExShapeStairsRegister {
    private static final LinkedHashMap<String, Block> BLOCK_ITEM_MAP = new LinkedHashMap<>();
    private static final LinkedHashMap<String,Block> BLOCK_MAP = new LinkedHashMap<>();

    @SubscribeEvent
    public static void register(RegisterEvent event) {

        BLOCK_MAP.put("stone", Blocks.STONE_STAIRS);
        BLOCK_MAP.put("granite", Blocks.GRANITE_STAIRS);
        BLOCK_MAP.put("polished_granite", Blocks.POLISHED_GRANITE_STAIRS);
        BLOCK_MAP.put("diorite", Blocks.DIORITE_STAIRS);
        BLOCK_MAP.put("polished_diorite", Blocks.POLISHED_DIORITE_STAIRS);
        BLOCK_MAP.put("andesite", Blocks.ANDESITE_STAIRS);
        BLOCK_MAP.put("polished_andesite", Blocks.POLISHED_ANDESITE_STAIRS);
        BLOCK_MAP.put("cobblestone", Blocks.COBBLESTONE_STAIRS);
        BLOCK_MAP.put("mossy_cobblestone", Blocks.MOSSY_COBBLESTONE_STAIRS);
        BLOCK_MAP.put("stone_brick", Blocks.STONE_BRICK_STAIRS);
        BLOCK_MAP.put("mossy_stone_brick", Blocks.MOSSY_STONE_BRICK_STAIRS);
        BLOCK_MAP.put("brick", Blocks.BRICK_STAIRS);
        BLOCK_MAP.put("mud_brick", Blocks.MUD_BRICK_STAIRS);
        BLOCK_MAP.put("sandstone", Blocks.SANDSTONE_STAIRS);
        BLOCK_MAP.put("smooth_sandstone", Blocks.SMOOTH_SANDSTONE_STAIRS);
        BLOCK_MAP.put("red_sandstone", Blocks.RED_SANDSTONE_STAIRS);
        BLOCK_MAP.put("smooth_red_sandstone", Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
        BLOCK_MAP.put("cobble_deepslate", Blocks.COBBLED_DEEPSLATE_STAIRS);
        BLOCK_MAP.put("polished_deepslate", Blocks.POLISHED_DEEPSLATE_STAIRS);
        BLOCK_MAP.put("deepslate_brick", Blocks.DEEPSLATE_BRICK_STAIRS);
        BLOCK_MAP.put("deepslate_tile", Blocks.DEEPSLATE_TILE_STAIRS);
        BLOCK_MAP.put("nether_brick", Blocks.NETHER_BRICK_STAIRS);
        BLOCK_MAP.put("red_nether_brick", Blocks.RED_NETHER_BRICK_STAIRS);
        BLOCK_MAP.put("blackstone", Blocks.BLACKSTONE_STAIRS);
        BLOCK_MAP.put("polished_blackstone", Blocks.POLISHED_BLACKSTONE_STAIRS);
        BLOCK_MAP.put("polished_blackstone_brick", Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        BLOCK_MAP.put("end_stone_brick", Blocks.END_STONE_BRICK_STAIRS);
        BLOCK_MAP.put("quartz", Blocks.QUARTZ_STAIRS);
        BLOCK_MAP.put("smooth_quartz", Blocks.SMOOTH_QUARTZ_STAIRS);
        BLOCK_MAP.put("prismarine", Blocks.PRISMARINE_STAIRS);
        BLOCK_MAP.put("prismarine_brick", Blocks.PRISMARINE_BRICK_STAIRS);
        BLOCK_MAP.put("dark_prismarine", Blocks.DARK_PRISMARINE_STAIRS);
        BLOCK_MAP.put("purpur", Blocks.PURPUR_STAIRS);
        BLOCK_MAP.put("waxed_cut_copper", Blocks.WAXED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_exposed_cut_copper", Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_weathered_cut_copper", Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS);
        BLOCK_MAP.put("waxed_oxidized_cut_copper", Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

        BLOCK_MAP.put("oak", Blocks.OAK_STAIRS);
        BLOCK_MAP.put("spruce", Blocks.SPRUCE_STAIRS);
        BLOCK_MAP.put("birch", Blocks.BIRCH_STAIRS);
        BLOCK_MAP.put("jungle", Blocks.JUNGLE_STAIRS);
        BLOCK_MAP.put("acacia", Blocks.ACACIA_STAIRS);
        BLOCK_MAP.put("dark_oak", Blocks.DARK_OAK_STAIRS);
        BLOCK_MAP.put("mangrove", Blocks.MANGROVE_STAIRS);
        BLOCK_MAP.put("crimson", Blocks.CRIMSON_STAIRS);
        BLOCK_MAP.put("warped", Blocks.WARPED_STAIRS);

        BLOCK_MAP.put("smooth_stone", Blocks.SMOOTH_STONE);

        event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
            BLOCK_MAP.forEach((name, block) -> {
                String tallStairID = name + "_tall_stair";
                String thinStairID = name + "_thin_stair";
                String tinyStairID = name + "_tiny_stair";
                helper.register(tallStairID, addTallStair(tallStairID, block));
                helper.register(thinStairID, addThinStair(thinStairID, block));
                helper.register(tinyStairID, addTinyStair(tinyStairID, block));
            });
        });
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
            BLOCK_ITEM_MAP.forEach((name, block) -> {
                helper.register(name, new BlockItem(block, new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
            });
        });
    }

    private static Block addTallStair(String id, Block block) {
        TallStairBlock tallStairBlock = new TallStairBlock(BlockBehaviour.Properties.copy(block));
        BLOCK_ITEM_MAP.put(id, tallStairBlock);
        return tallStairBlock;
    }

    private static Block addThinStair(String id, Block block) {
        ThinStairBlock thinStairBlock = new ThinStairBlock(BlockBehaviour.Properties.copy(block));
        BLOCK_ITEM_MAP.put(id, thinStairBlock);
        return thinStairBlock;
    }

    private static Block addTinyStair(String id, Block block) {
        TinyStairBlock tinyStairBlock = new TinyStairBlock(BlockBehaviour.Properties.copy(block));
        BLOCK_ITEM_MAP.put(id, tinyStairBlock);
        return tinyStairBlock;
    }

}
