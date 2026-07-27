import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/features/auth/hooks/useAuth'
import type { LoginRequest } from '@/features/auth/types/auth.types'
import { ROUTES } from '@/shared/constants/routes'
import { ApiError } from '@/shared/api/apiError'

export function useLogin() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)

  const submit = async (data: LoginRequest) => {
    setError(null)

    try {
      await login(data)
      void navigate(ROUTES.dashboard, { replace: true })
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'Login failed')
    }
  }

  return { submit, error }
}
