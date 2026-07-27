import { parseApiError } from '@/shared/api/apiError'
import type { ApiRequestOptions } from '@/shared/types/api.types'

const API_BASE_URL = import.meta.env.VITE_API_URL

async function request<T>(endpoint: string, options: ApiRequestOptions = {}): Promise<T> {
  const { body, headers, ...rest } = options

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...rest,
    credentials: 'include',
    headers: {
      ...(body !== undefined ? { 'Content-Type': 'application/json' } : {}),
      ...headers,
    },
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (!response.ok) {
    throw await parseApiError(response)
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json() as Promise<T>
}

export const httpClient = {
  get<T>(endpoint: string, options?: Omit<ApiRequestOptions, 'method' | 'body'>) {
    return request<T>(endpoint, { ...options, method: 'GET' })
  },

  post<T>(endpoint: string, body?: unknown, options?: Omit<ApiRequestOptions, 'method' | 'body'>) {
    return request<T>(endpoint, { ...options, method: 'POST', body })
  },

  put<T>(endpoint: string, body?: unknown, options?: Omit<ApiRequestOptions, 'method' | 'body'>) {
    return request<T>(endpoint, { ...options, method: 'PUT', body })
  },

  patch<T>(endpoint: string, body?: unknown, options?: Omit<ApiRequestOptions, 'method' | 'body'>) {
    return request<T>(endpoint, { ...options, method: 'PATCH', body })
  },

  delete<T>(endpoint: string, options?: Omit<ApiRequestOptions, 'method' | 'body'>) {
    return request<T>(endpoint, { ...options, method: 'DELETE' })
  },
}
