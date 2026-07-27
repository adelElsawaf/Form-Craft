export const authKeys = {
  all: ['auth'] as const,
  session: () => [...authKeys.all, 'session'] as const,
}
export const formKeys = {
  all: ['forms'] as const,
  lists: () => [...formKeys.all, 'list'] as const,
  list: (filters: Record<string, unknown> = {}) =>
    [...formKeys.lists(), filters] as const,
  details: () => [...formKeys.all, 'detail'] as const,
  detail: (id: string | number) => [...formKeys.details(), id] as const,
}

export const submissionKeys = {
  all: ['submissions'] as const,
  lists: () => [...submissionKeys.all, 'list'] as const,
  list: (filters: Record<string, unknown> = {}) =>
    [...submissionKeys.lists(), filters] as const,
  details: () => [...submissionKeys.all, 'detail'] as const,
  detail: (id: string | number) => [...submissionKeys.details(), id] as const,
}

export const workspaceKeys = {
  all: ['workspaces'] as const,
  lists: () => [...workspaceKeys.all, 'list'] as const,
  detail: (id: string | number) => [...workspaceKeys.details(), id] as const,
  details: () => [...workspaceKeys.all, 'detail'] as const,
}

export const userKeys = {
  all: ['users'] as const,
  lists: () => [...userKeys.all, 'list'] as const,
  detail: (id: string | number) => [...userKeys.details(), id] as const,
  details: () => [...userKeys.all, 'detail'] as const,
}

