export type AuthUser = {
  id: number
  firstName: string
  lastName: string
  email: string
  roles?: string[]
}

export type RegisterRequest = {
  firstName: string
  lastName: string
  email: string
  password: string
}

export type LoginRequest = {
  email: string
  password: string
}

export type GoogleAuthRequest = {
  code: string
}
