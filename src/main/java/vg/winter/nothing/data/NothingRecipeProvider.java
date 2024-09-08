package vg.winter.nothing.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.Nothing;

import java.util.concurrent.CompletableFuture;

public final class NothingRecipeProvider extends RecipeProvider {

    public NothingRecipeProvider(
            final @NotNull PackOutput packOutput,
            final @NotNull CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(final @NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Nothing.NOTHING_PORTAL_ITEM.get())
                .pattern("eee")
                .pattern("eoe")
                .pattern("eee")
                .define('e', Items.ENDER_PEARL)
                .define('o', Items.OBSIDIAN)
                .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Nothing.MOD_ID, "nothing_portal"));
    }
}
