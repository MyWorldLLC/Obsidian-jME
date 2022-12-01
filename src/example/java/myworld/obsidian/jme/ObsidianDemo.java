package myworld.obsidian.jme;


import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public class ObsidianDemo extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false);

        var obsidian = new ObsidianAppState();
        getStateManager().attach(obsidian);
        obsidian.setReadyListener((oldUi, ui) -> {
            System.out.println("UI is ready");
        });
    }

    public static void main(String[] args){
        var demo = new ObsidianDemo();

        var settings = new AppSettings(true);
        settings.setVSync(true);
        settings.setResizable(true);
        settings.setResolution(1200, 800);

        demo.setSettings(settings);

        demo.setShowSettings(false);

        demo.start();
    }
}
