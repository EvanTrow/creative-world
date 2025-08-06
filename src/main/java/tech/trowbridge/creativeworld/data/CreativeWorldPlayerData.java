package tech.trowbridge.creativeworld.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreativeWorldPlayerData extends SavedData {
    private final Map<UUID, CompoundTag> playerData = new HashMap<>();

    public static CreativeWorldPlayerData get(ServerLevel level) {
        return level
                .getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(CreativeWorldPlayerData::new, CreativeWorldPlayerData::load), "creative_world_player_data");
    }

    public void savePlayerData(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        // Inventory
        tag.put("Inventory", player.getInventory().save(new ListTag()));
        // Health
        tag.putFloat("Health", player.getHealth());
        // Food
        tag.putInt("FoodLevel", player.getFoodData().getFoodLevel());
        tag.putFloat("SaturationLevel", player.getFoodData().getSaturationLevel());
        // XP
        tag.putInt("XpLevel", player.experienceLevel);
        tag.putFloat("XpProgress", player.experienceProgress);
        tag.putInt("XpTotal", player.totalExperience);
        // Creative Inventory (if available)
        if (player.isCreative()) {
            CompoundTag creativeInv = new CompoundTag();
            // If using Forge, use player.getInventory().getContainer() or similar
            // Otherwise, skip or implement as needed
            tag.put("CreativeInventory", creativeInv);
        }
        playerData.put(player.getUUID(), tag);
        setDirty();
    }

    public void restorePlayerData(ServerPlayer player) {
        CompoundTag tag = playerData.get(player.getUUID());
        if (tag != null) {
            // Inventory
            player.getInventory().load(tag.getList("Inventory", 10));
            // Health
            player.setHealth(tag.getFloat("Health"));
            // Food
            player.getFoodData().setFoodLevel(tag.getInt("FoodLevel"));
            player.getFoodData().setSaturation(tag.getFloat("SaturationLevel"));
            // XP
            player.experienceLevel = tag.getInt("XpLevel");
            player.experienceProgress = tag.getFloat("XpProgress");
            player.totalExperience = tag.getInt("XpTotal");
            // Creative Inventory (if available)
            if (tag.contains("CreativeInventory")) {
                // Restore creative inventory if needed
            }
        } else {
            // No data: clear inventory, reset health/food/xp
            player.getInventory().clearContent();
            player.setHealth(player.getMaxHealth());
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(5.0f);
            player.experienceLevel = 0;
            player.experienceProgress = 0;
            player.totalExperience = 0;
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, CompoundTag> entry : playerData.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("UUID", entry.getKey());
            entryTag.put("Data", entry.getValue());
            list.add(entryTag);
        }
        compoundTag.put("Players", list);
        return compoundTag;
    }

    public static CreativeWorldPlayerData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        CreativeWorldPlayerData data = new CreativeWorldPlayerData();
        ListTag list = tag.getList("Players", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            UUID uuid = entryTag.getUUID("UUID");
            CompoundTag playerTag = entryTag.getCompound("Data");
            data.playerData.put(uuid, playerTag);
        }
        return data;
    }
}