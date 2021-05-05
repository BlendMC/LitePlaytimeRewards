package com.backtobedrock.LitePlaytimeRewards.commands;

import com.backtobedrock.LitePlaytimeRewards.domain.enumerations.Command;
import com.backtobedrock.LitePlaytimeRewards.domain.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPlaytime extends AbstractCommand {

    public CommandPlaytime(CommandSender cs, String[] args) {
        super(cs, args);
    }

    @Override
    public void run() {
        switch (this.args.length) {
            case 0:
                //check if player
                if (this.checkIfPlayer() && this.checkPermission("playtime")) {
                    if (this.plugin.getLPRConfig().isCountAllPlaytime()) {
                        this.cs.sendMessage(this.plugin.getMessages().getPlaytime(this.sender.getStatistic(Statistic.PLAY_ONE_MINUTE)));
                    } else {
                        //get player data
                        PlayerData crudplay = this.plugin.getPlayerCache().get(this.sender.getUniqueId());
                        this.cs.sendMessage(this.plugin.getMessages().getPlaytime(crudplay.getPlaytime() + crudplay.getAfktime()));
                    }
                }
                break;
            case 1:
                //check for permission
                if (this.checkPermission("playtime.other")) {
                    OfflinePlayer plyrplayother = Bukkit.getOfflinePlayer(args[0]);

                    if (this.plugin.getLPRConfig().isCountAllPlaytime() && plyrplayother.isOnline()) {
                        this.cs.sendMessage(this.plugin.getMessages().getPlaytimeOther(((Player) plyrplayother).getStatistic(Statistic.PLAY_ONE_MINUTE), plyrplayother.getName()));
                        break;
                    }

                    //check if player has played on server before
                    if (!PlayerData.doesPlayerDataExists(plyrplayother)) {
                        this.cs.sendMessage(this.plugin.getMessages().getNoData(plyrplayother.getName()));
                        break;
                    }

                    //get player data
                    PlayerData crudplayother = plyrplayother.isOnline()
                            ? this.plugin.getPlayerCache().get(plyrplayother.getUniqueId())
                            : new PlayerData(plyrplayother);
                    this.cs.sendMessage(this.plugin.getMessages().getPlaytimeOther(crudplayother.getPlaytime() + crudplayother.getAfktime(), plyrplayother.getName()));
                }
                break;
            default:
                this.sendUsageMessage(Command.PLAYTIME);
                break;
        }
    }
}
