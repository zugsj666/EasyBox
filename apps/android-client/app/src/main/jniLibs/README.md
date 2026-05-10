# sing-box Binary Placeholder

Place Android sing-box executables in ABI-specific folders and rename them to `libsing-box.so`.

Examples:

```text
app/src/main/jniLibs/arm64-v8a/libsing-box.so
app/src/main/jniLibs/armeabi-v7a/libsing-box.so
app/src/main/jniLibs/x86_64/libsing-box.so
```

The Phase 1 demo intentionally does not commit a sing-box binary.

