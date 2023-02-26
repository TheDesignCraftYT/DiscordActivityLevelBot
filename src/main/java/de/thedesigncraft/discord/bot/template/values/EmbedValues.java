package de.thedesigncraft.discord.bot.template.values;

import de.thedesigncraft.discord.bot.template.methods.manage.Main;
import org.jetbrains.annotations.NotNull;

public interface EmbedValues {

    int standardColor = 0xffffff;

    @NotNull
    String embedFooterPictureLink = Main.jda.retrieveUserById(1010513824291504228L).complete().getEffectiveAvatarUrl();

    @NotNull
    String embedFooterText = "Powered by DOS Bots";

    int issueColor = 0xff5555;

}
