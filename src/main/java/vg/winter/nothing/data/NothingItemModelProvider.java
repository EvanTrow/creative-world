package vg.winter.nothing.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.Nothing;

public final class NothingItemModelProvider extends ItemModelProvider {

    public NothingItemModelProvider(final @NotNull PackOutput packOutput, final @NotNull String modId, final @NotNull ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(Nothing.NOTHING_PORTAL_ITEM.get());
    }
}
