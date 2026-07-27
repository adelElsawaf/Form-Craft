import { useState } from 'react'
import { Link as RouterLink } from 'react-router-dom'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import EmailOutlinedIcon from '@mui/icons-material/EmailOutlined'
import LockOutlinedIcon from '@mui/icons-material/LockOutlined'
import VisibilityIcon from '@mui/icons-material/Visibility'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import {
  Alert,
  Box,
  Button,
  IconButton,
  InputAdornment,
  Link,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { AuthDivider } from '@/features/auth/components/AuthDivider'
import { GoogleContinueButton } from '@/features/auth/components/GoogleContinueButton'
import { useLogin } from '@/features/auth/hooks/useLogin'
import { loginSchema } from '@/features/auth/schemas/login.schema'
import type { LoginRequest } from '@/features/auth/types/auth.types'
import { ROUTES } from '@/shared/constants/routes'

export function LoginForm() {
  const { submit, error } = useLogin()
  const [showPassword, setShowPassword] = useState(false)
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginRequest>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
    mode: 'onBlur',
  })

  return (
    <Box
      component="form"
      onSubmit={(event) => void handleSubmit(submit)(event)}
      sx={{
        p: { xs: 0, sm: 3.5 },
        borderRadius: 3,
        bgcolor: { xs: 'transparent', sm: 'background.paper' },
        border: { xs: 'none', sm: '1px solid' },
        borderColor: 'divider',
        boxShadow: { xs: 'none', sm: '0 16px 40px rgba(15, 23, 42, 0.06)' },
      }}
    >
      <Stack spacing={2.5}>
        {error && (
          <Alert severity="error" sx={{ borderRadius: 2 }}>
            {error}
          </Alert>
        )}

        <GoogleContinueButton disabled={isSubmitting} />

        <AuthDivider />

        <TextField
          label="Email"
          type="email"
          {...register('email')}
          error={Boolean(errors.email)}
          helperText={errors.email?.message}
          fullWidth
          autoComplete="email"
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <EmailOutlinedIcon sx={{ fontSize: 20, color: 'text.secondary' }} />
                </InputAdornment>
              ),
            },
          }}
        />

        <TextField
          label="Password"
          type={showPassword ? 'text' : 'password'}
          {...register('password')}
          error={Boolean(errors.password)}
          helperText={errors.password?.message}
          fullWidth
          autoComplete="current-password"
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <LockOutlinedIcon sx={{ fontSize: 20, color: 'text.secondary' }} />
                </InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    aria-label={showPassword ? 'Hide password' : 'Show password'}
                    onClick={() => setShowPassword((current) => !current)}
                    edge="end"
                    size="small"
                  >
                    {showPassword ? (
                      <VisibilityOffIcon sx={{ fontSize: 20 }} />
                    ) : (
                      <VisibilityIcon sx={{ fontSize: 20 }} />
                    )}
                  </IconButton>
                </InputAdornment>
              ),
            },
          }}
        />

        <Button
          type="submit"
          variant="contained"
          size="large"
          disabled={isSubmitting}
          fullWidth
          sx={{ py: 1.35, mt: 0.5 }}
        >
          {isSubmitting ? 'Signing in...' : 'Sign in'}
        </Button>

        <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center' }}>
          Don&apos;t have an account?{' '}
          <Link
            component={RouterLink}
            to={ROUTES.auth.register}
            sx={{ fontWeight: 600, textDecoration: 'none' }}
          >
            Create one
          </Link>
        </Typography>
      </Stack>
    </Box>
  )
}
