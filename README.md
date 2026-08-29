# Back Gesture Hook

An LSPosed module rebased from [MiuiBackGestureHook](https://github.com/wxxsfxyzm/MiuiBackGestureHook)
and adapted for Meizu FlymeOS.

On Flyme, the module restores AOSP predictive-back animations without replacing
the OEM `EdgeBackView` gesture style. Flyme keeps `registerSystemGestureListener`
and the stock indicator; Shell receives a real `BackAnimationAdapter` and
non-callback targets are dispatched to the existing remote runners.

This fork does not package the HyperOS `hyos_spawner` native entry. Upstream
native sources remain in `miui-home-hyos-native/` for reference.

## Compatibility

| Platform | Required components | Gesture style | Predictive back |
| --- | --- | --- | --- |
| FlymeOS | LSPosed | OEM `EdgeBackView` | Shell adapter + animator dispatch |
| Android 16 HyperOS | LSPosed | Upstream SystemUI path | Upstream |

## Build

Build the debug APK unless a release artifact is requested:

```powershell
.\gradlew.bat assembleDebug
```

Outputs:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Scope and runtime

The static LSPosed scope remains:

```text
com.android.systemui
com.miui.home
com.google.android.googlequicksearchbox
system
```

Flyme does not load `com.miui.home`. Do not add `com.meizu.flyme.launcher`.

API 102 hot reload is enabled with `autoHotReload=true`.

## References

Checked-in AOSP references are under `refs/android16/aosp_back_16/`. Upstream
native sources remain under `miui-home-hyos-native/` and are not packaged.

## License

Apache License 2.0. See [LICENSE](LICENSE).

Bundled third-party components and their separate license terms are listed in
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
