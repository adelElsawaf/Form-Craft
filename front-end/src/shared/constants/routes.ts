import { HOME_SECTIONS } from '@/shared/lib/scrollToSection'

export const ROUTES = {
  home: '/',
  dashboard: '/dashboard',
  auth: {
    login: '/login',
    register: '/register',
    googleCallback: '/auth/google/callback',
  },
  forms: {
    list: '/forms',
    create: '/forms/new',
    detail: (id: string | number) => `/forms/${id}`,
    edit: (id: string | number) => `/forms/${id}/edit`,
  },
  submissions: {
    list: '/submissions',
    detail: (id: string | number) => `/submissions/${id}`,
  },
  workspaces: {
    list: '/workspaces',
    detail: (id: string | number) => `/workspaces/${id}`,
  },
  users: {
    list: '/users',
    detail: (id: string | number) => `/users/${id}`,
  },
  settings: {
    root: '/settings',
  },
} as const

export const PUBLIC_NAV_LINKS = [
  { label: 'How it works', sectionId: HOME_SECTIONS.howItWorks },
  { label: 'Use cases', sectionId: HOME_SECTIONS.useCases },
  { label: 'Pricing', sectionId: HOME_SECTIONS.pricing },
] as const
