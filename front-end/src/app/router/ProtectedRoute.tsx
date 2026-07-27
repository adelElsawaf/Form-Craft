import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { LoadingScreen } from '@/shared/components/feedback/LoadingScreen'
import { ROUTES } from '@/shared/constants/routes'

export function ProtectedRoute() {
  const { isAuthenticated, isLoading } = useAuth()

  if (isLoading) {
    return <LoadingScreen />
  }

  if (!isAuthenticated) {
    return <Navigate to={ROUTES.home} replace />
  }

  return <Outlet />
}
