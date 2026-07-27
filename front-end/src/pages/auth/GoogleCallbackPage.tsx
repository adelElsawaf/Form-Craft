import { useEffect, useRef, useState } from 'react'
import { Link as RouterLink, useNavigate, useSearchParams } from 'react-router-dom'
import { Alert, Box, CircularProgress, Link, Stack, Typography } from '@mui/material'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { consumeGoogleOAuthState } from '@/features/auth/lib/googleOAuth'
import { ApiError } from '@/shared/api/apiError'
import { ROUTES } from '@/shared/constants/routes'
import { useDocumentTitle } from '@/shared/hooks/useDocumentTitle'

export default function GoogleCallbackPage() {
  useDocumentTitle('Signing in | FormCraft')

  const { loginWithGoogle } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [error, setError] = useState<string | null>(null)
  const startedRef = useRef(false)

  useEffect(() => {
    if (startedRef.current) {
      return
    }
    startedRef.current = true

    const code = searchParams.get('code')
    const state = searchParams.get('state')
    const oauthError = searchParams.get('error')

    void (async () => {
      if (oauthError) {
        setError('Google sign-in was cancelled or denied.')
        return
      }

      if (!code || !consumeGoogleOAuthState(state)) {
        setError('Invalid Google sign-in response. Please try again.')
        return
      }

      try {
        await loginWithGoogle({ code })
        void navigate(ROUTES.dashboard, { replace: true })
      } catch (err) {
        setError(err instanceof ApiError ? err.message : 'Google sign-in failed')
      }
    })()
  }, [loginWithGoogle, navigate, searchParams])

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'grid',
        placeItems: 'center',
        px: 2,
        bgcolor: 'background.default',
      }}
    >
      <Stack spacing={2} sx={{ width: '100%', maxWidth: 420, textAlign: 'center' }}>
        {error ? (
          <>
            <Alert severity="error" sx={{ borderRadius: 2, textAlign: 'left' }}>
              {error}
            </Alert>
            <Typography variant="body2" color="text.secondary">
              <Link
                component={RouterLink}
                to={ROUTES.auth.login}
                sx={{ fontWeight: 600, textDecoration: 'none' }}
              >
                Back to sign in
              </Link>
            </Typography>
          </>
        ) : (
          <>
            <CircularProgress sx={{ mx: 'auto' }} />
            <Typography color="text.secondary">Completing Google sign-in...</Typography>
          </>
        )}
      </Stack>
    </Box>
  )
}
