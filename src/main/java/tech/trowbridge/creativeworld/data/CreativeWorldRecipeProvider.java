package tech.trowbridge.creativeworld.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import tech.trowbridge.creativeworld.CreativeWorld;

import java.util.concurrent.CompletableFuture;

public final class CreativeWorldRecipeProvider extends RecipeProvider {

    public CreativeWorldRecipeProvider(
            final @NotNull PackOutput packOutput,
            final @NotNull CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(final @NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, CreativeWorld.CREATIVE_WORLD_PORTAL_ITEM.get())
                .pattern("bbb")
                .pattern("bpb")
                .pattern("bbb")
                .define('b', Items.BRICKS)
                .define('p', Items.ENDER_PEARL)
                .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                .unlockedBy("has_bricks", has(Items.BRICKS))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreativeWorld.MOD_ID, "creativeworld_portal"));
    }
}