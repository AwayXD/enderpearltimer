package com.example.skywarscounter;

import com.example.skywarscounter.commands.CommandToggleCountdown;
import com.example.skywarscounter.commands.CommandSelectSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;

@Mod(modid = SkyWarsCounter.MODID, version = SkyWarsCounter.VERSION)
public class SkyWarsCounter {
    public static final String MODID = "skywarscounter";
    public static final String VERSION = "1.0";

    public static SkyWarsCounter instance;

    private Configuration config;
    private boolean countdownEnabled;
    private int selectedSlot;
    private boolean isSkyWars = false;
    private int countdown = 30;
    private long lastUpdateTime = 0;
    private boolean hadPearls = false;
    private long noPearlTime = 0;
    private boolean cooldownActive = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = new File(event.getModConfigurationDirectory(), "skywarscounter.cfg");
        config = new Configuration(configFile);
        loadConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandToggleCountdown());
        event.registerServerCommand(new CommandSelectSlot());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (countdownEnabled) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null) {
                boolean hasPearls = checkForPearlInSelectedSlot(mc);

                if (hasPearls && !hadPearls && !cooldownActive) {
                    hadPearls = true;
                    startCountdown();
                } else if (!hasPearls && hadPearls) {
                    hadPearls = false;
                    noPearlTime = System.currentTimeMillis(); // Start the no-pearl timer
                }

                // Reset after 30 seconds without pearls
                if (!hadPearls && noPearlTime != 0) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - noPearlTime >= 30000) {
                        resetCountdown();
                        cooldownActive = false; // Allow countdown to start again
                    } else {
                        cooldownActive = true; // Prevent countdown restart during this cooldown
                    }
                }
            }
        }
    }

    // Check for Ender Pearl in the user-selected slot
    private boolean checkForPearlInSelectedSlot(Minecraft mc) {
        if (selectedSlot >= 0 && selectedSlot <= 8) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(selectedSlot);
            return stack != null && stack.getItem() == net.minecraft.init.Items.ender_pearl;
        }
        return false;
    }

    private void startCountdown() {
        isSkyWars = true;
        countdown = 30;
        lastUpdateTime = System.currentTimeMillis();
    }

    private void resetCountdown() {
        isSkyWars = false;
        hadPearls = false;
        noPearlTime = 0;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (isSkyWars && countdownEnabled) {
            if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
                int xPos = event.resolution.getScaledWidth() - 385;
                int yPos = event.resolution.getScaledHeight() - 10;

                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                        "Pearl Countdown: " + countdown + "s",
                        xPos,
                        yPos,
                        0xFFFFFF
                );

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdateTime >= 1000) {
                    countdown--;
                    lastUpdateTime = currentTime;
                }

                if (countdown <= 0) {
                    isSkyWars = false;
                }
            }
        }
    }

    public void toggleCountdown() {
        countdownEnabled = !countdownEnabled;
        saveConfig();
    }

    public boolean isCountdownEnabled() {
        return countdownEnabled;
    }

    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot <= 8) {
            selectedSlot = slot;
            saveConfig();
        }
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    private void loadConfig() {
        countdownEnabled = config.getBoolean("countdownEnabled", Configuration.CATEGORY_GENERAL, true, "Whether the Ender Pearl countdown is enabled");
        selectedSlot = config.getInt("selectedSlot", Configuration.CATEGORY_GENERAL, 0, 0, 8, "The hotbar slot for Ender Pearl detection (0-8)");
        if (config.hasChanged()) {
            config.save();
        }
    }

    private void saveConfig() {
        config.get(Configuration.CATEGORY_GENERAL, "countdownEnabled", countdownEnabled).set(countdownEnabled);
        config.get(Configuration.CATEGORY_GENERAL, "selectedSlot", selectedSlot).set(selectedSlot);
        config.save();
    }
}
