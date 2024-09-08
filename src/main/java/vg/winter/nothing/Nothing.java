package vg.winter.nothing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import vg.winter.nothing.block.NothingPortalBlock;
import vg.winter.nothing.block.NothingPortalBlockEntity;

import java.util.Collections;
import java.util.Optional;

@Mod(value = Nothing.MOD_ID)
public final class Nothing {
    public static final String MOD_ID = "nothing";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final DeferredHolder<Block, NothingPortalBlock> NOTHING_PORTAL_BLOCK = BLOCKS.register("nothing_portal", NothingPortalBlock::createDefaultBlock);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NothingPortalBlockEntity>> NOTHING_PORTAL_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("nothing_portal", NothingPortalBlockEntity::createDefaultBlockEntityType);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static final DeferredHolder<Item, BlockItem> NOTHING_PORTAL_ITEM = ITEMS.register("nothing_portal", NothingPortalBlock::createDefaultBlockItem);

    public static final ResourceKey<Biome> NOTHING_BIOME_KEY = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MOD_ID, "nothing"));
    public static final ResourceKey<DimensionType> NOTHING_DIMENSION_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "nothing"));
    public static final ResourceKey<Level> NOTHING_DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MOD_ID, "nothing"));

    public Nothing(final @NotNull IEventBus eventBus, final @NotNull ModContainer modContainer) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        ITEMS.register(eventBus);

        eventBus.register(this);
    }

    @SubscribeEvent
    private void onBuildCreativeModeTabContents(final @NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS || event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(NOTHING_PORTAL_ITEM.get());
        }
    }

    public static void teleport(final @NotNull ServerLevel level, final @NotNull Player player, final @NotNull BlockPos blockPos) {
        findTeleportLocation(level, blockPos).ifPresentOrElse(teleportLocation -> {
            final var centerTeleportLocation = teleportLocation.getCenter();
            player.teleportTo(level, centerTeleportLocation.x, centerTeleportLocation.y, centerTeleportLocation.z, Collections.emptySet(), player.getYRot(), player.getXRot());
        }, () -> player.sendSystemMessage(Component.translatable("nothing.missing_portal")));
    }

    private static @NotNull Optional<BlockPos> findTeleportLocation(final @NotNull Level level, final @NotNull BlockPos origin) {
        if (level.getBlockState(origin).getBlock() == NOTHING_PORTAL_BLOCK.get()) {
            return Optional.of(origin.above());
        }

        if (level.getBlockState(origin).isAir()) {
            level.setBlockAndUpdate(origin, NOTHING_PORTAL_BLOCK.get().defaultBlockState());
            return Optional.of(origin.above());
        }

        return Optional.empty();
    }
}
