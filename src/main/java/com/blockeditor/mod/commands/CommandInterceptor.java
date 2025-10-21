package com.blockeditor.mod.commands;

import com.blockeditor.mod.worldedit.BlockNameResolver;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Intercepts commands and replaces custom block names with actual registry names
 * This allows WorldEdit commands like //set be:customname to work
 */
@Mod.EventBusSubscriber(modid = "be")
public class CommandInterceptor {
    
    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        String command = event.getParseResults().getReader().getString();
        
        // Debug: Always log commands that start with //set
        if (command.startsWith("//set")) {
            System.out.println("COMMAND INTERCEPTOR: Detected //set command: " + command);
        }
        
        // Check if this is a command that might contain block names
        if (containsBlockReference(command)) {
            System.out.println("COMMAND INTERCEPTOR: Command contains block reference: " + command);
            String modifiedCommand = replaceCustomBlockNames(command);
            System.out.println("COMMAND INTERCEPTOR: Original: " + command);
            System.out.println("COMMAND INTERCEPTOR: Modified: " + modifiedCommand);
            if (!modifiedCommand.equals(command)) {
                // Command was modified, we need to re-parse it
                System.out.println("COMMAND INTERCEPTOR: *** COMMAND CHANGED - INTERCEPTING ***");
                System.out.println("  Original: " + command);
                System.out.println("  Modified: " + modifiedCommand);
                
                // Cancel the original command and execute the modified one
                event.setCanceled(true);
                
                // Execute the modified command
                try {
                    event.getParseResults().getContext().getSource().getServer()
                        .getCommands().performPrefixedCommand(
                            event.getParseResults().getContext().getSource(), 
                            modifiedCommand
                        );
                } catch (Exception e) {
                    System.err.println("Failed to execute modified command: " + e.getMessage());
                }
            }
        }
    }
    
    private static boolean containsBlockReference(String command) {
        // Check for common commands that use block names
        return command.contains("//set ") || 
               command.contains("//replace ") ||
               command.contains("//walls ") ||
               command.contains("//faces ") ||
               command.contains("/setblock ") ||
               command.contains("/fill ") ||
               command.contains("be:");
    }
    
    private static String replaceCustomBlockNames(String command) {
        String modifiedCommand = command;
        
        System.out.println("REPLACE DEBUG: Processing command: " + command);
        
        // Find all "be:customname" patterns and replace them
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("be:([a-zA-Z0-9_]+)");
        java.util.regex.Matcher matcher = pattern.matcher(command);
        
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String customName = matcher.group(1);
            System.out.println("REPLACE DEBUG: Found pattern 'be:" + customName + "'");
            
            // Skip if it's already an internal name (starts with u_)
            if (customName.startsWith("u_")) {
                System.out.println("REPLACE DEBUG: Skipping internal name: " + customName);
                matcher.appendReplacement(sb, matcher.group(0));
                continue;
            }
            
            String registryName = BlockNameResolver.getRegistryName(customName);
            System.out.println("REPLACE DEBUG: BlockNameResolver returned: " + registryName);
            if (registryName != null) {
                // Replace be:customname with the actual registry name
                System.out.println("REPLACE DEBUG: Replacing 'be:" + customName + "' with '" + registryName + "'");
                matcher.appendReplacement(sb, registryName);
            } else {
                // Keep original if no mapping found
                System.out.println("REPLACE DEBUG: No mapping found, keeping original: be:" + customName);
                matcher.appendReplacement(sb, matcher.group(0));
            }
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
}