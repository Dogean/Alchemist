package ltd.matrixstudios.alchemist.aikar

import co.aikar.commands.BukkitMessageFormatter
import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import ltd.matrixstudios.alchemist.AlchemistSpigotPlugin
import ltd.matrixstudios.alchemist.aikar.context.*
import ltd.matrixstudios.alchemist.chatcolors.ChatColorLoader
import ltd.matrixstudios.alchemist.chatcolors.commands.ChatColorCommands
import ltd.matrixstudios.alchemist.commands.admin.AdminChatCommand
import ltd.matrixstudios.alchemist.commands.admin.panel.AdminPanelCommand
import ltd.matrixstudios.alchemist.commands.alts.AltsCommand
import ltd.matrixstudios.alchemist.commands.branding.AlchemistCommand
import ltd.matrixstudios.alchemist.commands.filter.FilterCommands
import ltd.matrixstudios.alchemist.commands.coins.CoinsCommand
import ltd.matrixstudios.alchemist.commands.disguise.RankDisguiseCommand
import ltd.matrixstudios.alchemist.friends.commands.FriendCommands
import ltd.matrixstudios.alchemist.grants.apply.CGrantCommand
import ltd.matrixstudios.alchemist.grants.apply.GrantCommand
import ltd.matrixstudios.alchemist.grants.apply.NonModelGrantCommand
import ltd.matrixstudios.alchemist.grants.configure.GrantConfigureCommand
import ltd.matrixstudios.alchemist.commands.metrics.MetricCommand
import ltd.matrixstudios.alchemist.commands.notes.PlayerNotesCommands
import ltd.matrixstudios.alchemist.commands.party.PartyCommands
import ltd.matrixstudios.alchemist.commands.rank.GenericRankCommands
import ltd.matrixstudios.alchemist.servers.commands.ServerEnvironmentCommand
import ltd.matrixstudios.alchemist.commands.sessions.SessionCommands
import ltd.matrixstudios.alchemist.commands.tags.TagAdminCommand
import ltd.matrixstudios.alchemist.commands.tags.TagCommand
import ltd.matrixstudios.alchemist.commands.tags.grants.TagGrantCommand
import ltd.matrixstudios.alchemist.commands.tags.grants.TagGrantsCommand
import ltd.matrixstudios.alchemist.commands.uuid.UUIDCacheCommands
import ltd.matrixstudios.alchemist.commands.vouchers.VoucherCommand
import ltd.matrixstudios.alchemist.convert.luckperms.LuckPermsConverterCommand
import ltd.matrixstudios.alchemist.grants.view.AuditCommand
import ltd.matrixstudios.alchemist.grants.view.GrantHistoryCommand
import ltd.matrixstudios.alchemist.grants.view.GrantsCommand
import ltd.matrixstudios.alchemist.models.grant.types.scope.GrantScope
import ltd.matrixstudios.alchemist.models.profile.GameProfile
import ltd.matrixstudios.alchemist.models.ranks.Rank
import ltd.matrixstudios.alchemist.models.ranks.scope.RankScope
import ltd.matrixstudios.alchemist.profiles.AsyncGameProfile
import ltd.matrixstudios.alchemist.profiles.commands.player.*
import ltd.matrixstudios.alchemist.punishments.PunishmentType
import ltd.matrixstudios.alchemist.queue.command.ModifyQueueCommands
import ltd.matrixstudios.alchemist.queue.command.QueueCommands
import ltd.matrixstudios.alchemist.servers.commands.BroadcastCommand
import ltd.matrixstudios.alchemist.themes.commands.ThemeSelectCommand
import org.bukkit.ChatColor
import java.util.UUID

object ACFCommandController {

    private val config = AlchemistSpigotPlugin.instance.config

    fun registerAll()
    {
        AlchemistSpigotPlugin.instance.commandManager = PaperCommandManager(AlchemistSpigotPlugin.instance).apply {

            this.commandContexts.registerContext(GameProfile::class.java, GameProfileContextResolver())
            this.commandContexts.registerContext(Rank::class.java, RankContextResolver())
            this.commandContexts.registerContext(PunishmentType::class.java, PunishmentTypeResolver())
            this.commandContexts.registerContext(UUID::class.java, UUIDContextResolver())
            this.commandContexts.registerContext(GrantScope::class.java, GrantScopeContextResolver())
            this.commandContexts.registerContext(AsyncGameProfile::class.java, AsyncProfileResolver())
            this.commandContexts.registerContext(RankScope::class.java, RankScopeResolver())


            this.commandCompletions.registerCompletion("gameprofile") {
                return@registerCompletion AlchemistSpigotPlugin.instance.server.onlinePlayers.map { it.name }.toList()
            }

            this.enableUnstableAPI("help")

            this.setFormat(MessageType.SYNTAX, BukkitMessageFormatter(ChatColor.GOLD, ChatColor.YELLOW, ChatColor.WHITE))
            this.setFormat(MessageType.HELP, BukkitMessageFormatter(ChatColor.GOLD, ChatColor.YELLOW, ChatColor.WHITE))

            if (config.getBoolean("modules.ranks")) {
                registerCommand(GenericRankCommands())
                registerCommand(GrantCommand())
                registerCommand(GrantsCommand)
                registerCommand(CGrantCommand())
                registerCommand(GrantConfigureCommand())
                registerCommand(NonModelGrantCommand())
                registerCommand(GrantHistoryCommand())
                registerCommand(LuckPermsConverterCommand())
                registerCommand(WipeGrantsCommand)
            }

            registerCommand(AdminPanelCommand())

            if (config.getBoolean("modules.vouchers")) {
                registerCommand(VoucherCommand())
            }

            registerCommand(BroadcastCommand)

            registerCommand(CoinsCommand())

            if (config.getBoolean("modules.themeCommands")) {
                registerCommand(ThemeSelectCommand())
            }

            if (AlchemistSpigotPlugin.instance.config.getBoolean("modules.queue")) {
                registerCommand(QueueCommands())
                registerCommand(ModifyQueueCommands())
            }

            registerCommand(RankDisguiseCommand())

            registerCommand(UUIDCacheCommands)


            registerCommand(AlchemistCommand())

            if (config.getBoolean("modules.chatcolors")) {
                ChatColorLoader.loadAllChatColors()
                registerCommand(ChatColorCommands())
            }

            registerCommand(AuditCommand)

            registerCommand(AltsCommand())

            if (config.getBoolean("modules.notes")) {
                registerCommand(PlayerNotesCommands())
            }

            if (config.getBoolean("modules.prefixes")) {
                registerCommand(TagAdminCommand())
                registerCommand(TagCommand())
                registerCommand(TagGrantCommand())
                registerCommand(TagGrantsCommand())
            }


            if (config.getBoolean("modules.filters")) {
                registerCommand(FilterCommands(), true)
            }

            if (config.getBoolean("modules.friends")) {
                registerCommand(FriendCommands(), true)
            }

            registerCommand(ServerEnvironmentCommand())
            registerCommand(AdminChatCommand())

            registerCommand(MetricCommand())

            registerCommand(SessionCommands())

            if (config.getBoolean("modules.parties")) {
                registerCommand(PartyCommands())
            }

            registerCommand(LookupCommand())
        }

    }
}