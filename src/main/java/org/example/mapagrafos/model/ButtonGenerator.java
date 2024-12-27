package org.example.mapagrafos.model;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.structures.IGraph;

import java.util.ArrayList;
import java.util.List;

public class ButtonGenerator {
    // Contenedor de VBox para colocar los botones
    private VBox buttonContainer;
    private List<Integer> listOfColumnsOfButtons;

    public ButtonGenerator() {
        this.listOfColumnsOfButtons = new ArrayList<>();
        buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.BOTTOM_CENTER); // Alinea los botones en el centro de la parte inferior
        buttonContainer.setSpacing(10); // Espaciado entre botones
    }

    public List<Button> generateButtonsForRow(int row, MapLogic mapLogic, IGraph graphBFS, List<Events> eventsList) {
        this.listOfColumnsOfButtons.clear();
        List<Button> buttons = new ArrayList<>();

        // Iterar sobre los eventos para la fila dada
        for (Events event : eventsList) {
            if (event.getRow() == row) {
                // Crear un botón basado en el tipo de evento
                Button button = new Button("Evento: " + event.getType() + " en (" + event.getRow() + "," + event.getCol() + ")");
                button.setOnAction(e -> {
                    // Manejar el evento al hacer clic en el botón
                    try {
                        handleButtonClick(event.getRow(), event.getCol(), graphBFS);
                    } catch (GraphException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                buttons.add(button);
                listOfColumnsOfButtons.add(event.getCol());
            }
        }

        // Agregar los botones al contenedor de VBox
        buttonContainer.getChildren().clear(); // Limpiar cualquier botón previo
        buttonContainer.getChildren().addAll(buttons);

        return buttons;
    }

    public void handleButtonClick(int row, int col, IGraph graphBFS) throws GraphException {

        if (graphBFS.hasNeighborInNextRow(row, col)) {
            // El jugador puede avanzar
            System.out.println("Valid path: Moving forward (" + row + "," + col + ")");

        } else {
            // El camino no es válido
            System.out.println("You can't go any further down this path.");
        }
    }

    public List<Integer> getListOfColumnsOfButtons() {
        return listOfColumnsOfButtons;
    }

    // Método para obtener el VBox con los botones
    public VBox getButtonContainer() {
        return buttonContainer;
    }
}