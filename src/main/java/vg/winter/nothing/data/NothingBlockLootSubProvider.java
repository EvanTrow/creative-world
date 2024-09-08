package vg.winter.nothing.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.Nothing;

import java.util.Collections;
import java.util.List;

public final class NothingBlockLootSubProvider extends BlockLootSubProvider {
    private static final List<Block> KNOWN_BLOCKS = List.of(Nothing.NOTHING_PORTAL_BLOCK.get());

    public NothingBlockLootSubProvider(final @NotNull HolderLookup.Provider lookupProvider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(Nothing.NOTHING_PORTAL_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return KNOWN_BLOCKS;
    }
}
