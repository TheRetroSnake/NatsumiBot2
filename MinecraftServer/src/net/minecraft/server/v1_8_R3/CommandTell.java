package net.minecraft.server.v1_8_R3;

import java.util.Arrays;
import java.util.List;

public class CommandTell extends CommandAbstract {

    public CommandTell() {}

    public List<String> b() {
        return Arrays.asList(new String[] { "w", "msg"});
    }

    public String getCommand() {
        return "tell";
    }

    public int a() {
        return 0;
    }

    public String getUsage(ICommandListener icommandlistener) {
        return "commands.message.usage";
    }

    public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new ExceptionUsage("commands.message.usage", new Object[0]);
        } else {
            EntityPlayer entityplayer = a(icommandlistener, astring[0]);

            if (entityplayer == icommandlistener) {
                throw new ExceptionPlayerNotFound("commands.message.sameTarget", new Object[0]);
            } else {
                IChatBaseComponent ichatbasecomponent = b(icommandlistener, astring, 1, !(icommandlistener instanceof EntityHuman));
                ChatMessage chatmessage = new ChatMessage("commands.message.display.incoming", new Object[] { icommandlistener.getScoreboardDisplayName(), ichatbasecomponent.f()});
                ChatMessage chatmessage1 = new ChatMessage("commands.message.display.outgoing", new Object[] { entityplayer.getScoreboardDisplayName(), ichatbasecomponent.f()});

                chatmessage.getChatModifier().setColor(EnumChatFormat.GRAY).setItalic(Boolean.valueOf(true));
                chatmessage1.getChatModifier().setColor(EnumChatFormat.GRAY).setItalic(Boolean.valueOf(true));
                entityplayer.sendMessage(chatmessage);
                icommandlistener.sendMessage(chatmessage1);
            }
        }
    }

    public List<String> tabComplete(ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return a(astring, MinecraftServer.getServer().getPlayers());
    }

    public boolean isListStart(String[] astring, int i) {
        return i == 0;
    }
}
