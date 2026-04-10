import type { VideoDTO, FragmentDTO, NoteDTO, QaLogDTO } from './api'

export interface ThemeToggleProps {
  compact?: boolean
}

export interface ReferenceTagProps {
  fragmentId: number
  startTime: number
  endTime: number
}

export interface VideoTimelineProps {
  video: VideoDTO
  fragments: FragmentDTO[]
}

export interface FragmentCardProps {
  fragment: FragmentDTO
}

export interface KpiCardProps {
  title: string
  value: number | string
  unit?: string
  icon?: string
  trend?: number
}

export interface EmptyStateProps {
  title?: string
  description?: string
  actionText?: string
  onAction?: () => void
}

export interface NoteItemProps {
  note: NoteDTO
  onEdit?: (note: NoteDTO) => void
  onDelete?: (noteId: number) => void
}

export interface ChatMessageProps {
  message: QaLogDTO
  isUser?: boolean
}
