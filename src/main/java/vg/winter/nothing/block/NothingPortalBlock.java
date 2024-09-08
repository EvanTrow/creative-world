package vg.winter.nothing.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.Nothing;

public final class NothingPortalBlock extends Block implements EntityBlock {
    private static final Properties DEFAULT_PROPERTIES = Properties.of()
            .strength(5, 6)
            .sound(SoundType.STONE)
            .mapColor(MapColor.COLOR_BLUE);

    private static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties();

    private NothingPortalBlock(final @NotNull Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            final @NotNull BlockState blockState,
            final @NotNull Level level,
            final @NotNull BlockPos blockPos,
            final @NotNull Player player,
            final @NotNull BlockHitResult blockHitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        }

        final var server = level.getServer();
        if (server == null) {
            throw new IllegalStateException("Level (" + level.dimension() + ") server is null!");
        }

        final var inNothingDimension = level.dimensionTypeRegistration().is(Nothing.NOTHING_DIMENSION_TYPE_KEY);
        final var targetDimensionKey = inNothingDimension ? Level.OVERWORLD : Nothing.NOTHING_DIMENSION_KEY;
        final var targetDimension = server.getLevel(targetDimensionKey);
        if (targetDimension == null) {
            throw new IllegalStateException("Target (" + targetDimensionKey + ") is null!");
        }

        Nothing.teleport(targetDimension, player, blockPos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(final @NotNull BlockPos blockPos, final @NotNull BlockState blockState) {
        return NothingPortalBlockEntity.createDefaultBlockEntity(blockPos, blockState);
    }

    public static @NotNull NothingPortalBlock createDefaultBlock() {
        return createBlock(DEFAULT_PROPERTIES);
    }

    public static @NotNull NothingPortalBlock createBlock(final @NotNull Properties properties) {
        return new NothingPortalBlock(properties);
    }

    public static @NotNull BlockItem createDefaultBlockItem() {
        return createBlockItem(DEFAULT_ITEM_PROPERTIES);
    }

    public static @NotNull BlockItem createBlockItem(final @NotNull Item.Properties properties) {
        return new BlockItem(Nothing.NOTHING_PORTAL_BLOCK.get(), properties);
    }
}
