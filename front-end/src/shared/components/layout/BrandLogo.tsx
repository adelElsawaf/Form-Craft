import { Link as RouterLink } from 'react-router-dom'
import DynamicFormIcon from '@mui/icons-material/DynamicForm'
import { Box, Typography } from '@mui/material'
import { ROUTES } from '@/shared/constants/routes'

type BrandLogoProps = {
  to?: string
}

export function BrandLogo({ to = ROUTES.home }: BrandLogoProps) {
  return (
    <Box
      component={RouterLink}
      to={to}
      sx={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 1,
        textDecoration: 'none',
        color: 'inherit',
      }}
    >
      <Box
        sx={{
          width: 36,
          height: 36,
          borderRadius: 2,
          display: 'grid',
          placeItems: 'center',
          bgcolor: 'primary.main',
          color: 'primary.contrastText',
        }}
      >
        <DynamicFormIcon sx={{ fontSize: 22 }} />
      </Box>
      <Typography
        variant="h6"
        sx={{
          fontWeight: 700,
          letterSpacing: '-0.02em',
          color: 'text.primary',
          display: { xs: 'none', sm: 'block' },
        }}
      >
        FormCraft
      </Typography>
    </Box>
  )
}
