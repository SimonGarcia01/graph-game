package org.example.mapagrafos.util;

import javafx.scene.image.Image;

public class GifSacrifico {
    private Image image;
    public GifSacrifico() {
        image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifSacrificio.gif"));
    }

    public Image getImage() {
        return image;
    }
}
