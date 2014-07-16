package fi.helsinki.cs.tmc.snapshot.api.model.view;

public class View {

    public static class IdOnly { }
    public static class Summary extends IdOnly { }
    public static class Complete extends Summary { }

}
