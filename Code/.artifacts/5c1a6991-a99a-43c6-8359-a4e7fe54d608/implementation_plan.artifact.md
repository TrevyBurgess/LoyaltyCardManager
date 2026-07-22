# Implementation Plan - Supporting targetSdk 37 (Android 17)

This plan focuses on adapting the app to fully support Android 17 (API 37) and finalizing the Edge-to-Edge migration started previously.

## User Review Required

> [!IMPORTANT]
> **Android 17 (API 37) Adaptations:**
> 1. **Edge-to-Edge & Insets:** I will replace the remaining hardcoded paddings (e.g., `60.dp` for the status bar) with dynamic `WindowInsets`. This is essential as Android 16+ enforces edge-to-edge and large screen resizability.
> 2. **Orientation & Resizability:** Android 17 removes the opt-out for ignoring orientation restrictions on large screens. The app already uses adaptive layouts (`LazyVerticalGrid`), so it should be well-positioned for this.
> 3. **Loopback Protections:** If the app ever communicates with other apps over `localhost`, it will need the `USE_LOOPBACK_INTERFACE` permission. Currently, no such communication is detected.

## Proposed Changes

### UI & Layout (Edge-to-Edge)

#### [MODIFY] [MainHostScreen.kt](file:///D:/Dev/GitHub/LoyaltyCardManager/Code/composeApp/src/androidMain/kotlin/com/cyberfeedforward/loyaltycardmanager/ui/MainHostScreen.kt)
- Replace `padding(top = 60.dp)` on the header with `WindowInsets.statusBars.asPaddingValues()` or similar.
- Use `Modifier.safeDrawingPadding()` or `Modifier.windowInsetsPadding()` to ensure the title and navigation are always correctly positioned.

#### [MODIFY] [CardsScreen.kt](file:///D:/Dev/GitHub/LoyaltyCardManager/Code/composeApp/src/androidMain/kotlin/com/cyberfeedforward/loyaltycardmanager/ui/cards/CardsScreen.kt)
- Clean up magic padding numbers (like `padding(top = 30.dp)`) that might conflict with system UI in certain window configurations.

### Build Configuration

#### [MODIFY] [app/build.gradle.kts](file:///D:/Dev/GitHub/LoyaltyCardManager/Code/app/build.gradle.kts)
- (Verified) User already set `targetSdk = 37`. I will ensure all other SDK-related settings are consistent.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to verify compilation with API 37.

### Manual Verification
- Deploy to an Android 17 (Preview) or 16 emulator.
- **Insets Check:** Verify the header title doesn't overlap with the status bar or notch in both portrait and landscape (on tablets).
- **Adaptive Check:** Verify the app handles window resizing correctly on a foldable or tablet emulator.
