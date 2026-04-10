const PREFIX = 'edu_platform_'

function getKey(key: string): string {
  return `${PREFIX}${key}`
}

export const storage = {
  set<T>(key: string, value: T): void {
    try {
      const serializedValue = JSON.stringify(value)
      localStorage.setItem(getKey(key), serializedValue)
    } catch (error) {
      console.error('Error saving to localStorage:', error)
    }
  },

  get<T>(key: string, defaultValue?: T): T | null {
    try {
      const serializedValue = localStorage.getItem(getKey(key))
      if (serializedValue === null) {
        return defaultValue ?? null
      }
      return JSON.parse(serializedValue) as T
    } catch (error) {
      console.error('Error reading from localStorage:', error)
      return defaultValue ?? null
    }
  },

  remove(key: string): void {
    localStorage.removeItem(getKey(key))
  },

  clear(): void {
    const keys = Object.keys(localStorage)
    keys.forEach(key => {
      if (key.startsWith(PREFIX)) {
        localStorage.removeItem(key)
      }
    })
  },

  has(key: string): boolean {
    return localStorage.getItem(getKey(key)) !== null
  }
}
