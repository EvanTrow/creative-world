package vg.winter.nothing.data;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import vg.winter.nothing.Nothing;

import java.util.Set;

@EventBusSubscriber(modid = Nothing.MOD_ID, bus = Bus.MOD)
public final class NothingDataProvider {

    @SubscribeEvent
    private static void onGatherData(final GatherDataEvent event) {
        final var generator = event.getGenerator();
        final var packOutput = generator.getPackOutput();
        final var lookupProvider = event.getLookupProvider();
        final var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new NothingBlockStateProvider(packOutput, Nothing.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new NothingItemModelProvider(packOutput, Nothing.MOD_ID, existingFileHelper));

        generator.addProvider(event.includeServer(), new NothingDatapackBuiltinEntriesProvider(packOutput, lookupProvider, Set.of(Nothing.MOD_ID)));
        generator.addProvider(event.includeServer(), new NothingLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new NothingRecipeProvider(packOutput, lookupProvider));
    }
}
