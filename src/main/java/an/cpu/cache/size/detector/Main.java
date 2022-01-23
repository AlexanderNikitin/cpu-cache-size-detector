package an.cpu.cache.size.detector;

public class Main {
    public static void main(String[] args) {
        CacheSizeDetector cacheSizeDetector = new CacheSizeDetector();
        cacheSizeDetector.detect();
        int level = 1;
        for (Integer result : cacheSizeDetector.getResults()) {
            System.out.println("Probably, the CPU has cache " + level++ + "th level size: " + result);
        }
    }
}
