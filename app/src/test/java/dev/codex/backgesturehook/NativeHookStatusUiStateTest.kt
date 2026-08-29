package dev.codex.backgesturehook

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NativeHookStatusUiStateTest {
    private fun classify(
        nativeReady: Boolean = true,
        drawerReady: Boolean = true,
        overviewReady: Boolean = true,
        editingReady: Boolean = true,
        dartStage: Int = 5,
    ): NativeHookStatusKind = classifyNativeHookStatus(
        nativeResponse = true,
        systemUiReady = true,
        legacyMode = false,
        legacyReady = false,
        profileResolved = true,
        nativeReady = nativeReady,
        businessState = 3,
        bridgeState = 3,
        profileStage = 7,
        dartResolverStage = dartStage,
        drawerStateReady = drawerReady,
        overviewStateReady = overviewReady,
        editingStateReady = editingReady,
    )

    @Test
    fun readyRequiresBothFeatureHooks() {
        assertEquals(NativeHookStatusKind.Ready, classify())
        assertEquals(
            NativeHookStatusKind.NativeNotReady,
            classify(drawerReady = false),
        )
        assertEquals(
            NativeHookStatusKind.NativeNotReady,
            classify(overviewReady = false),
        )
        assertEquals(
            NativeHookStatusKind.NativeNotReady,
            classify(editingReady = false),
        )
    }

    @Test
    fun resolverRejectionPrecedesNativeReadiness() {
        assertEquals(
            NativeHookStatusKind.ProfileRejected,
            classify(nativeReady = false, dartStage = 104),
        )
    }

    @Test
    fun dartRuntimeResolvedIncludesInstalledHookHealth() {
        val healthy = NativeHookStatusUiState(
            kind = NativeHookStatusKind.Ready,
            dartResolverStage = 5,
            dartDrawerCandidates = 1,
            dartTransitionCandidates = 1,
            dartOverviewEnterCandidates = 1,
            dartOverviewExitCandidates = 1,
            dartEditingCandidates = 1,
            drawerStateReady = true,
            overviewStateReady = true,
            editingStateReady = true,
        )
        assertTrue(healthy.dartRuntimeResolved)
        assertFalse(healthy.copy(drawerStateReady = false).dartRuntimeResolved)
        assertFalse(healthy.copy(overviewStateReady = false).dartRuntimeResolved)
        assertFalse(healthy.copy(editingStateReady = false).dartRuntimeResolved)
    }
}
