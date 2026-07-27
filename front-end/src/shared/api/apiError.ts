import type { ApiErrorResponse } from '@/shared/types/api.types'

export class ApiError extends Error {
  readonly status: number
  readonly error: string
  readonly path: string
  readonly timestamp: string

  constructor(response: ApiErrorResponse) {
    super(response.message)
    this.name = 'ApiError'
    this.status = response.status
    this.error = response.error
    this.path = response.path
    this.timestamp = response.timestamp
  }
}

export async function parseApiError(response: Response): Promise<ApiError> {
  try {
    const body = (await response.json()) as ApiErrorResponse
    return new ApiError(body)
  } catch {
    return new ApiError({
      timestamp: new Date().toISOString(),
      status: response.status,
      error: response.statusText,
      message: 'An unexpected error occurred',
      path: response.url,
    })
  }
}
