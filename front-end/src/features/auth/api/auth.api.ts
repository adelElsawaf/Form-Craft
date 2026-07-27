import { httpClient } from '@/shared/api/httpClient'
import type {
  AuthUser,
  GoogleAuthRequest,
  LoginRequest,
  RegisterRequest,
} from '@/features/auth/types/auth.types'

const AUTH_BASE = '/api/auth'

export const authApi = {
  register(data: RegisterRequest) {
    return httpClient.post<AuthUser>(`${AUTH_BASE}/register`, data)
  },

  login(data: LoginRequest) {
    return httpClient.post<AuthUser>(`${AUTH_BASE}/login`, data)
  },

  loginWithGoogle(data: GoogleAuthRequest) {
    return httpClient.post<AuthUser>(`${AUTH_BASE}/google`, data)
  },

  logout() {
    return httpClient.post<void>(`${AUTH_BASE}/logout`)
  },

  getSession() {
    return httpClient.get<AuthUser>('/api/users/me')
  },

  refresh() {
    return httpClient.post<void>(`${AUTH_BASE}/refresh`)
  },
}
