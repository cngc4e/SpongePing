package com.cngc4e.spongeping.tablist;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.cngc4e.spongeping.configuration.SPConfig;

public class PingTablist implements Consumer<Task> {
    @Override
    public void accept(Task task) {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            for (TabListEntry t : player.getTabList().getEntries()) {
                if (SPConfig.TABLIST_FORCE_UPDATE_PING) {
                    Optional<Player> p = Sponge.getServer().getPlayer(t.getProfile().getUniqueId());
                    if (p.isPresent()) {
                        t.setLatency(p.get().getConnection().getLatency());
                    }
                }
                t.setDisplayName(TextSerializers.FORMATTING_CODE.deserialize("[&a" + String.valueOf(t.getLatency()) + "ms&r] " + t.getProfile().getName().get()));
            }
        }
    }
}
