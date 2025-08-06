package tech.trowbridge.creativeworld.data;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tech.trowbridge.creativeworld.CreativeWorld;

import java.util.Set;

@EventBusSubscriber(modid = CreativeWorld.MOD_ID, bus = Bus.MOD)
public final class CreativeWorldDataProvider {

    @SubscribeEvent
    private static void onGatherData(final GatherDataEvent event) {
        final var generator = event.getGenerator();
        final var packOutput = generator.getPackOutput();
        final var lookupProvider = event.getLookupProvider();
        final var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new CreativeWorldBlockStateProvider(packOutput, CreativeWorld.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new CreativeWorldItemModelProvider(packOutput, CreativeWorld.MOD_ID, existingFileHelper));

        generator.addProvider(event.includeServer(), new CreativeWorldDatapackBuiltinEntriesProvider(packOutput, lookupProvider, Set.of(CreativeWorld.MOD_ID)));
        generator.addProvider(event.includeServer(), new CreativeWorldLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new CreativeWorldRecipeProvider(packOutput, lookupProvider));
    }
}