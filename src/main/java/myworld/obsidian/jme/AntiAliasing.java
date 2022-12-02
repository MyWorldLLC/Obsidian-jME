package myworld.obsidian.jme;

public enum AntiAliasing {

    MINIMAL(2), STANDARD(4), FULL(8);

    private final int samples;

    AntiAliasing(int samples){
        this.samples = samples;
    }

    public int getSamples(){
        return samples;
    }

}
