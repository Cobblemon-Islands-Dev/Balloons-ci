package de.tomalbrc.balloons.config;

import de.tomalbrc.balloons.component.BalloonProperties;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ConfiguredBalloon {
    public static Style EMPTY = Style.EMPTY.withColor(ChatFormatting.WHITE).withUnderlined(false).withItalic(false).withObfuscated(false).withStrikethrough(false);

    ResourceLocation id;
    @Nullable ItemStack item;
    BalloonProperties data;
    Component title;
    boolean glint;
    String permission;
    int permissionLevel;

    public ConfiguredBalloon(ResourceLocation id, Component title, @Nullable ItemStack item, BalloonProperties data) {
        this.id = id;
        this.item = item;
        this.data = data;
        this.title = title;
    }

    public void setItem(@Nullable ItemStack item) {
        this.item = item;
    }

    public BalloonProperties data() {
        return data;
    }

    public ItemStack item() {
        return item;
    }

    public ResourceLocation id() {
        return id;
    }

    public Component title() {
        return title;
    }

    public @Nullable String permission() {
        return permission;
    }

    public int permissionLevel() {
        return permissionLevel;
    }

    public GuiElementBuilder guiElementBuilder() {
        var builder = GuiElementBuilder.from(item == null ? Items.PAPER.getDefaultInstance() : item);
        builder.addLoreLine(Component.empty());
        builder.glow(glint);
        return builder;
    }
}
