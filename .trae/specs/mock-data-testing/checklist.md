# Mock 数据与测试规范 - Verification Checklist

- [x] Checkpoint 1: 环境变量配置完整，包含 .env.development 和 .env.production
- [x] Checkpoint 2: Mock 数据源文件完整，包含 auth.data.ts、qa.data.ts 等
- [x] Checkpoint 3: Mock Handler 文件完整，包含 auth.handler.ts、qa.handler.ts 等
- [x] Checkpoint 4: MSW 配置文件 mocks/browser.ts 已创建
- [x] Checkpoint 5: main.ts 已集成 MSW，包含条件启动逻辑
- [x] Checkpoint 6: TypeScript 编译无错误
- [x] Checkpoint 7: SSE 流式问答支持逐字输出和引用溯源
- [x] Checkpoint 8: 所有 Mock 数据类型安全，符合 DTO 定义
