package xyz.awayxd.browsermod;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class BrowserOverlay {
    private CefBrowser browser;
    private int x = 100, y = 100, width = 800, height = 600;
    private boolean dragging = false, resizing = false;
    private int dragStartX, dragStartY;
    private int resizeStartX, resizeStartY;
    private boolean isUrlBarFocused = false;
    private String currentUrl = "https://google.com";

    public BrowserOverlay() {
        CefApp cefApp = CefApp.getInstance();
        browser = cefApp.createClient().createBrowser(currentUrl, false, false);
    }

    public void renderOverlay(int mouseX, int mouseY) {
        // Render the browser content
        GL11.glPushMatrix();
        // Rendering browser as texture goes here...
        GL11.glPopMatrix();

        // Draw the URL bar
        Gui.drawRect(x, y, x + width, y + 20, 0xFF202020);
        Minecraft.getMinecraft().fontRendererObj.drawString("URL: " + currentUrl, x + 5, y + 5, isUrlBarFocused ? 0xFFFF00 : 0xFFFFFF);

        // Draw the resize handle
        Gui.drawRect(x + width - 10, y + height - 10, x + width, y + height, 0xFF555555);
    }

    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (mouseX > x + 5 && mouseX < x + width - 10 && mouseY > y && mouseY < y + 20) {
            isUrlBarFocused = true;
        } else if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + 20) {
            dragging = true;
            dragStartX = mouseX - x;
            dragStartY = mouseY - y;
        } else if (mouseX > x + width - 10 && mouseY > y + height - 10) {
            resizing = true;
            resizeStartX = mouseX - width;
            resizeStartY = mouseY - height;
        } else {
            isUrlBarFocused = false;
        }
    }

    public void onMouseRelease() {
        dragging = false;
        resizing = false;
    }

    public void onMouseDrag(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragStartX;
            y = mouseY - dragStartY;
        } else if (resizing) {
            width = Math.max(200, mouseX - resizeStartX);
            height = Math.max(150, mouseY - resizeStartY);
        }
    }

    public void onKeyPress(char typedChar, int keyCode) {
        if (isUrlBarFocused) {
            if (keyCode == 28) { // Enter key
                browser.loadURL(currentUrl);
                isUrlBarFocused = false;
            } else if (keyCode == 14) { // Backspace
                if (!currentUrl.isEmpty()) {
                    currentUrl = currentUrl.substring(0, currentUrl.length() - 1);
                }
            } else {
                currentUrl += typedChar;
            }
        }
    }
}
