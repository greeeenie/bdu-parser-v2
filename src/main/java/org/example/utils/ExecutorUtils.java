package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

import java.util.concurrent.Callable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutorUtils {

    public static Callable<Void> nullCallable(Logger log, Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        };
    }
}
