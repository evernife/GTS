package net.impactdev.gts.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.impactdev.gts.common.plugin.permissions.GTSPermissions;
import net.impactdev.gts.ui.SpongeMainMenu;
import net.impactdev.gts.util.GTSInfoGenerator;
import net.impactdev.impactor.api.Impactor;
import net.impactdev.gts.common.plugin.GTSPlugin;
import net.impactdev.gts.ui.admin.SpongeAdminMenu;
import net.impactdev.impactor.api.services.text.MessageService;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

@CommandAlias("gts")
@CommandPermission(GTSPermissions.DEFAULT)
public class GTSCommand extends BaseCommand {

    @Default
    public void execute(Player issuer) {
        new SpongeMainMenu(issuer).open();
    }

    @CatchUnknown
    public void invalid(CommandIssuer issuer) {

    }

    @Subcommand("admin")
    @CommandPermission(GTSPermissions.ADMIN_BASE)
    public class Admin extends BaseCommand {

        @Default
        public void execute(CommandIssuer issuer) {
            if(issuer.isPlayer()) {
                Player player = issuer.getIssuer();
                new SpongeAdminMenu(player).open();
            }
        }

        @Subcommand("info")
        @CommandPermission(GTSPermissions.ADMIN_INFO)
        public void processInfoRequest(CommandIssuer issuer) {
            new GTSInfoGenerator(issuer).create(issuer)
                    .thenAccept(x -> {
                        issuer.sendMessage("Report saved to: " + x);
                    });
        }

        @Subcommand("ping")
        @CommandPermission(GTSPermissions.ADMIN_PING)
        public void processPingRequest(CommandIssuer issuer) {
            MessageService<Text> service = Impactor.getInstance().getRegistry().get(MessageService.class);
            GTSPlugin.getInstance().getMessagingService()
                    .sendPing()
                    .thenAccept(pong -> {
                        if(issuer.getIssuer() instanceof Player) {
                            if(pong.wasSuccessful()) {
                                ((Player) issuer.getIssuer()).sendMessage(service.parse(
                                        "&eGTS &7\u00bb Ping request &asuccessful&7, took &b" + pong.getResponseTime() + " ms&7!"
                                ));
                            }
                        }
                    });
        }

    }

}