package com.abidux.craftus.enums;


public enum DisplayMessage {

    PREFIX(""),
    YOU_ARE_NOT_ALLOWED_TO_DO_THIS(""),
    CREATE(""),
    CLICK_TO_CREATE(""),
    SEARCH(""),
    CLICK_TO_SEARCH(""),
    SEND_NAME(""),
    MAP_CREATED(""),
    CLICK_TO_EDIT(""),
    PREVIOUS(""),
    NEXT(""),
    EDIT_MAP(""),
    MODIFIED(""),
    JOINED(""),
    LEFT(""),
    IMPOSTOR_TITLE(""),
    IMPOSTOR_SUBTITLE(""),
    CREWMATE_TITLE(""),
    CREWMATE_SUBTITLE("");

    private String content;

    DisplayMessage(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return this.content;
    }

}