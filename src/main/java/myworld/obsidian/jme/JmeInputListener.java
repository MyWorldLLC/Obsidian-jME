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

package myworld.obsidian.jme;

import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import myworld.obsidian.ObsidianUI;
import myworld.obsidian.events.BaseEvent;
import myworld.obsidian.input.Key;
import myworld.obsidian.input.MouseButton;
import myworld.obsidian.input.MouseWheelAxis;

import static com.jme3.input.KeyInput.*;

public class JmeInputListener implements RawInputListener {

    protected ObsidianUI ui;
    protected boolean consumeEvents;

    public JmeInputListener(){
        this(null);
    }

    public JmeInputListener(ObsidianUI ui){
        this.ui = ui;
    }

    public ObsidianUI getUI() {
        return ui;
    }

    public void setUI(ObsidianUI ui) {
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
        if(ui == null){
            return;
        }

        BaseEvent uiEvt;
        if(evt.getDeltaWheel() != 0){
            uiEvt = ui.getInput().fireMouseWheelEvent(MouseWheelAxis.VERTICAL, evt.getX(), invert(evt.getY()), -evt.getDeltaWheel());
        }else{
            uiEvt = ui.getInput().fireMouseMoveEvent(evt.getX(), invert(evt.getY()));
        }
        consumeEvent(evt, uiEvt);
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {
        if(ui == null){
            return;
        }

        MouseButton button = switch (evt.getButtonIndex()){
            case MouseInput.BUTTON_LEFT -> MouseButton.PRIMARY;
            case MouseInput.BUTTON_MIDDLE -> MouseButton.MIDDLE;
            case MouseInput.BUTTON_RIGHT -> MouseButton.SECONDARY;
            default -> MouseButton.NONE;
        };
        var uiEvt = ui.getInput().fireMouseButtonEvent(button, evt.isPressed(), evt.getX(), invert(evt.getY()));
        consumeEvent(evt, uiEvt);
    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {
        if(ui == null){
            return;
        }

        BaseEvent uiEvt;
        if(evt.getKeyChar() == 0){
            uiEvt = ui.getInput().fireCharacterEvent(new char[]{evt.getKeyChar()});
            consumeEvent(evt, uiEvt);
        }

        uiEvt = ui.getInput().fireKeyEvent(mapKey(evt.getKeyCode()), evt.isPressed());
        consumeEvent(evt, uiEvt);
    }

    @Override
    public void onTouchEvent(TouchEvent evt) {}

    protected void consumeEvent(InputEvent evt, BaseEvent uiEvt){
        if(consumeEvents && uiEvt.isConsumed()){
            evt.setConsumed();
        }
    }

    protected int invert(int y){
        return (int) ui.display().get().getDimensions().get().height() - y;
    }

    protected Key mapKey(int keyCode){
        return switch (keyCode) {
            case KEY_UNKNOWN -> Key.UNKNOWN;
            case KEY_SPACE -> Key.SPACE;
            case KEY_APOSTROPHE -> Key.APOSTROPHE;
            case KEY_COMMA -> Key.COMMA;
            case KEY_MINUS -> Key.MINUS;
            case KEY_PERIOD -> Key.PERIOD;
            case KEY_SLASH -> Key.SLASH;
            case KEY_0 -> Key.KEY_0;
            case KEY_1 -> Key.KEY_1;
            case KEY_2 -> Key.KEY_2;
            case KEY_3 -> Key.KEY_3;
            case KEY_4 -> Key.KEY_4;
            case KEY_5 -> Key.KEY_5;
            case KEY_6 -> Key.KEY_6;
            case KEY_7 -> Key.KEY_7;
            case KEY_8 -> Key.KEY_8;
            case KEY_9 -> Key.KEY_9;
            case KEY_SEMICOLON -> Key.SEMICOLON;
            case KEY_EQUALS -> Key.EQUAL;
            case KEY_A -> Key.KEY_A;
            case KEY_B -> Key.KEY_B;
            case KEY_C -> Key.KEY_C;
            case KEY_D -> Key.KEY_D;
            case KEY_E -> Key.KEY_E;
            case KEY_F -> Key.KEY_F;
            case KEY_G -> Key.KEY_G;
            case KEY_H -> Key.KEY_H;
            case KEY_I -> Key.KEY_I;
            case KEY_J -> Key.KEY_J;
            case KEY_K -> Key.KEY_K;
            case KEY_L -> Key.KEY_L;
            case KEY_M -> Key.KEY_M;
            case KEY_N -> Key.KEY_N;
            case KEY_O -> Key.KEY_O;
            case KEY_P -> Key.KEY_P;
            case KEY_Q -> Key.KEY_Q;
            case KEY_R -> Key.KEY_R;
            case KEY_S -> Key.KEY_S;
            case KEY_T -> Key.KEY_T;
            case KEY_U -> Key.KEY_U;
            case KEY_V -> Key.KEY_V;
            case KEY_W -> Key.KEY_W;
            case KEY_X -> Key.KEY_X;
            case KEY_Y -> Key.KEY_Y;
            case KEY_Z -> Key.KEY_Z;
            case KEY_LBRACKET -> Key.LEFT_BRACKET;
            case KEY_BACKSLASH -> Key.BACKSLASH;
            case KEY_RBRACKET -> Key.RIGHT_BRACKET;
            case KEY_GRAVE -> Key.GRAVE_ACCENT;
            case KEY_ESCAPE -> Key.ESCAPE;
            case KEY_RETURN -> Key.ENTER;
            case KEY_TAB -> Key.TAB;
            case KEY_BACK -> Key.BACKSPACE;
            case KEY_INSERT -> Key.INSERT;
            case KEY_DELETE -> Key.DELETE;
            case KEY_RIGHT -> Key.RIGHT;
            case KEY_LEFT -> Key.LEFT;
            case KEY_DOWN -> Key.DOWN;
            case KEY_UP -> Key.UP;
            case KEY_PGUP -> Key.PAGE_UP;
            case KEY_PGDN -> Key.PAGE_DOWN;
            case KEY_HOME -> Key.HOME;
            case KEY_END -> Key.END;
            case KEY_CAPITAL -> Key.CAPS_LOCK;
            case KEY_SCROLL -> Key.SCROLL_LOCK;
            case KEY_NUMLOCK -> Key.NUM_LOCK;
            case KEY_PRTSCR -> Key.PRINT_SCREEN;
            case KEY_PAUSE -> Key.PAUSE;
            case KEY_F1 -> Key.F1;
            case KEY_F2 -> Key.F2;
            case KEY_F3 -> Key.F3;
            case KEY_F4 -> Key.F4;
            case KEY_F5 -> Key.F5;
            case KEY_F6 -> Key.F6;
            case KEY_F7 -> Key.F7;
            case KEY_F8 -> Key.F8;
            case KEY_F9 -> Key.F9;
            case KEY_F10 -> Key.F10;
            case KEY_F11 -> Key.F11;
            case KEY_F12 -> Key.F12;
            case KEY_F13 -> Key.F13;
            case KEY_F14 -> Key.F14;
            case KEY_F15 -> Key.F15;
            case KEY_NUMPAD0 -> Key.KP_0;
            case KEY_NUMPAD1 -> Key.KP_1;
            case KEY_NUMPAD2 -> Key.KP_2;
            case KEY_NUMPAD3 -> Key.KP_3;
            case KEY_NUMPAD4 -> Key.KP_4;
            case KEY_NUMPAD5 -> Key.KP_5;
            case KEY_NUMPAD6 -> Key.KP_6;
            case KEY_NUMPAD7 -> Key.KP_7;
            case KEY_NUMPAD8 -> Key.KP_8;
            case KEY_NUMPAD9 -> Key.KP_9;
            case KEY_DECIMAL -> Key.KP_DECIMAL;
            case KEY_DIVIDE -> Key.KP_DIVIDE;
            case KEY_MULTIPLY -> Key.KP_MULTIPLY;
            case KEY_SUBTRACT -> Key.KP_SUBTRACT;
            case KEY_ADD -> Key.KP_ADD;
            case KEY_NUMPADENTER -> Key.KP_ENTER;
            case KEY_NUMPADEQUALS -> Key.KP_EQUAL;
            case KEY_LSHIFT -> Key.LEFT_SHIFT;
            case KEY_LCONTROL -> Key.LEFT_CONTROL;
            case KEY_LMENU -> Key.LEFT_ALT;
            case KEY_LMETA -> Key.LEFT_META;
            case KEY_RSHIFT -> Key.RIGHT_SHIFT;
            case KEY_RCONTROL -> Key.RIGHT_CONTROL;
            case KEY_RMENU -> Key.RIGHT_ALT;
            case KEY_RMETA -> Key.RIGHT_META;
            default -> Key.UNKNOWN;
        };
    }
}
