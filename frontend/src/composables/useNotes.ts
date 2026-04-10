import { ref, type Ref } from 'vue'
import type { NoteDTO, NoteRequest } from '@/types/api'

interface UseNotesOptions {
  videoId: () => number
}

export function useNotes(options: UseNotesOptions) {
  const notes: Ref<NoteDTO[]> = ref([])
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  /** 获取当前视频的笔记列表 */
  async function fetchNotes() {
    isLoading.value = true
    error.value = null
    try {
      const res = await fetch(
        `/api/student/notes?videoId=${options.videoId()}`,
        { headers: { 'Authorization': `Bearer ${localStorage.getItem('zhike-token')}` } }
      )
      const json = await res.json()
      if (json.code === 200) {
        notes.value = json.data ?? []
      } else {
        error.value = json.message
      }
    } catch (e: any) {
      error.value = e.message
    } finally {
      isLoading.value = false
    }
  }

  /** 创建笔记 */
  async function createNote(data: NoteRequest): Promise<NoteDTO | null> {
    try {
      const res = await fetch('/api/student/notes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
        },
        body: JSON.stringify({ ...data, videoId: options.videoId() }),
      })
      const json = await res.json()
      if (json.code === 200) {
        const note = json.data as NoteDTO
        notes.value.unshift(note)
        return note
      }
      error.value = json.message
      return null
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }

  /** 更新笔记 */
  async function updateNote(id: number, data: Partial<NoteRequest>): Promise<boolean> {
    try {
      const res = await fetch(`/api/student/notes/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
        },
        body: JSON.stringify(data),
      })
      const json = await res.json()
      if (json.code === 200) {
        const idx = notes.value.findIndex(n => n.id === id)
        if (idx !== -1) {
          notes.value[idx] = { ...notes.value[idx], ...json.data }
        }
        return true
      }
      return false
    } catch {
      return false
    }
  }

  /** 删除笔记 */
  async function deleteNote(id: number): Promise<boolean> {
    try {
      const res = await fetch(`/api/student/notes/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${localStorage.getItem('zhike-token')}` },
      })
      const json = await res.json()
      if (json.code === 200) {
        notes.value = notes.value.filter(n => n.id !== id)
        return true
      }
      return false
    } catch {
      return false
    }
  }

  /** 切换笔记公开/私有 */
  async function togglePublic(id: number): Promise<boolean> {
    const note = notes.value.find(n => n.id === id)
    if (!note) return false
    return updateNote(id, { isPublic: !note.isPublic })
  }

  return {
    notes,
    isLoading,
    error,
    fetchNotes,
    createNote,
    updateNote,
    deleteNote,
    togglePublic,
  }
}