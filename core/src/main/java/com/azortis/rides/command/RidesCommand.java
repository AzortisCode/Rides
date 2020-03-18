package com.azortis.rides.command;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import org.bukkit.command.CommandSender;

public class RidesCommand implements ICommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
