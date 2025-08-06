package tech.trowbridge.creativeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.trowbridge.creativeworld.CreativeWorld;

public final class CreativeWorldPortalBlockEntity extends BlockEntity {

    private CreativeWorldPortalBlockEntity(
            final @NotNull BlockEntityType<?> blockEntityType,
            final @NotNull BlockPos blockPos,
            final @NotNull BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    public static @NotNull BlockEntityType<CreativeWorldPortalBlockEntity> createDefaultBlockEntityType() {
        return createBlockEntityType(CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get());
    }

    public static @NotNull BlockEntityType<CreativeWorldPortalBlockEntity> createBlockEntityType(final @NotNull Block... validBlocks) {
        // NOTE(ossianwinter): build Type<?> parameter is incorrectly marked as @NotNull
        // noinspection DataFlowIssue
        return BlockEntityType.Builder.of(CreativeWorldPortalBlockEntity::createDefaultBlockEntity, validBlocks).build(null);
    }

    public static @NotNull CreativeWorldPortalBlockEntity createDefaultBlockEntity(
            final @NotNull BlockPos blockPos,
            final @NotNull BlockState blockState
    ) {
        return createBlockEntity(CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK_ENTITY_TYPE.get(), blockPos, blockState);
    }

    public static @NotNull CreativeWorldPortalBlockEntity createBlockEntity(
            final @NotNull BlockEntityType<?> blockEntityType,
            final @NotNull BlockPos blockPos,
            final @NotNull BlockState blockState
    ) {
        return new CreativeWorldPortalBlockEntity(blockEntityType, blockPos, blockState);
    }
}