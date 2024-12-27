package org.example.mapagrafos.util;

import javafx.scene.image.Image;

public class GifEquipamiento {
    private Image image;

    public GifEquipamiento() {
        image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifEquipamiento.gif"));
    }

    public Image getImage() {
        return image;
    }
}
