package org.example.mapagrafos.util;

import javafx.scene.image.Image;

public class GifCampamento {
    private Image image;

    public GifCampamento() {
        //image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifCampamento.gif"));
        image = new Image(getClass().getResourceAsStream("/iconos/gifScenes/gifCampamento-1.gif"));
    }

    public Image getImage() {
        return image;
    }
}
