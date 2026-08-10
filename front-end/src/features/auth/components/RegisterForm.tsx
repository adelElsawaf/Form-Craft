import { useState } from 'react'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import EmailOutlinedIcon from '@mui/icons-material/EmailOutlined'
import LockOutlinedIcon from '@mui/icons-material/LockOutlined'
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined'
import VisibilityIcon from '@mui/icons-material/Visibility'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import {
  Alert,
  Box,
  Button,
  Grid,
  IconButton,
  InputAdornment,
  Link,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { AuthDivider } from '@/features/auth/components/AuthDivider'
import { GoogleContinueButton } from '@/features/auth/components/GoogleContinueButton'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { registerFormSchema, type RegisterFormValues } from '@/features/auth/schemas/register.schema'
import type { RegisterRequest } from '@/features/auth/types/auth.types'
import { ROUTES } from '@/shared/constants/routes'
import { ApiError } from '@/shared/api/apiError'

const initialValues: RegisterFormValues = {
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
}

export function RegisterForm() {
  const { register: createAccount } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)
  const [showPassword, setShowPassword] = useState(false)
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormValues>({
    resolver: zodResolver(registerFormSchema),
    defaultValues: initialValues,
    mode: 'onBlur',
  })

  const onSubmit = async (values: RegisterFormValues) => {
    setError(null)

    const registerRequest: RegisterRequest = {
      firstName: values.firstName,
      lastName: values.lastName,
      email: values.email,
      password: values.password,
    }

    try {
      await createAccount(registerRequest)
      void navigate(ROUTES.dashboard, { replace: true })
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'Registration failed')
    }
  }

  return (
    <Box
      component="form"
      onSubmit={(event) => void handleSubmit(onSubmit)(event)}
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

        <Grid container spacing={2}>
          <Grid size={{ xs: 12, sm: 6 }}>
            <TextField
              label="First name"
              {...register('firstName')}
              error={Boolean(errors.firstName)}
              helperText={errors.firstName?.message}
              fullWidth
              autoComplete="given-name"
              slotProps={{
                input: {
                  startAdornment: (
                    <InputAdornment position="start">
                      <PersonOutlineOutlinedIcon sx={{ fontSize: 20, color: 'text.secondary' }} />
                    </InputAdornment>
                  ),
                },
              }}
            />
          </Grid>
          <Grid size={{ xs: 12, sm: 6 }}>
            <TextField
              label="Last name"
              {...register('lastName')}
              error={Boolean(errors.lastName)}
              helperText={errors.lastName?.message}
              fullWidth
              autoComplete="family-name"
            />
          </Grid>
        </Grid>

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
          helperText={errors.password?.message ?? 'At least 8 characters'}
          fullWidth
          autoComplete="new-password"
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

        <TextField
          label="Confirm password"
          type={showPassword ? 'text' : 'password'}
          {...register('confirmPassword')}
          error={Boolean(errors.confirmPassword)}
          helperText={errors.confirmPassword?.message}
          fullWidth
          autoComplete="new-password"
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <LockOutlinedIcon sx={{ fontSize: 20, color: 'text.secondary' }} />
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
          {isSubmitting ? 'Creating account...' : 'Create account'}
        </Button>

        <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center' }}>
          Already have an account?{' '}
          <Link
            component={RouterLink}
            to={ROUTES.auth.login}
            sx={{ fontWeight: 600, textDecoration: 'none' }}
          >
            Sign in
          </Link>
        </Typography>
      </Stack>
    </Box>
  )
}
