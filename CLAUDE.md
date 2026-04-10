# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ZhiKe (智课) is an intelligent teaching closed-loop system with video-based education, AI Q&A (RAG), learning analytics, and multi-role management (student/teacher/admin). Three independent sub-projects in one git repo — not a formal monorepo.

## Build & Run Commands

### Frontend (Vue 3 + TypeScript)
```bash
cd frontend
pnpm install
pnpm dev          # Vite dev server (port 5173)
pnpm build        # vue-tsc -b && vite build
pnpm test         # Vitest (single test exists)
```

### Backend (Spring Boot + Java 17)
```bash
cd backend
mvn clean install -DskipTests
# Start infrastructure first:
docker-compose up -d   # MySQL 8.0, Redis 7.0, ES 8.11, RabbitMQ 3.12
java -jar module-app/target/module-app-1.0.0.jar
```

### User Management (React prototype, standalone)
```bash
cd user-management
npm install
npm run dev
```

## Architecture

### Frontend (`frontend/src/`)
- **Vue 3 Composition API** + **TypeScript** (strict), **Pinia** stores, **Vue Router** (history mode)
- **UI library**: Element Plus + ECharts + Lucide icons
- **Build**: Vite 8 with `@` path alias to `src/`
- **API layer**: Shared Axios instance (`utils/request.ts`) unwraps `Result<T>` envelope (`{code, message, data}`), auto-injects JWT from localStorage, handles 401→logout redirect
- **SSE streaming**: QA chat uses raw `fetch()` in `composables/useSSEChat.ts`, not Axios
- **Mock layer**: MSW 2.0, activated when `VITE_ENABLE_MOCK=true` in dev. Handlers in `mocks/handlers/`, data in `mocks/data/`. Three mock users: `student`/`teacher`/`admin`
- **Theme system**: Three CSS themes via `data-theme` attribute on `<html>`: `student-dark` (default), `student-light`, `teacher-light`

### Backend (`backend/src/main/java/com/zhike/`)
- **Modular monolith**: Each feature is a `module_*` package (security, video, rag, analytics, student, teacher, admin, ai-adapter)
- **Shared code**: `common/` (CORS, Jackson, global exception handler, JwtUtil, Result/PageResult wrappers)
- **ORM**: MyBatis-Plus with camelCase mapping, mappers in each module
- **Security**: Spring Security + JWT (24h expiry, BCrypt, Redis-backed tokens, RBAC via `@PreAuthorize`)
- **Server**: Port 8080, context path `/api`, CORS allows `localhost:5173`
- **Database**: MySQL `zhike_teaching` — 12 tables (DDL in `schema.sql`), auto-initialized via Docker

### Routing & Roles
Routes in `frontend/src/router/routes.ts` are role-gated via `meta.role`. Navigation guards in `guards.ts` check auth state and redirect unauthenticated users to `/login`. Roles: `STUDENT` → `/student/*`, `TEACHER` → `/teacher/*`, `ADMIN` → `/admin/*`.

### Key Patterns
- API modules (`src/api/*.ts`) export typed method objects using the shared Axios instance
- Composables (`src/composables/*.ts`) encapsulate reusable reactive logic (SSE chat, video player with heartbeat, theme, notifications, notes, pagination)
- Pinia stores are thin wrappers around composables (theme, notification); the user store handles auth directly
- Backend controllers return `Result<T>` / `PageResult<T>` — frontend interceptor unwraps these
- Snake_case in API/DB, camelCase in TypeScript — `utils/snake-case.ts` handles conversion

## Design Documentation
Extensive Chinese-language design docs in `design-docs/` (architecture, database DDL, API specs, design system, page descriptions, component specs). Reference design inspiration in `design-md/` (70+ company clones). Trae AI task specs in `.trae/specs/` (13 feature areas).
