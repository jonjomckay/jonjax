package com.jonjax.providers;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import javax.inject.Provider;

public class ScanResultProvider implements Provider<ScanResult> {
    public ScanResult get() {
        return new FastClasspathScanner("com.jonjax")
                .enableMethodAnnotationIndexing()
                .scan();
    }
}
