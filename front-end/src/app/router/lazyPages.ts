import { lazy } from 'react'

export const HomePage = lazy(() => import('@/pages/home'))
export const DashboardPage = lazy(() => import('@/pages/dashboard'))
export const LoginPage = lazy(() => import('@/pages/login'))
export const RegisterPage = lazy(() => import('@/pages/register'))
export const GoogleCallbackPage = lazy(() => import('@/pages/auth'))
