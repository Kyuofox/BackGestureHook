// SPDX-License-Identifier: Apache-2.0
package dev.codex.backgesturehook

import android.content.Intent

enum class NativeHookStatusKind {
    Checking,
    WaitingForNative,
    Ready,
    SystemUiNotReady,
    NativeNotReady,
    LegacyNotReady,
    NativeNoResponse,
    ProfileRejected,
    NoResponse,
    LsPosedUnavailable,
}

internal fun classifyNativeHookStatus(
    nativeResponse: Boolean,
    systemUiReady: Boolean,
    legacyMode: Boolean,
    legacyReady: Boolean,
    profileResolved: Boolean,
    nativeReady: Boolean,
    businessState: Int,
    bridgeState: Int,
    profileStage: Int,
    dartResolverStage: Int,
    drawerStateReady: Boolean,
    overviewStateReady: Boolean,
    editingStateReady: Boolean,
): NativeHookStatusKind = when {
    !nativeResponse && systemUiReady -> NativeHookStatusKind.WaitingForNative
    !systemUiReady -> NativeHookStatusKind.SystemUiNotReady
    !nativeResponse -> NativeHookStatusKind.WaitingForNative
    legacyMode && !legacyReady -> NativeHookStatusKind.LegacyNotReady
    legacyMode -> NativeHookStatusKind.Ready
    profileStage in 101..105 -> NativeHookStatusKind.ProfileRejected
    dartResolverStage in 101..105 -> NativeHookStatusKind.ProfileRejected
    !nativeReady || !profileResolved || businessState != 3 || bridgeState != 3 ||
        !drawerStateReady || !overviewStateReady || !editingStateReady ->
        NativeHookStatusKind.NativeNotReady
    else -> NativeHookStatusKind.Ready
}

data class NativeHookStatusUiState(
    val kind: NativeHookStatusKind,
    val profileDynamic: Boolean = false,
    val legacyMode: Boolean = false,
    val profileResolved: Boolean = false,
    val nativeReady: Boolean = false,
    val systemUiReady: Boolean = false,
    val businessState: Int = 0,
    val bridgeState: Int = 0,
    val profileStage: Int = 0,
    val dartResolverStage: Int = 0,
    val dartDrawerCandidates: Int = 0,
    val dartTransitionCandidates: Int = 0,
    val dartOverviewEnterCandidates: Int = 0,
    val dartOverviewExitCandidates: Int = 0,
    val dartEditingCandidates: Int = 0,
    val drawerStateReady: Boolean = false,
    val overviewStateReady: Boolean = false,
    val editingStateReady: Boolean = false,
    val reason: String = "",
) {
    val dartRuntimeResolved: Boolean
        get() = dartResolverStage == 5 &&
            dartDrawerCandidates == 1 &&
            dartTransitionCandidates == 1 &&
            dartOverviewEnterCandidates == 1 &&
            dartOverviewExitCandidates == 1 &&
            dartEditingCandidates == 1 &&
            drawerStateReady &&
            overviewStateReady &&
            editingStateReady

    companion object {
        fun checking(legacyMode: Boolean = false) = NativeHookStatusUiState(
            kind = NativeHookStatusKind.Checking,
            legacyMode = legacyMode,
        )

        fun noResponse() = NativeHookStatusUiState(NativeHookStatusKind.NoResponse)

        fun fromReply(intent: Intent): NativeHookStatusUiState {
            val nativeResponse = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_RESPONSE,
                false,
            )
            val systemUiReady = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_SYSTEMUI_READY,
                false,
            )
            val legacyMode = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_LEGACY_MODE,
                false,
            )
            val legacyReady = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_LEGACY_READY,
                false,
            )
            val profileResolved = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_PROFILE_RESOLVED,
                false,
            )
            val nativeReady = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_READY,
                false,
            )
            val profileDynamic = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_PROFILE_DYNAMIC,
                false,
            )
            val businessState = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_BUSINESS_STATE,
                0,
            )
            val bridgeState = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_BRIDGE_STATE,
                0,
            )
            val profileStage = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_RUNTIME_PROFILE_STAGE,
                0,
            )
            val dartResolverStage = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DART_RESOLVER_STAGE,
                0,
            )
            val dartDrawerCandidates = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DART_DRAWER_CANDIDATES,
                0,
            )
            val dartTransitionCandidates = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DART_TRANSITION_CANDIDATES,
                0,
            )
            val dartOverviewEnterCandidates = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DART_OVERVIEW_ENTER_CANDIDATES,
                0,
            )
            val dartOverviewExitCandidates = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DART_OVERVIEW_EXIT_CANDIDATES,
                0,
            )
            val dartEditingCandidates = intent.getIntExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DART_EDITING_CANDIDATES,
                0,
            )
            val drawerStateReady = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_DRAWER_STATE_READY,
                false,
            )
            val overviewStateReady = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_OVERVIEW_STATE_READY,
                false,
            )
            val editingStateReady = intent.getBooleanExtra(
                NativeHookStatusProtocol.EXTRA_NATIVE_EDITING_STATE_READY,
                false,
            )
            val kind = classifyNativeHookStatus(
                nativeResponse = nativeResponse,
                systemUiReady = systemUiReady,
                legacyMode = legacyMode,
                legacyReady = legacyReady,
                profileResolved = profileResolved,
                nativeReady = nativeReady,
                businessState = businessState,
                bridgeState = bridgeState,
                profileStage = profileStage,
                dartResolverStage = dartResolverStage,
                drawerStateReady = drawerStateReady,
                overviewStateReady = overviewStateReady,
                editingStateReady = editingStateReady,
            )
            return NativeHookStatusUiState(
                kind = kind,
                profileDynamic = profileDynamic,
                legacyMode = legacyMode,
                profileResolved = profileResolved,
                nativeReady = nativeReady,
                systemUiReady = systemUiReady,
                businessState = businessState,
                bridgeState = bridgeState,
                profileStage = profileStage,
                dartResolverStage = dartResolverStage,
                dartDrawerCandidates = dartDrawerCandidates,
                dartTransitionCandidates = dartTransitionCandidates,
                dartOverviewEnterCandidates = dartOverviewEnterCandidates,
                dartOverviewExitCandidates = dartOverviewExitCandidates,
                dartEditingCandidates = dartEditingCandidates,
                drawerStateReady = drawerStateReady,
                overviewStateReady = overviewStateReady,
                editingStateReady = editingStateReady,
                reason = intent.getStringExtra(NativeHookStatusProtocol.EXTRA_REASON).orEmpty(),
            )
        }
    }
}
