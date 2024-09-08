package vg.winter.nothing.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class NothingLootTableProvider extends LootTableProvider {
    private static final List<SubProviderEntry> SUB_PROVIDER_ENTRIES = List.of(
            new SubProviderEntry(NothingBlockLootSubProvider::new, LootContextParamSets.BLOCK)
    );

    public NothingLootTableProvider(
            final @NotNull PackOutput packOutput,
            final @NotNull CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(packOutput, Collections.emptySet(), Collections.emptyList(), lookupProvider);
    }

    @Override
    public @NotNull List<SubProviderEntry> getTables() {
        return SUB_PROVIDER_ENTRIES;
    }
}
