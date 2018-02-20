/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package substats;

/**
 *
 * @author Keith
 */
public class FrequencyBand {
    private final int centerFreq;
    private final int bandwidth;
    private final int intensity;

    public FrequencyBand(int centerFreq, int bandwidth, int intensity) {
        this.centerFreq = centerFreq;
        this.bandwidth = bandwidth;
        this.intensity = intensity;
    }
    
    public int getCenterFreq() {
        return centerFreq;
    }
    
    public int getBandwidth() {
        return bandwidth;
    }
    
    public int getStartFreq() {
        return centerFreq - bandwidth / 2;
    }
    
    public int getEndFreq() {
        return centerFreq + bandwidth / 2;
    }

    public int getIntensity() {
        return intensity;
    }

    @Override
    public String toString() {
        return "FrequencyBand{" + "centerFreq=" + centerFreq + ", bandwidth=" + bandwidth + ", intensity=" + intensity + '}';
    }
    
    
}
