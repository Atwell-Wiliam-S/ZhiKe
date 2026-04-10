// src/composables/usePagination.ts
import { ref, reactive, type Ref } from 'vue'

interface PaginationState {
  page: number
  pageSize: number
  total: number
  totalPages: number
}

interface UsePaginationOptions<T> {
  fetchFn: (page: number, pageSize: number) => Promise<{
    records: T[]
    total: number
    totalPages: number
  }>
  defaultPageSize?: number
  immediate?: boolean
}

export function usePagination<T>(options: UsePaginationOptions<T>) {
  const data: Ref<T[]> = ref([])
  const isLoading = ref(false)
  const pagination = reactive<PaginationState>({
    page: 1,
    pageSize: options.defaultPageSize ?? 10,
    total: 0,
    totalPages: 0,
  })

  /** 加载指定页数据 */
  async function loadData(page?: number) {
    isLoading.value = true
    try {
      const targetPage = page ?? pagination.page
      const result = await options.fetchFn(targetPage, pagination.pageSize)
      data.value = result.records
      pagination.total = result.total
      pagination.totalPages = result.totalPages
      pagination.page = targetPage
    } catch (error) {
      console.error('分页加载失败:', error)
    } finally {
      isLoading.value = false
    }
  }

  /** 切换页码 */
  function changePage(page: number) {
    loadData(page)
  }

  /** 切换每页条数 */
  function changePageSize(size: number) {
    pagination.pageSize = size
    pagination.page = 1
    loadData(1)
  }

  /** 刷新当前页 */
  function refresh() {
    loadData()
  }

  // 自动加载
  if (options.immediate !== false) {
    loadData()
  }

  return {
    data,
    isLoading,
    pagination,
    loadData,
    changePage,
    changePageSize,
    refresh,
  }
}