package myworld.obsidian.jme;


import com.jme3.app.SimpleApplication;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import myworld.obsidian.components.Button;
import myworld.obsidian.components.Checkbox;
import myworld.obsidian.components.text.TextDisplay;
import myworld.obsidian.components.text.TextField;
import myworld.obsidian.display.skin.obsidian.ObsidianSkin;
import myworld.obsidian.events.input.CharacterEvent;
import myworld.obsidian.events.input.KeyEvent;
import myworld.obsidian.events.input.MouseOverEvent;
import myworld.obsidian.events.scene.ButtonEvent;
import myworld.obsidian.geometry.Distance;
import myworld.obsidian.layout.Offsets;
import myworld.obsidian.scene.Component;
import myworld.obsidian.text.Text;

public class ObsidianDemo extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false);

        var cube = new Box(1f, 1f, 1f);
        var geom = new Geometry("Box");
        geom.setMesh(cube);

        var material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(material);
        rootNode.attachChild(geom);
        cam.setLocation(new Vector3f(0, 0, -10f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        var obsidian = new ObsidianAppState();
        getStateManager().attach(obsidian);
        obsidian.getInputListener().setConsumeEvents(true);

        obsidian.setReadyListener((ui) -> {

            var layout = new ExampleLayout();
            ui.getRoot().addChild(layout);

            ui.getRoot().dispatcher().subscribe(MouseOverEvent.class, evt -> evt.isHovering(ui.getRoot()), evt -> {
                System.out.println("(%d, %d)".formatted(evt.getX(), evt.getY()));
            });

            var textField = TextField.password('*');
            textField.layout().preferredSize(Distance.pixels(100), Distance.pixels(50));
            textField.insert("Foo");
            layout.center().addChild(textField);

            var label = new TextDisplay();
            label.layout().preferredSize(Distance.pixels(100), Distance.pixels(100));
            label.text().set("Hello, World!");
            layout.left().addChild(label);

            var button = Button.textButton(Text.styled("Hello, Buttons!", ui.getStyle("ExampleText")));
            button.layout().margin().set(new Offsets(Distance.pixels(15)));
            button.addButtonListener(ButtonEvent::isPressed, evt -> System.out.println("Pressed!"));
            button.addButtonListener(ButtonEvent::isClicked, evt -> System.out.println("Clicked!"));
            button.addButtonListener(ButtonEvent::isReleased, evt -> System.out.println("Released!"));
            layout.right().addChild(button);

            var checkbox = new Checkbox();
            checkbox.addCheckListener(evt -> System.out.println("Checked: " + evt.isChecked()));
            layout.right().addChild(checkbox);

            ui.requestFocus(textField);

            enqueue(() -> {
                inputManager.addRawInputListener(new RawInputListener() {
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
                        System.out.println(evt);
                    }

                    @Override
                    public void onMouseButtonEvent(MouseButtonEvent evt) {
                        System.out.println(evt);
                    }

                    @Override
                    public void onKeyEvent(KeyInputEvent evt) {
                        System.out.println(evt);
                    }

                    @Override
                    public void onTouchEvent(TouchEvent evt) {}
                });
            });
        });
    }

    public static void main(String[] args){
        var demo = new ObsidianDemo();

        var settings = new AppSettings(true);
        settings.setVSync(true);
        settings.setResizable(true);
        settings.setResolution(1200, 800);
        settings.setRenderer(AppSettings.LWJGL_OPENGL33);

        demo.setSettings(settings);

        demo.setShowSettings(false);

        demo.start();
    }
}
