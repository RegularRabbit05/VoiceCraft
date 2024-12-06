package github.regularrabbit05.voicecraft;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Mod(Voicecraft.MODID)
public class Voicecraft {
    public static final String MODID = "voicecraft";
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Commands commands = new Commands();

    public Voicecraft(FMLJavaModLoadingContext context) {
        commands.addCommand("kill", "kill");
        commands.addCommand("die", "kill");
        commands.addCommand("died", "kill");
        commands.addCommand("dying", "kill");
        commands.addCommand("dead", "kill");

        commands.addCommand("sheep", "summon sheep", 20);
        commands.addCommand("iron golem", "summon iron_golem", 10);
        commands.addCommand("creeper", "summon creeper", 50);
        commands.addCommand("dragon", "summon ender_dragon");
        commands.addCommand("chicken", "summon chicken", 200);
        commands.addCommand("wither", "summon wither", 100);
        commands.addCommand("fish", "summon pufferfish", 20);
        commands.addCommand("shulker", "summon shulker", 1);
        commands.addCommand("warden", "summon warden", 5);
        commands.addCommand("zombie", "summon zombie", 10);
        commands.addCommand("skeleton", "summon skeleton", 10);
        commands.addCommand("piglin", "summon piglin", 20);
        commands.addCommand("pig", "summon pig", 15);
        commands.addCommand("illusioner", "summon illusioner", 50);
        commands.addCommand("hoglin", "summon hoglin", 30);
        commands.addCommand("zombies", "summon giant", 10);
        commands.addCommand("enderman", "summon enderman", 20);
        commands.addCommand("evoker", "summon evoker", 15);
        commands.addCommand("cow", "summon cow", 50);
        commands.addCommand("blaze", "summon blaze", 20);
        commands.addCommand("spider", "summon spider", 10);

        commands.addCommand("rain", "summon lightning_bolt", 1);
        commands.addCommand("raining", "summon lightning_bolt", 1);

        commands.addCommand("mining", "effect give @s minecraft:mining_fatigue 30 100");
        commands.addCommand("mine", "effect give @s minecraft:mining_fatigue 30 100");
        commands.addCommand("run", "effect give @s minecraft:speed 30 100");
        commands.addCommand("running", "effect give @s minecraft:speed 30 100");
        commands.addCommand("speed", "effect give @s minecraft:speed 30 100");
        commands.addCommand("hungry", "effect give @s minecraft:speed 30 100");
        commands.addCommand("hunger", "effect give @s minecraft:speed 30 100");
        commands.addCommand("hungary", "effect give @s minecraft:speed 30 100");
        commands.addCommand("fat", "effect give @s minecraft:speed 30 100");
        commands.addCommand("stuck", "effect give @s minecraft:jump_boost 30 100");

        commands.addCommand("diamonds", "fill ~-10 ~-10 ~-10 ~10 ~10 ~10 minecraft:stone replace minecraft:diamond_ore");
        commands.addCommand("diamond", "fill ~-10 ~-10 ~-10 ~10 ~10 ~10 minecraft:stone replace minecraft:diamond_ore");
        commands.addCommand("iron", "fill ~-10 ~-10 ~-10 ~10 ~10 ~10 minecraft:stone replace minecraft:iron_ore");
        commands.addCommand("gold", "fill ~-10 ~-10 ~-10 ~10 ~10 ~10 minecraft:stone replace minecraft:gold_ore");

        commands.addCommand("fuck", "attribute @s minecraft:max_health base set 1");
        commands.addCommand("sorry", "attribute @s minecraft:max_health base set 20");

        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::onCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        new Thread(() -> {
            try {
                ServerSocket s = new ServerSocket(56864);
                while (Minecraft.getInstance().isRunning()) {
                    Socket sock;
                    try {
                        sock = s.accept();
                    } catch (Exception e) {
                        LOGGER.error("Unable to accept: {}", e.getMessage());
                        continue;
                    }
                    try {
                        DataInputStream in = new DataInputStream(sock.getInputStream());
                        while (Minecraft.getInstance().isRunning()) {
                            if (Minecraft.getInstance().player == null) continue;
                            String next = in.readUTF();
                            LOGGER.info("Received: {}", next);
                            commands.findAndExecute(next);
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        if (Minecraft.getInstance().player != null) Minecraft.getInstance().player.displayClientMessage(Component.literal("Recognition connection lost"), true);
                    }
                }
                s.close();
            } catch (IOException e) {
                throw new RuntimeException("Unable to start listenServer: " + e.getMessage());
            }
        }).start();
    }
}
