package com.cngc4e.spongeping.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class SPConfig { 
    private Logger logger;
    private Path defaultConfig;
    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    private static SPConfig instance = null;

    /* Initialize with default config values */
    public static String COMMAND_PING_MESSAGE = "&a[PING] %ping%ms";
    public static int TABLIST_UPDATE_INTERVAL = 5;
    public static boolean TABLIST_FORCE_UPDATE_PING = true;

    /* Singleton pattern */
    protected SPConfig() {}

    public static SPConfig getInstance() {
        if (instance == null) {
            instance = new SPConfig();
        }
        return instance;
    }

    public void init(Path path, ConfigurationLoader<CommentedConfigurationNode> loader, Logger logger) {
        this.defaultConfig = path;
        this.configManager = loader;
        this.logger = logger;
    }

    public boolean createAndLoadConfig() {
        if (configManager == null) {
            this.logger.error("Couldn't load configuration file! Null configManager.");
            return false;
        }
        if (Files.notExists(this.defaultConfig)) {
            /* Create default config */
            try {
                this.defaultConfig.toFile().createNewFile();
                CommentedConfigurationNode defconfig = configManager.load();

                defconfig.getNode("command", "ping-message")
                .setValue(SPConfig.COMMAND_PING_MESSAGE)
                .setComment("Customise the ping message reply. Use %ping% to indicate the ping (in milliseconds).");
                defconfig.getNode("tablist", "update-interval")
                .setValue(SPConfig.TABLIST_UPDATE_INTERVAL)
                .setComment("Sets how frequent the tab list should update (in seconds)");
                defconfig.getNode("tablist", "force-update-ping")
                .setValue(SPConfig.TABLIST_FORCE_UPDATE_PING)
                .setComment("Should the plugin forcefully refresh the pings each time the tab list updates?\nIf false, players' latency will be updated as per normal by the server.");

                configManager.save(defconfig);
                this.logger.info("No existing configuration file was found, created one.");
            } catch (IOException e) {
                this.logger.error("Couldn't create configuration file!");
                return false;
            }
        } else {
            /* Reload configs */
            try {
                ConfigurationNode config = configManager.load();
                SPConfig.COMMAND_PING_MESSAGE = config.getNode("command", "ping-message").getString(SPConfig.COMMAND_PING_MESSAGE);
                SPConfig.TABLIST_UPDATE_INTERVAL = config.getNode("tablist", "update-interval").getInt(SPConfig.TABLIST_UPDATE_INTERVAL);
                SPConfig.TABLIST_FORCE_UPDATE_PING = config.getNode("tablist", "force-update-ping").getBoolean(SPConfig.TABLIST_FORCE_UPDATE_PING);
            } catch (IOException e) {
                this.logger.error("Couldn't load configuration file! " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}
