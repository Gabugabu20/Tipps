package model;
public enum Section {
    OBEN, UNTEN, LINKS, RECHTS;

    public static Section getSectionByName(String section) {
        switch (section) {
            case "LINKS":
                return Section.LINKS;
            case "RECHTS":
                return Section.RECHTS;
            case "OBEN":
                return Section.OBEN;
            case "UNTEN":
                return Section.UNTEN;
            default:
                return null;
        }
    }
}
