const GOOGLE_AUTH_URL = 'https://accounts.google.com/o/oauth2/v2/auth'
const OAUTH_STATE_KEY = 'formcraft_google_oauth_state'

function createOAuthState(): string {
  const bytes = crypto.getRandomValues(new Uint8Array(16))
  return Array.from(bytes, (byte) => byte.toString(16).padStart(2, '0')).join('')
}

export function beginGoogleLogin(): void {
  const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID
  const redirectUri = import.meta.env.VITE_GOOGLE_REDIRECT_URI

  if (!clientId || !redirectUri) {
    throw new Error('Google OAuth is not configured')
  }

  const state = createOAuthState()
  sessionStorage.setItem(OAUTH_STATE_KEY, state)

  const params = new URLSearchParams({
    client_id: clientId,
    redirect_uri: redirectUri,
    response_type: 'code',
    scope: 'openid email profile',
    state,
    prompt: 'select_account',
  })

  window.location.assign(`${GOOGLE_AUTH_URL}?${params.toString()}`)
}

export function consumeGoogleOAuthState(receivedState: string | null): boolean {
  const expectedState = sessionStorage.getItem(OAUTH_STATE_KEY)
  sessionStorage.removeItem(OAUTH_STATE_KEY)

  return Boolean(expectedState && receivedState && expectedState === receivedState)
}
