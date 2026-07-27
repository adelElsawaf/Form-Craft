import { type ReactNode } from 'react'
import { Link as RouterLink } from 'react-router-dom'
import DynamicFormIcon from '@mui/icons-material/DynamicForm'
import {
  AppBar,
  Box,
  IconButton,
  Toolbar,
  Typography,
} from '@mui/material'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { UserMenu } from '@/shared/components/layout/UserMenu'
import { ROUTES } from '@/shared/constants/routes'

type AppNavbarProps = {
  onMenuClick?: () => void
  menuIcon?: ReactNode
}

export function AppNavbar({ onMenuClick, menuIcon }: AppNavbarProps) {
  const { user } = useAuth()

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        zIndex: (theme) => theme.zIndex.drawer + 1,
        bgcolor: 'background.paper',
      }}
    >
      <Toolbar
        sx={{
          minHeight: { xs: 64, md: 68 },
          px: { xs: 2, md: 3 },
          gap: 1.5,
        }}
      >
        {onMenuClick && (
          <IconButton
            aria-label="Open navigation"
            onClick={onMenuClick}
            edge="start"
            sx={{
              display: { md: 'none' },
              border: '1px solid',
              borderColor: 'divider',
              borderRadius: 2,
            }}
          >
            {menuIcon}
          </IconButton>
        )}

        <Box
          component={RouterLink}
          to={ROUTES.home}
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

        <Box sx={{ flex: 1 }} />

        {user && <UserMenu variant="app" />}
      </Toolbar>
    </AppBar>
  )
}
