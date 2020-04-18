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
    private ConfigurationNode config;
    
    private static SPConfig instance = null;
    
    /* Singleton pattern */
    protected SPConfig() {}
    
    public static SPConfig getInstance() {
        if (instance == null){
            instance = new SPConfig();
        }
        return instance;
    }
    
    public void init(Path path, ConfigurationLoader<CommentedConfigurationNode> loader, Logger logger) {
        this.defaultConfig = path;
        this.configManager = loader;
        this.logger = logger;
    }
    
    public void createAndLoadConfig() {
        try {
            this.logger.info("gettedth path " + this.defaultConfig.toString());
            if (Files.notExists(this.defaultConfig)) {
                this.defaultConfig.toFile().createNewFile();
                config = configManager.load();

                config.getNode("command", "ping-message").setValue("[PING] %ping%");
                config.getNode("tablist", "update-interval").setValue(5);
                config.getNode("tablist", "force-update-ping").setValue(5);
                configManager.save(config);
                this.logger.info("Created default configuration, ConfigDatabase will not run until you have edited this file!");

            }
            config = configManager.load();
        } catch (IOException e) {
            this.logger.error("Couldn't load configuration file!");
        }
    }
    
    public boolean reload() {
        if (configManager == null) {
            this.logger.error("Couldn't load configuration file!");
            return false;
        }
        try {
            config = configManager.load();
            return true;
        } catch (IOException e) {
            this.logger.error("Couldn't load configuration file!");
            return false;
        }
    }
}
