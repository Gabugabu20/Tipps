package model;

public enum Message {
    REMOVEEDGE("Diese Verbindung gehört nicht zur Lösung. Bitte entfernen!"),
    ADDEDGE("Diese Verbindung gehört zur Lösung! Bitte Auswählen!"),
    KREIS("Es hat einen Kreis! Bitte korrigieren!");

    private String message;

    private Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
