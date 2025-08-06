package tech.trowbridge.creativeworld.block;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import tech.trowbridge.creativeworld.CreativeWorld;
import tech.trowbridge.creativeworld.data.CreativeWorldPlayerData;

public final class CreativeWorldPortalBlock extends Block implements EntityBlock {

    private static final Properties DEFAULT_PROPERTIES = Properties.of()
            .strength(5, 6)
            .sound(SoundType.STONE)
            .mapColor(MapColor.COLOR_BLUE);

    private static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties();

    private CreativeWorldPortalBlock(final @NotNull Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull
    InteractionResult useWithoutItem(
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

        final var inCreativeWorldDimension = level.dimensionTypeRegistration().is(CreativeWorld.CREATIVE_WORLD_DIMENSION_TYPE_KEY);
        final var targetDimensionKey = inCreativeWorldDimension ? Level.OVERWORLD : CreativeWorld.CREATIVE_WORLD_DIMENSION_KEY;
        final var targetDimension = server.getLevel(targetDimensionKey);
        if (targetDimension == null) {
            throw new IllegalStateException("Target (" + targetDimensionKey + ") is null!");
        }

        // Try teleporting and only proceed if successful
        boolean teleportSuccess = findTeleportLocation(targetDimension, blockPos).map(teleportLocation -> {
            final var centerTeleportLocation = teleportLocation.getCenter();
            player.teleportTo(targetDimension, centerTeleportLocation.x, centerTeleportLocation.y, centerTeleportLocation.z, Collections.emptySet(), player.getYRot(), player.getXRot());
            return true;
        }).orElseGet(() -> {
            player.sendSystemMessage(Component.translatable("creativeworld.missing_portal"));
            return false;
        });

        if (!teleportSuccess) {
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            if (inCreativeWorldDimension) {
                serverPlayer.setGameMode(GameType.SURVIVAL);
            } else {
                serverPlayer.setGameMode(GameType.CREATIVE);
            }
        }

        // --- Save/Restore Player Data ---
        if (player instanceof ServerPlayer serverPlayer) {
            // Get SavedData for both dimensions
            CreativeWorldPlayerData creativeData = CreativeWorldPlayerData.get((ServerLevel) Objects.requireNonNull(server.getLevel(CreativeWorld.CREATIVE_WORLD_DIMENSION_KEY)));
            CreativeWorldPlayerData overworldData = CreativeWorldPlayerData.get((ServerLevel) Objects.requireNonNull(server.getLevel(Level.OVERWORLD)));

            if (inCreativeWorldDimension) {
                // Save Creative World state
                creativeData.savePlayerData(serverPlayer);

                // Restore Overworld state
                overworldData.restorePlayerData(serverPlayer);
            } else {
                // Save Overworld state
                overworldData.savePlayerData(serverPlayer);

                // Restore Creative World state
                creativeData.restorePlayerData(serverPlayer);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull
    BlockEntity newBlockEntity(final @NotNull BlockPos blockPos, final @NotNull BlockState blockState) {
        return CreativeWorldPortalBlockEntity.createDefaultBlockEntity(blockPos, blockState);
    }

    public static @NotNull
    CreativeWorldPortalBlock createDefaultBlock() {
        return createBlock(DEFAULT_PROPERTIES);
    }

    public static @NotNull
    CreativeWorldPortalBlock createBlock(final @NotNull Properties properties) {
        return new CreativeWorldPortalBlock(properties);
    }

    public static @NotNull
    BlockItem createDefaultBlockItem() {
        return createBlockItem(DEFAULT_ITEM_PROPERTIES);
    }

    public static @NotNull
    BlockItem createBlockItem(final @NotNull Item.Properties properties) {
        return new BlockItem(CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get(), properties);
    }

    public static void teleport(final @NotNull ServerLevel level, final @NotNull Player player, final @NotNull BlockPos blockPos) {
        findTeleportLocation(level, blockPos).ifPresentOrElse(teleportLocation -> {
            final var centerTeleportLocation = teleportLocation.getCenter();
            player.teleportTo(level, centerTeleportLocation.x, centerTeleportLocation.y, centerTeleportLocation.z, Collections.emptySet(), player.getYRot(), player.getXRot());
        }, () -> player.displayClientMessage(Component.translatable("creativeworld.missing_portal"), false));
    }

    private static @NotNull
    Optional<BlockPos> findTeleportLocation(final @NotNull Level level, final @NotNull BlockPos origin) {
        final int radius = 16;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        // List of replaceable natural blocks
        var replaceableBlocks = Set.of(
                // Ground blocks
                Blocks.GRASS_BLOCK,
                Blocks.DIRT,
                Blocks.COARSE_DIRT,
                Blocks.PODZOL,
                Blocks.ROOTED_DIRT,
                Blocks.MYCELIUM,
                Blocks.MOSS_BLOCK,
                Blocks.MOSS_CARPET,
                Blocks.DIRT_PATH,
                Blocks.SAND,
                Blocks.RED_SAND,
                Blocks.GRAVEL,
                Blocks.CLAY,
                Blocks.MUD,
                Blocks.PACKED_MUD,

                // Stone and solid terrain
                Blocks.STONE,
                Blocks.GRANITE,
                Blocks.DIORITE,
                Blocks.ANDESITE,
                Blocks.CALCITE,
                Blocks.TUFF,
                Blocks.DRIPSTONE_BLOCK,
                Blocks.POINTED_DRIPSTONE,
                Blocks.DEEPSLATE,

                // Water/ice/snow
                Blocks.WATER,
                Blocks.LAVA,
                Blocks.SNOW,
                Blocks.SNOW_BLOCK,
                Blocks.ICE,
                Blocks.PACKED_ICE,
                Blocks.BLUE_ICE,
                Blocks.POWDER_SNOW,

                // Plants - grass & ground flora
                Blocks.SHORT_GRASS,
                Blocks.TALL_GRASS,
                Blocks.FERN,
                Blocks.LARGE_FERN,
                Blocks.DEAD_BUSH,
                Blocks.SEAGRASS,
                Blocks.TALL_SEAGRASS,
                Blocks.LILY_PAD,
                Blocks.VINE,
                Blocks.GLOW_LICHEN,
                Blocks.SWEET_BERRY_BUSH,
                Blocks.BAMBOO,
                Blocks.SUGAR_CANE,
                Blocks.CACTUS,
                Blocks.MANGROVE_ROOTS,
                Blocks.MUDDY_MANGROVE_ROOTS,
                Blocks.HANGING_ROOTS,

                // Leaves
                Blocks.OAK_LEAVES,
                Blocks.SPRUCE_LEAVES,
                Blocks.BIRCH_LEAVES,
                Blocks.JUNGLE_LEAVES,
                Blocks.ACACIA_LEAVES,
                Blocks.DARK_OAK_LEAVES,
                Blocks.CHERRY_LEAVES,
                Blocks.MANGROVE_LEAVES,
                Blocks.AZALEA_LEAVES,
                Blocks.FLOWERING_AZALEA_LEAVES,

                // Flowers
                Blocks.DANDELION,
                Blocks.POPPY,
                Blocks.BLUE_ORCHID,
                Blocks.ALLIUM,
                Blocks.AZURE_BLUET,
                Blocks.RED_TULIP,
                Blocks.ORANGE_TULIP,
                Blocks.WHITE_TULIP,
                Blocks.PINK_TULIP,
                Blocks.OXEYE_DAISY,
                Blocks.CORNFLOWER,
                Blocks.LILY_OF_THE_VALLEY,
                Blocks.WITHER_ROSE,
                Blocks.TORCHFLOWER,
                Blocks.SUNFLOWER,
                Blocks.LILAC,
                Blocks.ROSE_BUSH,
                Blocks.PEONY,
                Blocks.PITCHER_PLANT,

                // Mushrooms
                Blocks.BROWN_MUSHROOM,
                Blocks.RED_MUSHROOM,
                Blocks.MUSHROOM_STEM,
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.RED_MUSHROOM_BLOCK,

                // Dripleaf & misc
                Blocks.BIG_DRIPLEAF,
                Blocks.SMALL_DRIPLEAF,
                Blocks.SPORE_BLOSSOM,
                Blocks.AZALEA,
                Blocks.FLOWERING_AZALEA,

                // Underwater flora
                Blocks.KELP,
                Blocks.KELP_PLANT,

                // Crops (surface wild variants)
                Blocks.TORCHFLOWER_CROP,
                Blocks.PITCHER_CROP
        );

        // Search for any portal block within a cube of size 33x33x33 centered on origin
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    mutablePos.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (level.getBlockState(mutablePos).getBlock() == CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get()) {
                        return Optional.of(mutablePos.above());
                    }
                }
            }
        }

        // Search for nearest solid or replaceable block with 2 air blocks above
        BlockPos bestPos = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    int x = origin.getX() + dx;
                    int y = origin.getY() + dy;
                    int z = origin.getZ() + dz;
                    mutablePos.set(x, y, z);
                    var state = level.getBlockState(mutablePos);
                    var above1 = level.getBlockState(mutablePos.above());
                    var above2 = level.getBlockState(mutablePos.above(2));
                    boolean solid = state.isSolid();
                    boolean replaceable = replaceableBlocks.contains(state.getBlock());
                    if ((solid || replaceable) && above1.isAir() && above2.isAir()) {
                        double dist = mutablePos.distSqr(origin);
                        if (dist < bestDist) {
                            bestDist = dist;
                            bestPos = mutablePos.immutable();
                        }
                    }
                }
            }
        }

        if (bestPos != null) {
            // Place portal block if not already there
            var state = level.getBlockState(bestPos);
            if (replaceableBlocks.contains(state.getBlock()) || state.isAir()) {
                level.setBlockAndUpdate(bestPos, CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get().defaultBlockState());
            }
            return Optional.of(bestPos.above());
        }

        // If the origin is air, place the portal block and return above
        if (level.getBlockState(origin).isAir()) {
            level.setBlockAndUpdate(origin, CreativeWorld.CREATIVE_WORLD_PORTAL_BLOCK.get().defaultBlockState());
            return Optional.of(origin.above());
        }

        // No suitable location found
        return Optional.empty();
    }
}
