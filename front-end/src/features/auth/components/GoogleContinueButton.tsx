import GoogleIcon from '@mui/icons-material/Google'
import { Button } from '@mui/material'
import { beginGoogleLogin } from '@/features/auth/lib/googleOAuth'

type GoogleContinueButtonProps = {
  disabled?: boolean
}

export function GoogleContinueButton({ disabled = false }: GoogleContinueButtonProps) {
  return (
    <Button
      type="button"
      variant="outlined"
      size="large"
      fullWidth
      disabled={disabled}
      startIcon={<GoogleIcon />}
      onClick={() => beginGoogleLogin()}
      sx={{
        py: 1.35,
        color: 'text.primary',
        borderColor: '#EA4335',
        bgcolor: 'background.paper',
        '& .MuiButton-startIcon': {
          color: '#EA4335',
        },
        '&:hover': {
          borderColor: '#EA4335',
          bgcolor: 'action.hover',
          '& .MuiButton-startIcon': {
            color: '#EA4335',
          },
        },
      }}
    >
      Continue with Google
    </Button>
  )
}
