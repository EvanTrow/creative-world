package tech.trowbridge.creativeworld.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import tech.trowbridge.creativeworld.CreativeWorld;

import java.util.Collections;
import java.util.List;

public final class CreativeWorldBlockLootSubProvider extends BlockLootSubProvider {
    private static final List<Block> KNOWN_BLOCKS = List.of(CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get());

    public CreativeWorldBlockLootSubProvider(final @NotNull HolderLookup.Provider lookupProvider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return KNOWN_BLOCKS;
    }
}