package github.regularrabbit05.voicecraft;

import net.minecraft.client.Minecraft;

import java.util.HashMap;

public class Commands {
    public final HashMap<String, CommandAction> commands;
    public Commands() {
        this.commands = new HashMap<>();
    }

    public void addCommand(String activation, String command) {
        addCommand(activation, command, 1);
    }

    public void addCommand(String activation, String command, int times) {
        commands.put(activation, new CommandAction(command, times));
    }

    public void findAndExecute(String from) {
        assert Minecraft.getInstance().player != null;
        String[] split = from.split("\\s+");
        for (String s : commands.keySet()) {
            for (int i = 0; i < split.length; i++) {
                if (!s.toLowerCase().contains(split[i].toLowerCase())) continue;
                if (i == split.length-1) {
                    commands.get(s).run();
                    return;
                }
            }
        }
    }
}
