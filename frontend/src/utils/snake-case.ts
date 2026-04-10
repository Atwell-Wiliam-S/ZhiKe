export function snakeCaseToCamelCase(str: string): string {
  return str.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase())
}

export function camelCaseToSnakeCase(str: string): string {
  return str.replace(/([A-Z])/g, '_$1').toLowerCase()
}

export function convertKeysToCamelCase(obj: any): any {
  if (obj === null || typeof obj !== 'object') {
    return obj
  }
  if (Array.isArray(obj)) {
    return obj.map(convertKeysToCamelCase)
  }
  return Object.keys(obj).reduce((result: any, key) => {
    const camelKey = snakeCaseToCamelCase(key)
    result[camelKey] = convertKeysToCamelCase(obj[key])
    return result
  }, {})
}

export function convertKeysToSnakeCase(obj: any): any {
  if (obj === null || typeof obj !== 'object') {
    return obj
  }
  if (Array.isArray(obj)) {
    return obj.map(convertKeysToSnakeCase)
  }
  return Object.keys(obj).reduce((result: any, key) => {
    const snakeKey = camelCaseToSnakeCase(key)
    result[snakeKey] = convertKeysToSnakeCase(obj[key])
    return result
  }, {})
}
