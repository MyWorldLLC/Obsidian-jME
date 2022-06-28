package myworld.obsidian.jme;

import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.input.event.*;
import myworld.obsidian.InputHandler;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.input.MouseButton;
import myworld.obsidian.input.MouseWheelAxis;

public class JmeInputListener implements RawInputListener {

    protected final ObsidianUI ui;
    protected final InputHandler handler;
    protected boolean consumeEvents;

    public JmeInputListener(ObsidianUI ui, InputHandler handler){
        this.ui = ui;
        this.handler = handler;
    }

    public InputHandler getHandler(){
        return handler;
    }

    public void setConsumeEvents(boolean consume){
        consumeEvents = consume;
    }

    public boolean isConsumingEvents(){
        return consumeEvents;
    }

    @Override
    public void beginInput() {}

    @Override
    public void endInput() {}

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {}

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {}

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        if(evt.getDeltaWheel() != 0){
            handler.mouseWheelEvent(MouseWheelAxis.VERTICAL, -evt.getDeltaWheel(), evt.getWheel(), evt.getX(), invert(evt.getY()));
        }else{
            handler.mouseMoveEvent(evt.getX(), invert(evt.getY()));
        }
        consumeEvent(evt);
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        MouseButton button = switch (evt.getButtonIndex()){
            case MouseInput.BUTTON_LEFT -> MouseButton.PRIMARY;
            case MouseInput.BUTTON_MIDDLE -> MouseButton.MIDDLE;
            case MouseInput.BUTTON_RIGHT -> MouseButton.SECONDARY;
            default -> MouseButton.NONE;
        };
        handler.mouseButtonEvent(button, evt.isPressed(), evt.getX(), invert(evt.getY()));
        consumeEvent(evt);
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {
        int keyCode = 0;
        if(evt.getKeyCode() != 0){
            keyCode = AwtKeyInput.convertJmeCode(evt.getKeyCode());
        }
        handler.keyEvent(keyCode, evt.getKeyChar(), evt.isPressed(), evt.isRepeating());
        consumeEvent(evt);
    }

    @Override
    public void onTouchEvent(TouchEvent evt) {}

    protected void consumeEvent(InputEvent evt){
        if(consumeEvents){
            evt.setConsumed();
        }
    }

    protected int invert(int y){
        return ui.getWindowInfo().getHeight() - y;
    }
}
