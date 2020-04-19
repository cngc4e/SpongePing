package com.cngc4e.spongeping;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import com.cngc4e.spongeping.commands.PingCmd;
import com.cngc4e.spongeping.commands.ReloadCmd;
import com.cngc4e.spongeping.configuration.SPConfig;
import com.cngc4e.spongeping.tablist.PingTablist;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION)
public class SpongePing {
    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;
    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    public static Game game;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        this.logger.info("Version " + PluginInfo.VERSION);
    }

    @Listener
    public void onInitialization(GameInitializationEvent event) {
        SPConfig cfg = SPConfig.getInstance();
        cfg.init(defaultConfig, configManager, this.logger);
        cfg.createAndLoadConfig();
    }

    @Listener
    public void aboutToStart(GameAboutToStartServerEvent event) {
        Sponge.getCommandManager().register(this,
                PingCmd.getCommandBuilder()
                .child(ReloadCmd.getCommandBuilder()
                        .build(), "reload")
                .build(), "ping", "sping");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Task task = Task.builder().execute(new PingTablist())
                .interval(SPConfig.TABLIST_UPDATE_INTERVAL, TimeUnit.SECONDS)
                .submit(this);
    }

    public Logger getLogger() {
        return this.logger;
    }
}
