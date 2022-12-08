/*
 *    Copyright 2022 MyWorld, LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package myworld.obsidian.jfx.jme;

import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.awt.AwtKeyInput;
import com.jme3.input.event.*;
import myworld.obsidian.jfx.ObsidianJfxUI;
import myworld.obsidian.input.MouseButton;
import myworld.obsidian.input.MouseWheelAxis;

public class JmeJfxInputListener implements RawInputListener {

    protected final ObsidianJfxUI ui;
    protected boolean consumeEvents;

    public JmeJfxInputListener(ObsidianJfxUI ui){
        this.ui = ui;
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
            ui.getInputHandler().mouseWheelEvent(MouseWheelAxis.VERTICAL, -evt.getDeltaWheel(), evt.getWheel(), evt.getX(), invert(evt.getY()));
        }else{
            ui.getInputHandler().mouseMoveEvent(evt.getX(), invert(evt.getY()));
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
        ui.getInputHandler().mouseButtonEvent(button, evt.isPressed(), evt.getX(), invert(evt.getY()));
        consumeEvent(evt);
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {
        int keyCode = 0;
        if(evt.getKeyCode() != 0){
            keyCode = AwtKeyInput.convertJmeCode(evt.getKeyCode());
        }
        ui.getInputHandler().keyEvent(keyCode, evt.getKeyChar(), evt.isPressed(), evt.isRepeating());
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
