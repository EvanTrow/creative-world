package vg.winter.nothing.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.Nothing;

public final class NothingBlockStateProvider extends BlockStateProvider {

    public NothingBlockStateProvider(final @NotNull PackOutput packOutput, final @NotNull String modId, final @NotNull ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Nothing.NOTHING_PORTAL_BLOCK.get());
    }
}
