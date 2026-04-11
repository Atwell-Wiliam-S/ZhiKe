import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/routes'
import './assets/styles/variables.css'
import './assets/styles/index.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { vuetify } from './plugins/vuetify'
import TDesign from 'tdesign-vue-next'
import 'tdesign-vue-next/es/style/index.css'

async function bootstrap() {
  const app = createApp(App)
  app.use(createPinia())
  app.use(router)
  app.use(ElementPlus)
  app.use(vuetify)
  app.use(TDesign)

  app.mount('#app')
}

bootstrap()
