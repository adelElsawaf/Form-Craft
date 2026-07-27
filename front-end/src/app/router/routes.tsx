import type { RouteObject } from 'react-router-dom'
import { AppShellLayout } from '@/app/layout/AppShellLayout'
import { AuthenticatedLayout } from '@/app/layout/AuthenticatedLayout'
import { GuestRoute } from '@/app/router/GuestRoute'
import { LazyPage } from '@/app/router/LazyPage'
import {
  DashboardPage,
  GoogleCallbackPage,
  HomePage,
  LoginPage,
  RegisterPage,
} from '@/app/router/lazyPages'
import { ProtectedRoute } from '@/app/router/ProtectedRoute'
import { ROUTES } from '@/shared/constants/routes'

export const routes: RouteObject[] = [
  {
    element: <AppShellLayout />,
    children: [
      {
        path: ROUTES.home,
        element: <LazyPage page={HomePage} />,
      },
    ],
  },
  {
    path: ROUTES.auth.googleCallback,
    element: <LazyPage page={GoogleCallbackPage} />,
  },
  {
    element: <GuestRoute />,
    children: [
      {
        path: ROUTES.auth.register,
        element: <LazyPage page={RegisterPage} />,
      },
      {
        path: ROUTES.auth.login,
        element: <LazyPage page={LoginPage} />,
      },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <AuthenticatedLayout />,
        children: [
          {
            path: ROUTES.dashboard,
            element: <LazyPage page={DashboardPage} />,
          },
        ],
      },
    ],
  },
]
