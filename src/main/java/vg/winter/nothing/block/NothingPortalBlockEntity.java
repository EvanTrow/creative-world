package vg.winter.nothing.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.Nothing;

public final class NothingPortalBlockEntity extends BlockEntity {

    private NothingPortalBlockEntity(
            final @NotNull BlockEntityType<?> blockEntityType,
            final @NotNull BlockPos blockPos,
            final @NotNull BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    public static @NotNull BlockEntityType<NothingPortalBlockEntity> createDefaultBlockEntityType() {
        return createBlockEntityType(Nothing.NOTHING_PORTAL_BLOCK.get());
    }

    public static @NotNull BlockEntityType<NothingPortalBlockEntity> createBlockEntityType(final @NotNull Block... validBlocks) {
        // NOTE(ossianwinter): build Type<?> parameter is incorrectly marked as @NotNull
        // noinspection DataFlowIssue
        return BlockEntityType.Builder.of(NothingPortalBlockEntity::createDefaultBlockEntity, validBlocks).build(null);
    }

    public static @NotNull NothingPortalBlockEntity createDefaultBlockEntity(
            final @NotNull BlockPos blockPos,
            final @NotNull BlockState blockState
    ) {
        return createBlockEntity(Nothing.NOTHING_PORTAL_BLOCK_ENTITY_TYPE.get(), blockPos, blockState);
    }

    public static @NotNull NothingPortalBlockEntity createBlockEntity(
            final @NotNull BlockEntityType<?> blockEntityType,
            final @NotNull BlockPos blockPos,
            final @NotNull BlockState blockState
    ) {
        return new NothingPortalBlockEntity(blockEntityType, blockPos, blockState);
    }
}
