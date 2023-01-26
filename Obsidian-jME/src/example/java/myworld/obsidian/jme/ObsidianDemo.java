package myworld.obsidian.jme;


import com.jme3.app.SimpleApplication;
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

        var obsidian = new ObsidianAppState();
        getStateManager().attach(obsidian);
        obsidian.setReadyListener((ui) -> {

            var layout = new ExampleLayout();
            ui.getRoot().addChild(layout);

            ui.getRoot().dispatcher().subscribe(MouseOverEvent.class, evt -> evt.isHovering(ui.getRoot()), evt -> {
                System.out.println("(%d, %d)".formatted(evt.getX(), evt.getY()));
            });

            var example = new Component();
            example.styleName().set("Example");
            example.layout().preferredSize(Distance.pixels(100), Distance.pixels(50));
            layout.left().addChild(example);

            example.renderVars().put("text", () -> new Text("Hello, World!", ui.getStyle("ExampleText")));

            example.dispatcher().subscribe(MouseOverEvent.class, evt -> evt.entered(example), evt -> {
                System.out.println("Mouse entered");
            });

            example.dispatcher().subscribe(MouseOverEvent.class, evt -> evt.exited(example), evt -> {
                System.out.println("Mouse exited");
            });

            example.dispatcher().subscribe(MouseOverEvent.class, evt -> evt.isHovering(example), evt -> {
                System.out.println("(%d, %d)".formatted(evt.getX(), evt.getY()));
            });

            example.dispatcher().subscribe(CharacterEvent.class, evt -> {
                System.out.println("Received characters: " + String.copyValueOf(evt.getCharacters()));
            });

            example.dispatcher().subscribe(KeyEvent.class, KeyEvent::isDown, evt -> {
                System.out.println("Key: " + evt.getKey());
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
