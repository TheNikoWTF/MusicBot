/*
 * Copyright 2017 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.OwnerCommand;
import net.dv8tion.jda.core.entities.Game;

/**
 *
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SetgameCmd extends OwnerCommand
{
    public SetgameCmd(Bot bot)
    {
        this.name = "setgame";
        this.help = "establece a qu� esta jugando el bot";
        this.arguments = "[action] [game]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
        this.children = new OwnerCommand[]{
            new SetlistenCmd(),
            new SetstreamCmd(),
            new SetwatchCmd()
        };
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        String title = event.getArgs().toLowerCase().startsWith("playing") ? event.getArgs().substring(7).trim() : event.getArgs();
        try
        {
            event.getJDA().getPresence().setGame(title.isEmpty() ? null : Game.playing(title));
            event.reply(event.getClient().getSuccess()+" **"+event.getSelfUser().getName()
                    +"** ya no est� "+(title.isEmpty() ? "reproduciendo nada." : "ahora reproduciendo `"+title+"`"));
        }
        catch(Exception e)
        {
            event.reply(event.getClient().getError()+" No se ha podido establecer el mierdijuego!");
        }
    }
    
    private class SetstreamCmd extends OwnerCommand
    {
        private SetstreamCmd()
        {
            this.name = "stream";
            this.aliases = new String[]{"twitch","streaming"};
            this.help = "establece el juego al que est� jugando como stream";
            this.arguments = "<username> <game>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            String[] parts = event.getArgs().split("\\s+", 2);
            if(parts.length<2)
            {
                event.replyError("Incluye un nombre de usuario de Twitch y el nombre del juego para 'transmitir'");
                return;
            }
            try
            {
                event.getJDA().getPresence().setGame(Game.streaming(parts[1], "https://twitch.tv/"+parts[0]));
                event.replySuccess("**"+event.getSelfUser().getName()
                        +"** est� ahora emitiendo `"+parts[1]+"`");
            }
            catch(Exception e)
            {
                event.reply(event.getClient().getError()+" no se ha podido establecer!");
            }
        }
    }
    
    private class SetlistenCmd extends OwnerCommand
    {
        private SetlistenCmd()
        {
            this.name = "listen";
            this.aliases = new String[]{"listening"};
            this.help = "establece el juego que el bot est� escuchando";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("Por favor pon un titulo para escucharlo!");
                return;
            }
            String title = event.getArgs().toLowerCase().startsWith("to") ? event.getArgs().substring(2).trim() : event.getArgs();
            try
            {
                event.getJDA().getPresence().setGame(Game.listening(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** est� ahora escuchando `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" No se ha podido establecer el juego!");
            }
        }
    }
    
    private class SetwatchCmd extends OwnerCommand
    {
        private SetwatchCmd()
        {
            this.name = "watch";
            this.aliases = new String[]{"watching"};
            this.help = "establece qu� juego est� viendo";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("�Pero pon un juego!");
                return;
            }
            String title = event.getArgs();
            try
            {
                event.getJDA().getPresence().setGame(Game.watching(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** est� ahora viendo `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" No se ha podido establecer!");
            }
        }
    }
}
