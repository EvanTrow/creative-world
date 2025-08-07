package tech.trowbridge.creativeworld.data;

import org.jetbrains.annotations.NotNull;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tech.trowbridge.creativeworld.CreativeWorld;

public final class CreativeWorldItemModelProvider extends ItemModelProvider {

    public CreativeWorldItemModelProvider(final @NotNull PackOutput packOutput, final @NotNull String modId, final @NotNull ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Use block model as parent for block item
        withExistingParent(
            CreativeWorld.CREATIVE_WORLD_PORTAL_ITEM.getId().getPath(),
            modLoc("block/" + CreativeWorld.CREATIVE_WORLD_PORTAL_ITEM.getId().getPath())
        );
    }
}