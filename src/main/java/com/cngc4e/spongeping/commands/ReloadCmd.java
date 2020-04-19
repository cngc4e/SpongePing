package com.cngc4e.spongeping.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;

import com.cngc4e.spongeping.configuration.SPConfig;

public class ReloadCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (SPConfig.getInstance().createAndLoadConfig()) {
            src.sendMessage(Text.of("Plugin reloaded successfully."));
            return CommandResult.success();
        } else {
            src.sendMessage(Text.of("Plugin could not reload."));
            return CommandResult.success();
        }
    }

    public static Builder getCommandBuilder() {
        return CommandSpec.builder()
                .executor(new ReloadCmd())
                .description(Text.of("Used to reload configuration"));
    }
}
