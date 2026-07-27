import {
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
  type ReactNode,
} from 'react'
import { AuthContext, type AuthContextValue } from '@/app/providers/authContext'
import { authApi } from '@/features/auth/api/auth.api'
import type {
  GoogleAuthRequest,
  LoginRequest,
  RegisterRequest,
} from '@/features/auth/types/auth.types'
import { ApiError } from '@/shared/api/apiError'

type AuthProviderProps = {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<AuthContextValue['user']>(null)
  const [isLoading, setIsLoading] = useState(true)
  const sessionRequestId = useRef(0)

  const refreshSession = useCallback(async () => {
    const requestId = ++sessionRequestId.current

    try {
      const sessionUser = await authApi.getSession()

      if (requestId !== sessionRequestId.current) {
        return
      }

      setUser(sessionUser)
    } catch (error) {
      if (requestId !== sessionRequestId.current) {
        return
      }

      if (error instanceof ApiError && error.status === 401) {
        setUser(null)
      }
    }
  }, [])

  useEffect(() => {
    void (async () => {
      try {
        await refreshSession()
      } finally {
        setIsLoading(false)
      }
    })()
  }, [refreshSession])

  const login = useCallback(async (data: LoginRequest) => {
    sessionRequestId.current += 1
    const authenticatedUser = await authApi.login(data)
    setUser(authenticatedUser)
  }, [])

  const loginWithGoogle = useCallback(async (data: GoogleAuthRequest) => {
    sessionRequestId.current += 1
    const authenticatedUser = await authApi.loginWithGoogle(data)
    setUser(authenticatedUser)
  }, [])

  const register = useCallback(async (data: RegisterRequest) => {
    sessionRequestId.current += 1
    const registeredUser = await authApi.register(data)
    setUser(registeredUser)
  }, [])

  const logout = useCallback(async () => {
    sessionRequestId.current += 1

    try {
      await authApi.logout()
    } finally {
      setUser(null)
    }
  }, [])

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      isAuthenticated: user !== null,
      isLoading,
      login,
      loginWithGoogle,
      register,
      logout,
      refreshSession,
    }),
    [user, isLoading, login, loginWithGoogle, register, logout, refreshSession],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
