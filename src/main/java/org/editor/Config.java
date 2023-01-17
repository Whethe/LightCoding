package org.editor;

import com.formdev.flatlaf.*;

public class Config {

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWindowTheme() {
        return WindowTheme;
    }

    public void setWindowTheme(String windowTheme) {
        this.WindowTheme = windowTheme;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getTextAreaTheme() {
        return TextAreaTheme;
    }

    public void setTextAreaTheme(String textAreaTheme) {
        this.TextAreaTheme = textAreaTheme;
    }

    //----- Window Settings ----------------------------------------------
    private int width;
    private int  height;
    private String color;
    private String WindowTheme;

    //---- Body Settings ----------------------------------------------
    private int fontSize;
    private String fontStyle;
    private String TextAreaTheme;
}
