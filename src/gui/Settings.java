package gui;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
public class Settings {
    double uiScaling;
    double topScaling;
    Theme theme;
    String font;

    public Settings(double uiScaling, double topScaling, Theme theme, String font) {
        this.uiScaling = uiScaling;
        this.topScaling = topScaling;
        this.theme = theme;
        this.font = font;
    }

    public double getUiScaling() {
        return uiScaling;
    }

    public void setUiScaling(double uiScaling) {
        this.uiScaling = uiScaling;
    }

    public double getTopScaling() {
        return topScaling;
    }

    public void setTopScaling(double topScaling) {
        this.topScaling = topScaling;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }
}
