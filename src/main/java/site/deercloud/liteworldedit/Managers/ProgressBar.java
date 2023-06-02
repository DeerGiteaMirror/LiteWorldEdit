package site.deercloud.liteworldedit.Managers;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

public class ProgressBar implements BossBar {

    /**
     * Returns the title of this boss bar
     *
     * @return the title of the bar
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * Sets the title of this boss bar
     *
     * @param title the title of the bar
     */
    @Override
    public void setTitle(String title) {

    }

    /**
     * Returns the color of this boss bar
     *
     * @return the color of the bar
     */
    @Override
    public BarColor getColor() {
        return null;
    }

    /**
     * Sets the color of this boss bar.
     *
     * @param color the color of the bar
     */
    @Override
    public void setColor(BarColor color) {

    }

    /**
     * Returns the style of this boss bar
     *
     * @return the style of the bar
     */
    @Override
    public BarStyle getStyle() {
        return null;
    }

    /**
     * Sets the bar style of this boss bar
     *
     * @param style the style of the bar
     */
    @Override
    public void setStyle(BarStyle style) {

    }

    /**
     * Remove an existing flag on this boss bar
     *
     * @param flag the existing flag to remove
     */
    @Override
    public void removeFlag(BarFlag flag) {

    }

    /**
     * Add an optional flag to this boss bar
     *
     * @param flag an optional flag to set on the boss bar
     */
    @Override
    public void addFlag(BarFlag flag) {

    }

    /**
     * Returns whether this boss bar as the passed flag set
     *
     * @param flag the flag to check
     * @return whether it has the flag
     */
    @Override
    public boolean hasFlag(BarFlag flag) {
        return false;
    }

    /**
     * Sets the progress of the bar. Values should be between 0.0 (empty) and
     * 1.0 (full)
     *
     * @param progress the progress of the bar
     */
    @Override
    public void setProgress(double progress) {

    }

    /**
     * Returns the progress of the bar between 0.0 and 1.0
     *
     * @return the progress of the bar
     */
    @Override
    public double getProgress() {
        return 0;
    }

    /**
     * Adds the player to this boss bar causing it to display on their screen.
     *
     * @param player the player to add
     */
    @Override
    public void addPlayer(Player player) {

    }

    /**
     * Removes the player from this boss bar causing it to be removed from their
     * screen.
     *
     * @param player the player to remove
     */
    @Override
    public void removePlayer(Player player) {

    }

    /**
     * Removes all players from this boss bar
     *
     * @see #removePlayer(Player)
     */
    @Override
    public void removeAll() {

    }

    /**
     * Returns all players viewing this boss bar
     *
     * @return a immutable list of players
     */
    @Override
    public List<Player> getPlayers() {
        return null;
    }

    /**
     * Set if the boss bar is displayed to attached players.
     *
     * @param visible visible status
     */
    @Override
    public void setVisible(boolean visible) {

    }

    /**
     * Return if the boss bar is displayed to attached players.
     *
     * @return visible status
     */
    @Override
    public boolean isVisible() {
        return false;
    }

    /**
     * Shows the previously hidden boss bar to all attached players
     *
     * @deprecated {@link #setVisible(boolean)}
     */
    @Override
    public void show() {

    }

    /**
     * Hides this boss bar from all attached players
     *
     * @deprecated {@link #setVisible(boolean)}
     */
    @Override
    public void hide() {

    }
}
