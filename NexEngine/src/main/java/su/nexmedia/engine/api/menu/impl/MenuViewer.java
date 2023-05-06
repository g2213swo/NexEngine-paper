package su.nexmedia.engine.api.menu.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuViewer {

    private final Player player;

    private int page;
    private int pages;

    public MenuViewer(@NotNull Player player) {
        this.player = player;
        this.setPage(1);
        this.setPages(1);
    }

    public @NotNull Player getPlayer() {
        return this.player;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    public void finePage() {
        this.page = Math.max(1, Math.min(this.getPage(), this.getPages()));
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = Math.max(1, pages);
    }
}
