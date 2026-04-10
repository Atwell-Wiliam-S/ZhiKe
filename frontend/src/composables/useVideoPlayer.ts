import { ref, onUnmounted } from 'vue'
import type { FragmentDTO } from '@/types/api'

interface UseVideoPlayerOptions {
  videoId: () => number
  signUrl: () => string
  duration: () => number
  fragments: () => FragmentDTO[]
  onProgress?: (currentTime: number) => void
}

export function useVideoPlayer(options: UseVideoPlayerOptions) {
  const videoRef = ref<HTMLVideoElement | null>(null)
  const currentTime = ref(0)
  const isPlaying = ref(false)
  const volume = ref(1)
  const lastWatchTime = ref<number | null>(null)

  let heartbeatTimer: ReturnType<typeof setInterval> | null = null
  const HEARTBEAT_INTERVAL = 30_000

  function togglePlay() {
    if (!videoRef.value) return
    if (videoRef.value.paused) {
      videoRef.value.play()
    } else {
      videoRef.value.pause()
    }
  }

  function seekTo(time: number) {
    if (!videoRef.value) return
    videoRef.value.currentTime = Math.max(0, Math.min(time, options.duration()))
    if (videoRef.value.paused) {
      videoRef.value.play()
    }
  }

  function seekToFragment(fragmentId: number) {
    const fragment = options.fragments().find(f => f.id === fragmentId)
    if (fragment) {
      seekTo(fragment.startTime)
    }
  }

  function getNearbyFragments(rangeSeconds = 30): FragmentDTO[] {
    return options.fragments().filter(
      f => Math.abs(f.startTime - currentTime.value) <= rangeSeconds
    )
  }

  function startHeartbeat() {
    stopHeartbeat()
    heartbeatTimer = setInterval(async () => {
      if (!isPlaying.value) return
      try {
        await fetch('/api/student/heartbeat', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
          },
          body: JSON.stringify({
            videoId: options.videoId(),
            currentTime: Math.floor(currentTime.value),
            status: isPlaying.value ? 'PLAYING' : 'PAUSED',
          }),
        })
      } catch {
      }
    }, HEARTBEAT_INTERVAL)
  }

  function stopHeartbeat() {
    if (heartbeatTimer) {
      clearInterval(heartbeatTimer)
      heartbeatTimer = null
    }
  }

  async function reportProgress() {
    try {
      await fetch('/api/student/progress', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
        },
        body: JSON.stringify({
          videoId: options.videoId(),
          currentTime: Math.floor(currentTime.value),
          duration: options.duration(),
        }),
      })
    } catch {
    }
  }

  function initPlayer() {
    const video = videoRef.value
    if (!video) return

    video.addEventListener('play', () => { isPlaying.value = true; startHeartbeat() })
    video.addEventListener('pause', () => { isPlaying.value = false; reportProgress() })
    video.addEventListener('timeupdate', () => { currentTime.value = video.currentTime })
    video.addEventListener('ended', () => { isPlaying.value = false; reportProgress(); stopHeartbeat() })

    if (lastWatchTime.value && lastWatchTime.value > 0) {
      video.currentTime = lastWatchTime.value
    }
  }

  function handleKeydown(e: KeyboardEvent) {
    if (!videoRef.value) return
    switch (e.key) {
      case ' ':
        e.preventDefault()
        togglePlay()
        break
      case 'ArrowLeft':
        seekTo(currentTime.value - 5)
        break
      case 'ArrowRight':
        seekTo(currentTime.value + 5)
        break
      case 'ArrowUp':
        e.preventDefault()
        volume.value = Math.min(1, volume.value + 0.1)
        videoRef.value.volume = volume.value
        break
      case 'ArrowDown':
        e.preventDefault()
        volume.value = Math.max(0, volume.value - 0.1)
        videoRef.value.volume = volume.value
        break
      case 'f':
        videoRef.value.requestFullscreen?.()
        break
    }
  }

  onUnmounted(() => {
    stopHeartbeat()
    reportProgress()
  })

  return {
    videoRef,
    currentTime,
    isPlaying,
    volume,
    lastWatchTime,
    togglePlay,
    seekTo,
    seekToFragment,
    getNearbyFragments,
    initPlayer,
    handleKeydown,
  }
}
