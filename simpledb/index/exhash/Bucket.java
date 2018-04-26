package simpledb.index.exhash;

/**
 * Created by Brady Snowden on 4/25/2018.
 */
public class Bucket {
    private int bits;
    private String filename;
    private int localdepth;

    public Bucket(int bits, String filename, int localdepth) {
        this.bits = bits;
        this.filename = filename;
        this.localdepth = localdepth;
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLocaldepth() {
        return localdepth;
    }

    public void setLocaldepth(int localdepth) {
        this.localdepth = localdepth;
    }
}
