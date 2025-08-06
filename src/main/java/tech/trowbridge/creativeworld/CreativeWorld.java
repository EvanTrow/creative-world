package tech.trowbridge.creativeworld;

import java.util.Collections;
import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import tech.trowbridge.creativeworld.block.CreativeWorldPortalBlock;
import tech.trowbridge.creativeworld.block.CreativeWorldPortalBlockEntity;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreativeWorld.MOD_ID)
public class CreativeWorld {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "creativeworld";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final DeferredHolder<Block, CreativeWorldPortalBlock> CREATIVE_WORLD_PORTAL_BLOCK = BLOCKS.register("creativeworld_portal", CreativeWorldPortalBlock::createDefaultBlock);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativeWorldPortalBlockEntity>> CREATIVE_WORLD_PORTAL_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("creativeworld_portal", CreativeWorldPortalBlockEntity::createDefaultBlockEntityType);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static final DeferredHolder<Item, BlockItem> CREATIVE_WORLD_PORTAL_ITEM = ITEMS.register("creativeworld_portal", CreativeWorldPortalBlock::createDefaultBlockItem);

    public static final ResourceKey<Biome> CREATIVE_WORLD_BIOME_KEY = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MOD_ID, "creativeworld"));
    public static final ResourceKey<DimensionType> CREATIVE_WORLD_DIMENSION_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "creativeworld"));
    public static final ResourceKey<Level> CREATIVE_WORLD_DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MOD_ID, "creativeworld"));

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CreativeWorld(final @NotNull IEventBus eventBus, final @NotNull ModContainer modContainer) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        ITEMS.register(eventBus);

        // Register mod event handlers on the mod event bus
        eventBus.register(this);
    }

    @SubscribeEvent
    private void onBuildCreativeModeTabContents(final @NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS || event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CREATIVE_WORLD_PORTAL_ITEM.get());
        }
    }
}
