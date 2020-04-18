package com.cngc4e.spongeping.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.cngc4e.spongeping.SpongePing;
import com.cngc4e.spongeping.configuration.SPConfig;

public class PingCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Player p = (Player) src;
            p.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(
                    SPConfig.COMMAND_PING_MESSAGE.replace("%ping%", String.valueOf(p.getConnection().getLatency()))
                    ));
        }
        return CommandResult.success();
    }
}