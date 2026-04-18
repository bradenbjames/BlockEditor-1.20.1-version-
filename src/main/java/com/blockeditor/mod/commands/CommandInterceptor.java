package com.blockeditor.mod.commands;

import com.blockeditor.mod.worldedit.BlockNameResolver;

/**
 * Utility class that replaces custom block names with actual registry names in commands.
 * This allows WorldEdit commands like //set be:customname to work.
 * In Fabric, command interception is handled via chat message events in
 * integration/WorldEditIntegration rather than a CommandEvent (which doesn't exist).
 */
public class CommandInterceptor {

    public static boolean containsBlockReference(String command) {
        return command.contains("//set ") ||
               command.contains("//replace ") ||
               command.contains("//walls ") ||
               command.contains("//faces ") ||
               command.contains("/setblock ") ||
               command.contains("/fill ") ||
               command.contains("be:");
    }

    public static String replaceCustomBlockNames(String command) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("be:([a-zA-Z0-9_]+)");
        java.util.regex.Matcher matcher = pattern.matcher(command);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String customName = matcher.group(1);

            if (customName.startsWith("u_")) {
                matcher.appendReplacement(sb, matcher.group(0));
                continue;
            }

            String registryName = BlockNameResolver.getRegistryName(customName);
            if (registryName != null) {
                matcher.appendReplacement(sb, registryName);
            } else {
                matcher.appendReplacement(sb, matcher.group(0));
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}