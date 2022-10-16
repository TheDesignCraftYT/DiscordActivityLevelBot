package de.thedesigncraft.discord.bot.template.values;

import de.thedesigncraft.discord.bot.template.methods.manage.MainTemplate;
import org.jetbrains.annotations.NotNull;

public interface EmbedValues {

    int standardColor = 0xffffff;

    @NotNull
    String embedFooterPictureLink = MainTemplate.jda.retrieveUserById(1010513824291504228L).complete().getEffectiveAvatarUrl();

    @NotNull
    String embedFooterText = "Powered by DOS Bots";

    int issueColor = 0xff5555;

}
