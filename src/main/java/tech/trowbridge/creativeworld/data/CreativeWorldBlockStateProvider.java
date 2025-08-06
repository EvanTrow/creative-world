package tech.trowbridge.creativeworld.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import tech.trowbridge.creativeworld.CreativeWorld;

public final class CreativeWorldBlockStateProvider extends BlockStateProvider {

    public CreativeWorldBlockStateProvider(final @NotNull PackOutput packOutput, final @NotNull String modId, final @NotNull ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get());
    }
}