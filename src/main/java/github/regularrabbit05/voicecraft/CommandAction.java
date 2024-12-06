package github.regularrabbit05.voicecraft;

import net.minecraft.client.Minecraft;

public class CommandAction {
    private final String command;
    private final int times;
    public CommandAction(String command, int times) {
        this.command = command;
        this.times = times;
    }

    public void run() {
        assert Minecraft.getInstance().player != null;
        for (int i = 0; i < times; i++) Minecraft.getInstance().player.connection.sendUnsignedCommand(command);
    }
}
