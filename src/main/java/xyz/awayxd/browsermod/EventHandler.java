package xyz.awayxd.browsermod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

public class EventHandler {
    private BrowserOverlay browserOverlay = new BrowserOverlay();

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        int mouseX = Mouse.getEventX() * Minecraft.getMinecraft().displayWidth / Minecraft.getMinecraft().displayHeight;
        int mouseY = Minecraft.getMinecraft().displayHeight - Mouse.getEventY() * Minecraft.getMinecraft().displayHeight / Minecraft.getMinecraft().displayHeight;

        if (event.button >= 0 && event.buttonstate) {
            browserOverlay.onMouseClick(mouseX, mouseY, event.button);
        } else if (!event.buttonstate) {
            browserOverlay.onMouseRelease();
        }

        if (Mouse.isButtonDown(0)) {
            browserOverlay.onMouseDrag(mouseX, mouseY);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        browserOverlay.renderOverlay(Mouse.getX(), Mouse.getY());
    }

    @SubscribeEvent
    public void onKeyInput(KeyboardInputEvent.Post event) {
        char typedChar = Keyboard.getEventCharacter();
        int keyCode = Keyboard.getEventKey();
        browserOverlay.onKeyPress(typedChar, keyCode);
    }
}
