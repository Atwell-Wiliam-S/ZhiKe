// 图标资源管理

// 1. Iconfont 图标
// 可以通过以下方式引入
// import { IconFontIcon } from '@/assets/icons/iconfont'

// 2. Icons8 图标
// 可以通过以下方式引入
// import { Icons8Icon } from '@/assets/icons/icons8'

// 3. Freepik 图标
// 可以通过以下方式引入
// import { FreepikIcon } from '@/assets/icons/freepik'

// 4. 统一图标组件
import { createApp } from 'vue'

// 注册全局图标组件
export function registerIconComponents(app: ReturnType<typeof createApp>) {
  // 这里可以注册全局图标组件
  // 例如: app.component('Icon', IconComponent)
}

// 图标使用工具函数
export const iconUtils = {
  // 获取图标URL
  getIconUrl: (type: 'iconfont' | 'icons8' | 'freepik', name: string): string => {
    switch (type) {
      case 'iconfont':
        return `/icons/iconfont/${name}.svg`
      case 'icons8':
        return `/icons/icons8/${name}.svg`
      case 'freepik':
        return `/icons/freepik/${name}.svg`
      default:
        return ''
    }
  },

  // 构建图标类名
  getIconClass: (type: 'iconfont' | 'icons8' | 'freepik', name: string): string => {
    return `icon-${type}-${name}`
  }
}
