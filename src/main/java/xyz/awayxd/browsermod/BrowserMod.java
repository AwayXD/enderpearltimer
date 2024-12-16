package xyz.awayxd.browsermod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BrowserMod.MODID, name = BrowserMod.NAME, version = BrowserMod.VERSION)
public class BrowserMod {
    public static final String MODID = "browsermod";
    public static final String NAME = "Browser Mod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}

// i have no idea they gone noW???