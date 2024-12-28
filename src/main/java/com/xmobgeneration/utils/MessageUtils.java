package com.xmobgeneration.utils;

import org.bukkit.ChatColor;

public class MessageUtils {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String formatMessage(String message, String... replacements) {
        String formatted = message;
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                formatted = formatted.replace(replacements[i], replacements[i + 1]);
            }
        }
        return colorize(formatted);
    }
}