import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BG = new Color(24, 26, 33);
    public static final Color PANEL = new Color(36, 39, 50);
    public static final Color FIELD = new Color(50, 54, 68);
    public static final Color TEXT = new Color(230, 232, 240);
    public static final Color ACCENT = new Color(120, 160, 255);
    public static final Color ERROR = new Color(255, 100, 100);
    public static final Color SUCCESS = new Color(100, 220, 140);

    public static void install() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        put(BG, "Panel.background", "OptionPane.background", "ScrollPane.background", "Viewport.background",
                "TabbedPane.background", "TabbedPane.contentAreaColor", "Table.selectionForeground",
                "List.selectionForeground", "ComboBox.selectionForeground");
        put(PANEL, "TabbedPane.selected", "TableHeader.background", "Table.gridColor", "Button.select");
        put(FIELD, "TextField.background", "TextArea.background", "ComboBox.background",
                "ComboBox.disabledBackground", "List.background", "Table.background", "Button.background",
                "ProgressBar.background");
        put(TEXT, "Label.foreground", "TextField.foreground", "TextField.caretForeground",
                "TextArea.foreground", "TextArea.caretForeground", "ComboBox.foreground", "List.foreground",
                "Table.foreground", "TableHeader.foreground", "Button.foreground", "TabbedPane.foreground",
                "OptionPane.messageForeground", "ProgressBar.selectionBackground");
        put(ACCENT, "Table.selectionBackground", "List.selectionBackground", "ComboBox.selectionBackground");
        put(Color.GRAY, "Button.disabledText", "ComboBox.disabledForeground");
        put(new Color(60, 160, 90), "ProgressBar.foreground");
        put(Color.WHITE, "ProgressBar.selectionForeground");
    }

    private static void put(Color color, String... keys) {
        for (String key : keys) {
            UIManager.put(key, color);
        }
    }
}