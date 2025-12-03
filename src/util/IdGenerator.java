package util;

import java.util.concurrent.atomic.AtomicInteger;

// Utility sederhana untuk membuat ID unik secara sequential (Clean Code)
public class IdGenerator {
    private static AtomicInteger counter = new AtomicInteger(1000);

    public static String generateUniqueId(String prefix) {
        return prefix + counter.getAndIncrement();
    }
}