package si.banani.si.banani.screens.fades;

/**
 * Created by Urban on 26.10.2016.
 */

public class Effect {
    private float time, start, finish;
    public Effect(float time, float start, float finish){

        this.time = time;
        this.start = start;
        this.finish = finish;
    }

    public float getStart(){ return this.start; }
    public float getFinish(){ return  this.finish; }
    public float getTime () { return  this.time; }
}
