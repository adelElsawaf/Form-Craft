import { createContext } from 'react'
import type {
  AuthUser,
  GoogleAuthRequest,
  LoginRequest,
  RegisterRequest,
} from '@/features/auth/types/auth.types'

export type AuthContextValue = {
  user: AuthUser | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (data: LoginRequest) => Promise<void>
  loginWithGoogle: (data: GoogleAuthRequest) => Promise<void>
  register: (data: RegisterRequest) => Promise<void>
  logout: () => Promise<void>
  refreshSession: () => Promise<void>
}

export const AuthContext = createContext<AuthContextValue | null>(null)
