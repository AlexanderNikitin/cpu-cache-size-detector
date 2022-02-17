package an.cpu.cache.size.detector;

import java.util.*;

public class CacheSizeDetector {

    private static final int INITIAL_DATA_SIZE = 1024;
    private static final int INITIAL_DELTA = INITIAL_DATA_SIZE / 2;
    private static final int MAX_DATA_SIZE = 1024 * 1024 * 128;

    private static final int SINGLE_TEST_FACTOR = 20;
    private static final int DELTA_CALC_FACTOR = 2;

    private static final long LESS_TIME_FACTOR = 4;
    private static final long LAGER_TIME_FACTOR = 3;

    private int currentDataSize;
    private int currentDelta;
    private Random rnd;

    private Map<Long, Integer> avgTime;
    private List<Integer> caches;

    public void detect() {
        init();
        while (hasNextIterations()) {
            calcTime();
            nextIteration();
        }
        prepareResult();
        cleanup();
    }

    private long[] collectionToArray(Collection<Long> collection) {
        int size = collection.size();
        int i = 0;
        long[] result = new long[size];
        for (Long aLong : collection) {
            result[i++] = aLong;
        }
        return result;
    }

    private long[] handleResult(long[] times) {
        long min = Long.MAX_VALUE;
        long[] times2 = new long[times.length];
        for (int i = times.length - 1; i >= 0; i--) {
            long t = times[i];
            if (t < min) {
                min = t;
            }
            times2[i] = min;
        }
        return times2;
    }

    private void prepareResult() {
        long[] times = handleResult(collectionToArray(avgTime.keySet()));
        for (int i = 1; i < times.length; i++) {
            long t = times[i];
            long t0 = times[i - 1];
            if (LESS_TIME_FACTOR * t0 < LAGER_TIME_FACTOR * t) {
                caches.add(avgTime.get(times[i]));
            }
        }
    }

    private void cleanup() {
        avgTime.clear();
        avgTime = null;
        System.gc();
    }

    private void calcTime() {
        int[] a = new int[currentDataSize / 4];
        for (int i = 0; i < a.length; i++) {
            a[i] = rnd.nextInt();
        }

        long t0 = System.nanoTime();
        test(a);
        long t = System.nanoTime();
        long avg = (t - t0) / a.length;
        avgTime.put(avg, currentDataSize);
    }

    private void test(int[] a) {
        for (int i = 0; i < a.length * SINGLE_TEST_FACTOR; i++) {
            a[rnd.nextInt(a.length)] = rnd.nextInt() + a[rnd.nextInt(a.length)] - a[rnd.nextInt(a.length)];
        }
    }

    private void init() {
        currentDataSize = INITIAL_DATA_SIZE;
        currentDelta = INITIAL_DELTA;
        rnd = new Random();
        avgTime = new LinkedHashMap<>(20);
        caches = new ArrayList<>(5);
    }

    private boolean hasNextIterations() {
        return currentDataSize <= MAX_DATA_SIZE;
    }

    private void nextIteration() {
        currentDataSize += currentDelta;
        if (currentDataSize % 3 > 0) {
            currentDelta = currentDataSize / DELTA_CALC_FACTOR;
        }
    }

    public List<Integer> getResults() {
        return new ArrayList<>(caches);
    }
}
