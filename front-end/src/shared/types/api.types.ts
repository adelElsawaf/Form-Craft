export type ApiErrorResponse = {
  timestamp: string
  status: number
  error: string
  message: string
  path: string
}

export type PaginatedResponse<T> = {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
}

export type ApiRequestOptions = Omit<RequestInit, 'body'> & {
  body?: unknown
}
