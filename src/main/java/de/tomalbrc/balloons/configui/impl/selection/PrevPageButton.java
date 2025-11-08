package de.tomalbrc.balloons.configui.impl.selection;

import com.cobblemonislands.emotive.config.ConfiguredAnimation;
import de.tomalbrc.balloons.configui.api.ConfiguredGui;
import de.tomalbrc.balloons.configui.api.GuiElementData;
import de.tomalbrc.balloons.configui.api.GuiElementType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.world.item.Items;

public class PrevPageButton implements GuiElementType<GuiElementData, ConfiguredAnimation> {
    @Override
    public GuiElementBuilder build(ConfiguredGui<GuiElementData, ConfiguredAnimation> g, GuiElementData data) {
        if (g.getCurrentPage("contents") == 0) {
            return new GuiElementBuilder(Items.AIR.getDefaultInstance());
        }

        return data.decorate(new GuiElementBuilder(data.item().copy())).setCallback(() -> g.previousPage(data.type()));
    }
}
