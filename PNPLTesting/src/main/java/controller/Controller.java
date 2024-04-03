package controller;

public abstract class Controller {
    private static Controller _ctrl;

    public static Controller getInstance() {
        if (_ctrl == null) _ctrl = new ControllerImp();
        return _ctrl;
    }

    public abstract void execute(String[] paths);
}
