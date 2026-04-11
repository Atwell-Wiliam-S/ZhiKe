# 图标资源使用规范

## 概述
本规范旨在指导团队成员在前端开发中合理使用Iconfont、Icons8和Freepik图标资源，确保图标使用规范统一、管理有序。

## 图标资源来源

### 1. Iconfont
- **特点**：阿里巴巴矢量图标库，提供丰富的图标资源
- **适用场景**：通用图标、业务图标
- **访问地址**：https://www.iconfont.cn/

### 2. Icons8
- **特点**：提供高质量的图标和插图
- **适用场景**：需要更精美、更具设计感的图标
- **访问地址**：https://icons8.com/

### 3. Freepik
- **特点**：提供矢量图形和图标
- **适用场景**：需要创意性、个性化的图标
- **访问地址**：https://www.freepik.com/

## 图标管理机制

### 目录结构
```
src/assets/icons/
├── index.ts          # 图标资源管理入口
├── iconfont/         # Iconfont图标
├── icons8/           # Icons8图标
├── freepik/          # Freepik图标
└── guidelines.md     # 图标使用规范
```

### 命名规范
- **图标文件名**：使用小写字母、数字和连字符（-），如 `user-profile.svg`
- **图标类名**：使用 `icon-{source}-{name}` 格式，如 `icon-iconfont-user`
- **图标引用**：使用统一的工具函数 `iconUtils.getIconUrl()` 获取图标URL

### 图标使用方式

#### 1. 直接引用
```vue
<template>
  <img :src="iconUtils.getIconUrl('iconfont', 'user')" alt="用户图标" />
</template>

<script setup>
import { iconUtils } from '@/assets/icons'
</script>
```

#### 2. 通过CSS类名使用
```css
.icon-iconfont-user {
  background-image: url('/icons/iconfont/user.svg');
  width: 24px;
  height: 24px;
}
```

#### 3. 使用组件库的图标组件
```vue
<template>
  <!-- Vuetify -->
  <v-icon icon="mdi:user"></v-icon>
  
  <!-- TDesign -->
  <t-icon name="user"></t-icon>
</template>
```

## 图标使用规范

### 1. 一致性
- 在同一页面或组件中，使用相同风格的图标
- 保持图标大小、颜色的一致性
- 遵循设计系统的图标规范

### 2. 性能优化
- **按需加载**：只加载页面所需的图标
- **图标压缩**：对SVG图标进行压缩
- **缓存策略**：合理设置图标资源的缓存

### 3. 可访问性
- 为图标添加合适的 `alt` 属性
- 对于功能性图标，添加适当的 aria 属性
- 确保图标在不同主题下的可见性

### 4. 版权注意事项
- 遵守各图标库的使用条款
- 对于商业项目，确保获得适当的授权
- 注明图标的来源和作者（如要求）

## 最佳实践

### 1. 图标选择
- 根据使用场景选择合适的图标库
- 优先使用组件库内置的图标
- 对于特殊需求，选择符合设计风格的自定义图标

### 2. 图标管理
- 定期清理未使用的图标
- 建立图标资源的版本控制
- 维护图标资源的文档和目录结构

### 3. 图标更新
- 当图标库有更新时，及时更新项目中的图标
- 确保图标更新后不会破坏现有功能
- 测试图标在不同设备和浏览器中的显示效果

## 总结
合理使用和管理图标资源，将有助于提升用户界面的美观性和一致性，增强用户体验。遵循本规范，可以确保图标资源的使用规范统一、管理有序。