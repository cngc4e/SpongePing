package com.cngc4e.spongeping.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.cngc4e.spongeping.SpongePing;
import com.cngc4e.spongeping.configuration.SPConfig;

public class PingCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player target = null;
        Optional<Player> op = args.<Player>getOne("player");

        if (op.isPresent()) {
            target = op.get();
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(
                    SPConfig.COMMAND_PING_OTHERS_MESSAGE
                            .replace("%ping%", String.valueOf(target.getConnection().getLatency()))
                            .replace("%target%", target.getName())
                    ));
        } else if (src instanceof Player) {
            target = (Player) src;
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(
                    SPConfig.COMMAND_PING_MESSAGE.replace("%ping%", String.valueOf(target.getConnection().getLatency()))
                    ));
        }

        return CommandResult.success();
    }

    public static Builder getCommandBuilder() {
        return CommandSpec.builder()
                .permission("spongeping.use")
                .arguments(
                        GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
                        )
                .executor(new PingCmd())
                .description(Text.of("Retrieve the ping (latency) of yourself or another player."));
    }
}
